package com.eshop.app.repository;

import org.springframework.stereotype.Repository;
import com.eshop.app.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, Long> {
	 Optional<UserEntity> findByUserName(String userName);
	 boolean existsByUid(String uid);
}
