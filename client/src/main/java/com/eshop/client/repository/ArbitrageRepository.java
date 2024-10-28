package com.eshop.client.repository;

import com.eshop.client.entity.ArbitrageEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArbitrageRepository extends BaseRepository<ArbitrageEntity, Long> {
    long countAllByUserId(long userId);
    Optional<ArbitrageEntity> findTopByUserIdAndSubscriptionIdOrderByCreatedDateDesc(Long userId, Long subscriptionId);
}
