package com.eshop.app.repository;

import com.eshop.app.entity.WalletEntity;
import com.eshop.app.model.BalanceModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends BaseRepository<WalletEntity, Long> {
	@Query("SELECT new com.eshop.app.model.BalanceModel(w.currency, "
			+ "SUM(CASE WHEN w.transactionType <> com.eshop.app.enums.TransactionType.WITHDRAWAL THEN w.amount ELSE 0 END) - "
			+ "SUM(CASE WHEN w.transactionType = com.eshop.app.enums.TransactionType.WITHDRAWAL THEN w.amount ELSE 0 END)) "
			+ "FROM WalletEntity w WHERE (:userId is null or w.user.id = :userId) AND w.active is true GROUP BY w.currency")
	 List<BalanceModel> findBalanceGroupedByCurrency(Long userId);

	@Query("SELECT new com.eshop.app.model.BalanceModel(w.currency, SUM(w.amount)) "
			+ "FROM WalletEntity w WHERE w.transactionType <> com.eshop.app.enums.TransactionType.WITHDRAWAL "
			+ "AND (:userId is null or w.user.id = :userId) AND w.active is true "
			+ "GROUP BY w.currency")
	 List<BalanceModel> findDepositGroupedByCurrency(Long userId);
	@Query("SELECT new com.eshop.app.model.BalanceModel(w.currency, SUM(w.amount)) "
			+ "FROM WalletEntity w WHERE w.transactionType = com.eshop.app.enums.TransactionType.WITHDRAWAL "
			+ "AND (:userId is null or w.user.id = :userId) AND w.active is true "
			+ "GROUP BY w.currency")
	 List<BalanceModel> findWithdrawalGroupedByCurrency(Long userId);
}
