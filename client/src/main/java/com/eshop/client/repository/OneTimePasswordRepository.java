package com.eshop.client.repository;

import com.eshop.client.entity.OneTimePasswordEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OneTimePasswordRepository extends BaseRepository<OneTimePasswordEntity, Long> {
    Optional<OneTimePasswordEntity> findByUserIdAndPasswordAndConsumedFalse(Long userId, String password);
}
