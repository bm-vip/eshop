package com.eshop.app.service.impl;

import com.eshop.app.config.MessageConfig;
import com.eshop.app.entity.QUserEntity;
import com.eshop.app.entity.UserEntity;
import com.eshop.app.enums.CurrencyType;
import com.eshop.app.enums.RoleType;
import com.eshop.app.filter.UserFilter;
import com.eshop.app.mapping.UserMapper;
import com.eshop.app.model.*;
import com.eshop.app.repository.CountryRepository;
import com.eshop.app.repository.RoleRepository;
import com.eshop.app.repository.UserRepository;
import com.eshop.app.repository.WalletRepository;
import com.eshop.app.service.NotificationService;
import com.eshop.app.service.RoleService;
import com.eshop.app.service.UserService;
import com.eshop.app.util.ReferralCodeGenerator;
import com.eshop.exception.common.BadRequestException;
import com.eshop.exception.common.NotFoundException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static com.eshop.app.util.MapperHelper.get;


@Service
public class UserServiceImpl extends BaseServiceImpl<UserFilter,UserModel, UserEntity, Long> implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MessageConfig messages;
    private final ResourceLoader resourceLoader;
    private final NotificationService notificationService;
    private final WalletRepository walletRepository;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, MessageConfig messages, ResourceLoader resourceLoader, NotificationService notificationService, WalletRepository walletRepository) {
        super(userRepository, userMapper);
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.messages = messages;
        this.resourceLoader = resourceLoader;
        this.notificationService = notificationService;
        this.walletRepository = walletRepository;
    }

    @Override
    public UserModel findByUserName(String userName) {
        return mapper.toModel(userRepository.findByUserName(userName).orElseThrow(() -> new NotFoundException("userName: " + userName)));
    }

    @Override
    public Page<UserModel> findAll(UserFilter filter, Pageable pageable) {
        return super.findAll(filter, pageable).map(m-> {
            m.setDeposit(walletRepository.totalDepositGroupedByCurrency(m.getId()).stream().filter(f->f.getCurrency().equals(CurrencyType.USDT)).map(BalanceModel::getTotalAmount).findFirst().orElse(BigDecimal.ZERO));
            m.setWithdrawal(walletRepository.totalWithdrawalGroupedByCurrency(m.getId()).stream().filter(f->f.getCurrency().equals(CurrencyType.USDT)).map(BalanceModel::getTotalAmount).findFirst().orElse(BigDecimal.ZERO));
            m.setBonus(walletRepository.totalBonusGroupedByCurrency(m.getId()).stream().filter(f->f.getCurrency().equals(CurrencyType.USDT)).map(BalanceModel::getTotalAmount).findFirst().orElse(BigDecimal.ZERO));
            m.setReward(walletRepository.totalRewardGroupedByCurrency(m.getId()).stream().filter(f->f.getCurrency().equals(CurrencyType.USDT)).map(BalanceModel::getTotalAmount).findFirst().orElse(BigDecimal.ZERO));
            return m;
        });
    }

    @Override
    public PageModel<UserModel> findAllTable(UserFilter filter, Pageable pageable) {
        var data = super.findAllTable(filter, pageable);
        if(!CollectionUtils.isEmpty(data.getData())) {
            for (UserModel m : data.getData()) {
                m.setDeposit(walletRepository.totalDepositGroupedByCurrency(m.getId()).stream().filter(f->f.getCurrency().equals(CurrencyType.USDT)).map(BalanceModel::getTotalAmount).findFirst().orElse(BigDecimal.ZERO));
                m.setWithdrawal(walletRepository.totalWithdrawalGroupedByCurrency(m.getId()).stream().filter(f->f.getCurrency().equals(CurrencyType.USDT)).map(BalanceModel::getTotalAmount).findFirst().orElse(BigDecimal.ZERO));
                m.setBonus(walletRepository.totalBonusGroupedByCurrency(m.getId()).stream().filter(f->f.getCurrency().equals(CurrencyType.USDT)).map(BalanceModel::getTotalAmount).findFirst().orElse(BigDecimal.ZERO));
                m.setReward(walletRepository.totalRewardGroupedByCurrency(m.getId()).stream().filter(f->f.getCurrency().equals(CurrencyType.USDT)).map(BalanceModel::getTotalAmount).findFirst().orElse(BigDecimal.ZERO));
            }
        }
        return data;
    }

    @Override
    @Transactional
    public UserModel register(UserModel model) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserName(model.getUserName());
        if (optionalUserEntity.isPresent())
            throw new BadRequestException("userName is already taken!");

        var entity = mapper.toEntity(model);
        if (StringUtils.hasLength(model.getPassword()))
            entity.setPassword(bCryptPasswordEncoder.encode(model.getPassword()));
        else
            entity.setPassword(bCryptPasswordEncoder.encode("12345"));
        entity.setActive(true);
        var role = roleRepository.findByRole(RoleType.USER).get();
        entity.setRoles(new HashSet<>(Arrays.asList(role)));
        if (StringUtils.hasLength(model.getReferralCode())) {
            userRepository.findByUid(model.getReferralCode()).ifPresent(p -> entity.setParent(p));
        }
        entity.setUid(getUid());
        var createdUser = mapper.toModel(repository.save(entity));
        sendWelcomeNotification(createdUser.getId());
        return createdUser;
    }

    private String getUid() {
        var uid = ReferralCodeGenerator.generateReferralCode();
        if(!userRepository.existsByUid(uid)) {
            return uid;
        }
        return getUid();
    }

    @Override
    @Transactional
    public UserModel update(UserModel model) {
        UserEntity entity = repository.findById(model.getId()).orElseThrow(() -> new NotFoundException("id: " + model.getId()));
        mapper.updateEntity(model, entity);

        if (StringUtils.hasLength(model.getPassword()))
            entity.setPassword(bCryptPasswordEncoder.encode(model.getPassword()));
        if(get(()->model.getCountry().getId())!=null)
            entity.setCountry(entityManager.getReference(entity.getCountry().getClass(), model.getCountry().getId()));
        if(get(()->model.getParent().getId())!=null)
            entity.setParent(entityManager.getReference(entity.getParent().getClass(), model.getParent().getId()));
        return mapper.toModel(repository.save(entity));
    }

    @Override
    @Transactional
    public UserModel create(UserModel model) {
        var entity = mapper.toEntity(model);

        if (StringUtils.hasLength(model.getPassword()))
            entity.setPassword(bCryptPasswordEncoder.encode(model.getPassword()));
        else
            entity.setPassword(bCryptPasswordEncoder.encode("12345"));

        entity.setUid(getUid());
        var createdUser = mapper.toModel(repository.save(entity));
        sendWelcomeNotification(createdUser.getId());
        return createdUser;
    }

    @Override
    public Predicate queryBuilder(UserFilter filter) {
        BooleanBuilder builder = new BooleanBuilder();
        QUserEntity path = QUserEntity.userEntity;

        if(!RoleType.hasRole(RoleType.ADMIN)) {
            builder.and(path.roles.any().role.ne(RoleType.ADMIN));
        }
        filter.getId().ifPresent(v -> builder.and(path.id.eq(v)));
        filter.getTitle().ifPresent(v -> builder.and(path.firstName.toLowerCase().contains(v.toLowerCase())).or(path.lastName.toLowerCase().contains(v.toLowerCase())));
        filter.getActive().ifPresent(v -> builder.and(path.active.eq(v)));
        filter.getUserName().ifPresent(v -> builder.and(path.userName.eq(v)));
        filter.getEmail().ifPresent(v -> builder.and(path.email.toLowerCase().eq(v.toLowerCase())));
        filter.getUid().ifPresent(v -> builder.and(path.uid.eq(v)));
        filter.getParentId().ifPresent(v -> builder.and(path.parent.id.eq(v)));
        filter.getTreePath().ifPresent(v -> builder.and(path.treePath.eq(v)));
        filter.getWalletAddress().ifPresent(v -> builder.and(path.walletAddress.eq(v)));
        filter.getFirstName().ifPresent(v -> builder.and(path.firstName.toLowerCase().contains(v.toLowerCase())));
        filter.getLastName().ifPresent(v -> builder.and(path.lastName.toLowerCase().contains(v.toLowerCase())));
        filter.getHasParent().ifPresent(v-> { if(v) builder.and(path.parent.isNotNull()); else builder.and(path.parent.isNull());});
        filter.getProfileImageUrl().ifPresent(v -> builder.and(path.profileImageUrl.eq(v)));
        filter.getCountryId().ifPresent(v -> builder.and(path.country.id.eq(v)));

        return builder;
    }
    @SneakyThrows
    public void sendWelcomeNotification(Long recipientId) {
        var recipient = findById(recipientId);
        String siteName = messages.getMessage("siteName");
        String siteUrl = messages.getMessage("siteUrl");
        Resource emailTemplateResource = resourceLoader.getResource("classpath:templates/welcome-email.html");
        String emailContent = new String(Files.readAllBytes(Paths.get(emailTemplateResource.getURI())));
        emailContent = emailContent.replace("[user_first_name]", recipient.getFirstName());
        emailContent = emailContent.replace("[YourAppName]", siteName);
        emailContent = emailContent.replace("[YourSiteUrl]", siteUrl);

        notificationService.create(new NotificationModel()
                .setSubject(String.format("Welcome to %s!", siteName))
                .setBody(emailContent)
                .setSender(new UserModel().setUserId(2L))
                .setRecipient(new UserModel().setUserId(recipientId)));
    }
}
