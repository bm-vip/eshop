package com.eshop.client.service;

import com.eshop.client.filter.NotificationFilter;
import com.eshop.client.model.NotificationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService extends BaseService<NotificationFilter, NotificationModel, Long>, LogicalDeletedService<Long>{
    Page<NotificationModel> findAllByRecipientId(Long recipientId, Pageable pageable);
    Page<NotificationModel> findAllBySenderId(Long senderId, Pageable pageable);
    Page<NotificationModel> findAllByRecipientIdAndNotRead(Long recipientId, Pageable pageable);
}
