package com.eshop.app.repository;

import com.eshop.app.entity.ArbitrageEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ArbitrageRepository extends BaseRepository<ArbitrageEntity, Long> {
    long countAllByUserId(long userId);
}
