package com.eshop.app.repository;

import com.eshop.app.entity.CoinEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinRepository extends BaseRepository<CoinEntity, Long> {
}
