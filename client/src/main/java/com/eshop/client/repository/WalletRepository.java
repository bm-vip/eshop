package com.eshop.client.repository;

import com.eshop.client.entity.WalletEntity;
import com.eshop.client.model.BalanceModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends BaseRepository<WalletEntity, Long> {
	@Query("SELECT new com.eshop.client.model.BalanceModel(w.currency, "
			+ "SUM(CASE WHEN w.transactionType <> com.eshop.client.enums.TransactionType.WITHDRAWAL THEN w.amount ELSE 0 END) - "
			+ "SUM(CASE WHEN w.transactionType = com.eshop.client.enums.TransactionType.WITHDRAWAL THEN w.amount ELSE 0 END)) "
			+ "FROM WalletEntity w WHERE (:userId is null or w.user.id = :userId) AND w.active is true GROUP BY w.currency")
	 List<BalanceModel> findBalanceGroupedByCurrency(Long userId);

	@Query("SELECT new com.eshop.client.model.BalanceModel(w.currency, SUM(w.amount)) "
			+ "FROM WalletEntity w WHERE w.transactionType = com.eshop.client.enums.TransactionType.DEPOSIT "
			+ "AND (:userId is null or w.user.id = :userId) AND w.active is true "
			+ "GROUP BY w.currency")
	 List<BalanceModel> findDepositGroupedByCurrency(Long userId);
	@Query("SELECT new com.eshop.client.model.BalanceModel(w.currency, SUM(w.amount)) "
			+ "FROM WalletEntity w WHERE w.transactionType = com.eshop.client.enums.TransactionType.WITHDRAWAL "
			+ "AND (:userId is null or w.user.id = :userId) AND w.active is true "
			+ "GROUP BY w.currency")
	 List<BalanceModel> findWithdrawalGroupedByCurrency(Long userId);
	@Query("SELECT new com.eshop.client.model.BalanceModel(w.currency, SUM(w.amount)) "
			+ "FROM WalletEntity w WHERE w.transactionType = com.eshop.client.enums.TransactionType.BONUS "
			+ "AND (:userId is null or w.user.id = :userId) AND w.active is true "
			+ "GROUP BY w.currency")
	 List<BalanceModel> findBonusGroupedByCurrency(Long userId);

	@Query("SELECT new com.eshop.client.model.BalanceModel(w.currency, SUM(w.amount)) "
			+ "FROM WalletEntity w WHERE w.transactionType = com.eshop.client.enums.TransactionType.REWARD "
			+ "AND (:userId is null or w.user.id = :userId) AND w.active is true "
			+ "GROUP BY w.currency")
	List<BalanceModel> findRewardGroupedByCurrency(Long userId);
}
