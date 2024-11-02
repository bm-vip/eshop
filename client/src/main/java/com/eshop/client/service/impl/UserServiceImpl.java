package com.eshop.client.service.impl;

import com.eshop.client.config.MessageConfig;
import com.eshop.client.entity.QUserEntity;
import com.eshop.client.entity.UserEntity;
import com.eshop.client.enums.RoleType;
import com.eshop.client.filter.UserFilter;
import com.eshop.client.mapping.UserMapper;
import com.eshop.client.model.NotificationModel;
import com.eshop.client.model.RoleModel;
import com.eshop.client.model.UserModel;
import com.eshop.client.repository.CountryRepository;
import com.eshop.client.repository.CountryUsers;
import com.eshop.client.repository.UserRepository;
import com.eshop.client.service.NotificationService;
import com.eshop.client.service.RoleService;
import com.eshop.client.service.UserService;
import com.eshop.client.util.ReferralCodeGenerator;
import com.eshop.exception.common.BadRequestException;
import com.eshop.exception.common.NotFoundException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl extends BaseServiceImpl<UserFilter,UserModel, UserEntity, Long> implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MessageConfig messages;
    private final ResourceLoader resourceLoader;
    private final NotificationService notificationService;
    private final CountryRepository countryRepository;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder, MessageConfig messages, ResourceLoader resourceLoader, NotificationService notificationService, CountryRepository countryRepository) {
        super(userRepository, userMapper);
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.messages = messages;
        this.resourceLoader = resourceLoader;
        this.notificationService = notificationService;
        this.countryRepository = countryRepository;
    }

    @Override
    public UserModel findByUserName(String userName) {
        return mapper.toModel(userRepository.findByUserName(userName).orElseThrow(() -> new NotFoundException("userName: " + userName)));
    }
    @Override
    public UserModel findByEmail(String email) {
        return mapper.toModel(userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("userName: " + email)));
    }
    @Override
    public List<CountryUsers> findAllUserCountByCountry() {
        return userRepository.findAllUserCountByCountry();
    }

    @Override
    @Transactional
    public UserModel register(UserModel model) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserName(model.getUserName());
        if (optionalUserEntity.isPresent())
            throw new BadRequestException("userName is already taken!");

        if (StringUtils.hasLength(model.getPassword()))
            model.setPassword(bCryptPasswordEncoder.encode(model.getPassword()));
        model.setActive(true);
        RoleModel roleModel = roleService.findByRole(RoleType.USER);
        model.setRoles(new HashSet<>(Arrays.asList(roleModel)));
        if (StringUtils.hasLength(model.getReferralCode())) {
            userRepository.findByUid(model.getReferralCode()).ifPresent(p -> model.setParent(mapper.toModel(p)));
        }
        var entity = mapper.toEntity(model);
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
        if (StringUtils.hasLength(model.getPassword()))
            model.setPassword(bCryptPasswordEncoder.encode(model.getPassword()));

        UserEntity entity = repository.findById(model.getId()).orElseThrow(() -> new NotFoundException("id: " + model.getId()));
        entity = mapper.updateEntity(model, entity);
        return mapper.toModel(repository.save(entity));
    }

    @Override
    @Transactional
    public UserModel create(UserModel model) {
        var entity = mapper.toEntity(model);
        if (StringUtils.hasLength(model.getPassword()))
            entity.setPassword(bCryptPasswordEncoder.encode(model.getPassword()));
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
        filter.getTreePath().ifPresent(v -> builder.and(path.treePath.like(v)));
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
