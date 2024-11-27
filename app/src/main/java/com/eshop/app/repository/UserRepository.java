package com.eshop.app.repository;

import com.eshop.app.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, UUID> {
	 Optional<UserEntity> findByUserName(String userName);
	 boolean existsByUid(String uid);
	 Optional<UserEntity> findByUid(String uid);
	 Optional<UserEntity> findByUserNameOrEmail(String userName, String email);
}
