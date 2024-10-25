package com.eshop.client.repository;

import com.eshop.client.entity.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends BaseRepository<NotificationEntity, Long> {
    Page<NotificationEntity> findAllByRecipientIdOrderByCreatedDateDesc(Long recipientId, Pageable pageable);
    Page<NotificationEntity> findAllByRecipientIdAndReadIsFalseOrderByCreatedDateDesc(Long recipientId, Pageable pageable);
}
