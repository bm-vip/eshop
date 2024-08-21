package com.eshop.client.repository;

import com.eshop.client.entity.CoinEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinRepository extends BaseRepository<CoinEntity, Long> {
}
