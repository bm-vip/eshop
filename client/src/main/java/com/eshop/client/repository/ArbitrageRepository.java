package com.eshop.client.repository;

import com.eshop.client.entity.ArbitrageEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ArbitrageRepository extends BaseRepository<ArbitrageEntity, Long> {
    long countAllByUserId(long userId);
}
