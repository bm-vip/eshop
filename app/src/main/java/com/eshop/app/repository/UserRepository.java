package com.eshop.app.repository;

import com.eshop.app.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, Long> {
	 Optional<UserEntity> findByUserName(String userName);
	 boolean existsByUid(String uid);
	 Optional<UserEntity> findByUid(String uid);
}
