package com.eshop.app.service;

import com.eshop.app.filter.NotificationFilter;
import com.eshop.app.model.NotificationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService extends BaseService<NotificationFilter, NotificationModel, Long>, LogicalDeletedService<Long>{
    Page<NotificationModel> findAllUnreadByRecipientId(Long recipientId, Pageable pageable);
}
