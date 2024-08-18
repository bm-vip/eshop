package com.eshop.app.repository;

import com.eshop.app.entity.SubscriptionEntity;
import com.eshop.app.entity.UserEntity;
import com.eshop.app.enums.EntityStatusType;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends BaseRepository<SubscriptionEntity, Long> {
    SubscriptionEntity findByUserIdAndStatus(long userId, EntityStatusType status);
}
