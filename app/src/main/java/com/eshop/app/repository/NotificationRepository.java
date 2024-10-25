package com.eshop.app.repository;

import com.eshop.app.entity.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends BaseRepository<NotificationEntity, Long> {
    Page<NotificationEntity> findAllByRecipientIdAndReadIsFalse(Long recipientId, Pageable pageable);
}
