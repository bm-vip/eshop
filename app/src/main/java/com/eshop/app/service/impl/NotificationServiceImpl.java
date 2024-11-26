package com.eshop.app.service.impl;


import com.eshop.app.entity.NotificationEntity;
import com.eshop.app.entity.QNotificationEntity;
import com.eshop.app.filter.NotificationFilter;
import com.eshop.app.mapping.NotificationMapper;
import com.eshop.app.model.NotificationModel;
import com.eshop.app.model.UserModel;
import com.eshop.app.repository.NotificationRepository;
import com.eshop.app.repository.UserRepository;
import com.eshop.app.service.NotificationService;
import com.eshop.app.util.SessionHolder;
import com.eshop.exception.common.NotFoundException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class NotificationServiceImpl extends BaseServiceImpl<NotificationFilter, NotificationModel, NotificationEntity, Long> implements NotificationService {
    private final NotificationRepository repository;
    private final NotificationMapper mapper;
    private final SessionHolder sessionHolder;
    private final UserRepository userRepository;

    public NotificationServiceImpl(NotificationRepository repository, NotificationMapper mapper, SessionHolder sessionHolder, UserRepository userRepository) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.sessionHolder = sessionHolder;
        this.userRepository = userRepository;
    }

    @Override
    public JpaRepository<NotificationEntity, Long> getRepository() {
        return repository;
    }

    @Override
    public Page<NotificationModel> findAllUnreadByRecipientId(Long recipientId, Pageable pageable) {
        return repository.findAllByRecipientIdAndReadIsFalse(recipientId, pageable).map(mapper::toModel);
    }
    @Override
    public NotificationModel findById(Long id) {
        var entity = repository.findById(id).orElseThrow(() -> new NotFoundException("id: " + id));
        if(entity.getRecipient().getId().equals(sessionHolder.getCurrentUser().getId()))
            entity.setRead(true);
        repository.save(entity);
        return mapper.toModel(entity);
    }

    @Override
    public Predicate queryBuilder(NotificationFilter filter) {
        BooleanBuilder builder = new BooleanBuilder();
        QNotificationEntity path = QNotificationEntity.notificationEntity;

        filter.getId().ifPresent(v -> builder.and(path.id.eq(v)));
        filter.getSenderId().ifPresent(v -> builder.and(path.sender.id.eq(v)));
        filter.getRecipientId().ifPresent(v -> builder.and(path.recipient.id.eq(v)));
        filter.getSubject().ifPresent(v -> builder.and(path.subject.toLowerCase().contains(v.toLowerCase())));
        filter.getBody().ifPresent(v -> builder.and(path.body.toLowerCase().contains(v.toLowerCase())));
        filter.getRead().ifPresent(v -> builder.and(path.read.eq(v)));
        
        return builder;
    }

    @Override
    public NotificationModel create(NotificationModel model) {
        if (model.isAllRecipients()) {
            userRepository.findAll().forEach(user->{
                model.setRecipient(new UserModel().setUserId(user.getId()));
                super.create(model);
            });
        }
        else if(!CollectionUtils.isEmpty(model.getRecipients())) {
            model.getRecipients().forEach(recipientId -> {
                model.setRecipient(new UserModel().setUserId(recipientId));
                super.create(model);
            });
            return model;
        }
        return super.create(model);
    }

    @Override
    public NotificationModel update(NotificationModel model) {
        if (model.isAllRecipients()) {
            userRepository.findAll().forEach(user->{
                model.setRecipient(new UserModel().setUserId(user.getId()));
                super.update(model);
            });
        }
        else if(!CollectionUtils.isEmpty(model.getRecipients())) {
            model.getRecipients().forEach(recipientId -> {
                model.setRecipient(new UserModel().setUserId(recipientId));
                super.update(model);
            });
            return model;
        }
        return super.update(model);
    }
}
