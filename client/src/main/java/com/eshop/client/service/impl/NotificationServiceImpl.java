package com.eshop.client.service.impl;


import com.eshop.client.entity.NotificationEntity;
import com.eshop.client.entity.QNotificationEntity;
import com.eshop.client.filter.NotificationFilter;
import com.eshop.client.mapping.NotificationMapper;
import com.eshop.client.model.NotificationModel;
import com.eshop.client.repository.NotificationRepository;
import com.eshop.client.service.NotificationService;
import com.eshop.exception.common.NotFoundException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationServiceImpl extends BaseServiceImpl<NotificationFilter, NotificationModel, NotificationEntity, Long> implements NotificationService {
    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    public NotificationServiceImpl(NotificationRepository repository, NotificationMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public JpaRepository<NotificationEntity, Long> getRepository() {
        return repository;
    }

    @Override
    public NotificationModel findById(Long id) {
        var entity = repository.findById(id).orElseThrow(() -> new NotFoundException("id: " + id));
        entity.setRead(true);
        repository.save(entity);
        return mapper.toModel(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationModel> findAllByRecipientId(Long recipientId, Pageable pageable) {
        return repository.findAllByRecipientIdOrderByCreatedDateDesc(recipientId, pageable).map(mapper::toModel);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<NotificationModel> findAllByRecipientIdAndNotRead(Long recipientId, Pageable pageable) {
        return repository.findAllByRecipientIdAndReadIsFalseOrderByCreatedDateDesc(recipientId, pageable).map(mapper::toModel);
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

}
