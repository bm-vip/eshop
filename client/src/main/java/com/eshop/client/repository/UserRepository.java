package com.eshop.client.repository;

import org.springframework.stereotype.Repository;
import com.eshop.client.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, Long> {
	 Optional<UserEntity> findByUserName(String userName);
	 boolean existsByUid(String uid);
	Optional<UserEntity> findByUid(String uid);
}
