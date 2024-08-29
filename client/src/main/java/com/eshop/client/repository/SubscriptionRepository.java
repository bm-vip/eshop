package com.eshop.client.repository;

import com.eshop.client.entity.SubscriptionEntity;
import com.eshop.client.enums.EntityStatusType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends BaseRepository<SubscriptionEntity, Long> {
    SubscriptionEntity findByUserIdAndStatus(long userId, EntityStatusType status);
}
