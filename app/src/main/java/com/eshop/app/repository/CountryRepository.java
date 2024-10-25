package com.eshop.app.repository;

import com.eshop.app.entity.CountryEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends BaseRepository<CountryEntity, Long> {
}
