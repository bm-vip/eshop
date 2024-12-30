package com.eshop.app.repository;

import com.eshop.app.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, UUID> {
	 Optional<UserEntity> findByUserName(String userName);
	 Optional<UserEntity> findByEmail(String email);
	 boolean existsByUid(String uid);
	 Optional<UserEntity> findByUid(String uid);
	 Optional<UserEntity> findByUserNameOrEmail(String userName, String email);
	@Query("""
        SELECT coalesce(COUNT(DISTINCT u),0) 
        FROM UserEntity u
        JOIN WalletEntity w ON u.id = w.user.id
        WHERE u.parent.id = :userId   
        AND w.status = com.eshop.app.enums.EntityStatusType.Active and w.transactionType = com.eshop.app.enums.TransactionType.DEPOSIT
    """)
	Long countActiveChildrenByUserId(@Param("userId") UUID userId);
}
