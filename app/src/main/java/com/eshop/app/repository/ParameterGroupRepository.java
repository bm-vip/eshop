package com.eshop.app.repository;

import com.eshop.app.entity.ParameterGroupEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ParameterGroupRepository extends BaseRepository<ParameterGroupEntity, Long> {
	ParameterGroupEntity findByCode(String role);
}
