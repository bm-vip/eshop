package com.eshop.client.service;

import com.eshop.client.filter.NotificationFilter;
import com.eshop.client.model.NotificationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface NotificationService extends BaseService<NotificationFilter, NotificationModel, Long>, LogicalDeletedService<Long>{
    Page<NotificationModel> findAllByRecipientId(UUID recipientId, Pageable pageable);
    Page<NotificationModel> findAllBySenderId(UUID senderId, Pageable pageable);
    Page<NotificationModel> findAllByRecipientIdAndNotRead(UUID recipientId, Pageable pageable);
}
