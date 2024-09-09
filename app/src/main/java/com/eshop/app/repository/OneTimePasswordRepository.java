package com.eshop.app.repository;

import com.eshop.app.entity.OneTimePasswordEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OneTimePasswordRepository extends BaseRepository<OneTimePasswordEntity, Long> {
    Optional<OneTimePasswordEntity> findByUserIdAndPasswordAndConsumedFalse(Long userId, String password);
}
