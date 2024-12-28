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
        SELECT DISTINCT u 
        FROM UserEntity u
        JOIN WalletEntity w ON u.id = w.user.id
        WHERE u.treePath LIKE CONCAT(:userId, ',%') 
           OR u.treePath LIKE CONCAT('%,', :userId, ',%') 
           OR u.treePath LIKE CONCAT('%,', :userId)
           OR u.treePath = :userId
        AND w.status = com.eshop.app.enums.EntityStatusType.Active and w.transactionType = com.eshop.app.enums.TransactionType.DEPOSIT
    """)
	List<UserEntity> findActiveChildrenByUserId(@Param("userId") String userId);
	@Query("""
        SELECT COUNT(DISTINCT u) 
        FROM UserEntity u
        JOIN WalletEntity w ON u.id = w.user.id
        WHERE u.treePath LIKE CONCAT(:userId, ',%') 
           OR u.treePath LIKE CONCAT('%,', :userId, ',%') 
           OR u.treePath LIKE CONCAT('%,', :userId)
           OR u.treePath = :userId
        AND w.status = com.eshop.app.enums.EntityStatusType.Active and w.transactionType = com.eshop.app.enums.TransactionType.DEPOSIT
    """)
	Long countActiveChildrenByUserId(@Param("userId") String userId);
}
