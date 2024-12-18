package com.eshop.client.repository;

import com.eshop.client.entity.WalletEntity;
import com.eshop.client.model.BalanceModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface WalletRepository extends BaseRepository<WalletEntity, Long> {
	@Query("SELECT new com.eshop.client.model.BalanceModel(w.currency, "
			+ "SUM(CASE WHEN w.transactionType = com.eshop.client.enums.TransactionType.DEPOSIT OR w.transactionType = com.eshop.client.enums.TransactionType.REWARD OR w.transactionType = com.eshop.client.enums.TransactionType.BONUS THEN w.amount ELSE 0 END) - "
			+ "SUM(CASE WHEN w.transactionType = com.eshop.client.enums.TransactionType.WITHDRAWAL OR w.transactionType = com.eshop.client.enums.TransactionType.WITHDRAWAL_PROFIT THEN w.amount ELSE 0 END)) "
			+ "FROM WalletEntity w WHERE w.user.id = :userId AND w.active is true GROUP BY w.currency")
	 List<BalanceModel> totalBalanceGroupedByCurrency(UUID userId);

	@Query("SELECT new com.eshop.client.model.BalanceModel(w.currency, SUM(w.amount)) "
			+ "FROM WalletEntity w WHERE w.transactionType = com.eshop.client.enums.TransactionType.DEPOSIT "
			+ "AND w.user.id = :userId AND w.active is true "
			+ "GROUP BY w.currency")
	 List<BalanceModel> totalDepositGroupedByCurrency(UUID userId);
	@Query("SELECT new com.eshop.client.model.BalanceModel(w.currency, SUM(w.amount)) "
			+ "FROM WalletEntity w WHERE (w.transactionType = com.eshop.client.enums.TransactionType.WITHDRAWAL OR w.transactionType = com.eshop.client.enums.TransactionType.WITHDRAWAL_PROFIT) "
			+ "AND w.user.id = :userId AND w.active is true "
			+ "GROUP BY w.currency")
	 List<BalanceModel> totalWithdrawalGroupedByCurrency(UUID userId);
	@Query("SELECT new com.eshop.client.model.BalanceModel(w.currency, SUM(w.amount)) "
			+ "FROM WalletEntity w WHERE w.transactionType = com.eshop.client.enums.TransactionType.BONUS "
			+ "AND w.user.id = :userId AND w.active is true "
			+ "GROUP BY w.currency")
	 List<BalanceModel> totalBonusGroupedByCurrency(UUID userId);
	@Query("SELECT new com.eshop.client.model.BalanceModel(w.currency, SUM(w.amount)) "
			+ "FROM WalletEntity w WHERE w.transactionType = com.eshop.client.enums.TransactionType.REWARD "
			+ "AND w.user.id = :userId AND w.active is true "
			+ "GROUP BY w.currency")
	 List<BalanceModel> totalRewardGroupedByCurrency(UUID userId);

	@Query("SELECT new com.eshop.client.model.BalanceModel(w.currency, "
			+ "SUM(CASE WHEN w.transactionType = com.eshop.client.enums.TransactionType.REWARD OR w.transactionType = com.eshop.client.enums.TransactionType.BONUS THEN w.amount ELSE 0 END) - "
			+ "SUM(CASE WHEN w.transactionType = com.eshop.client.enums.TransactionType.WITHDRAWAL_PROFIT THEN w.amount ELSE 0 END)) "
			+ "FROM WalletEntity w WHERE w.user.id = :userId AND w.active is true GROUP BY w.currency")
	 List<BalanceModel> totalProfitGroupedByCurrency(UUID userId);

//	@Query(value = "SELECT currency, SUM(totalAmount) AS totalAmount" +
//				   " FROM (" +
//				   " SELECT w.currency AS currency, SUM(w.amount) AS totalAmount" +
//				   " FROM tbl_wallet w" +
//				   " WHERE w.transaction_type = 'BONUS' AND w.user_id = :userId AND w.active = TRUE" +
//				   " GROUP BY w.currency" +
//				   " UNION ALL" +
//				   " SELECT a.currency AS currency, SUM(a.reward) AS totalAmount" +
//				   " FROM tbl_arbitrage a" +
//				   " WHERE a.user_id = :userId" +
//				   " GROUP BY a.currency" +
//				   " ) AS combined" +
//				   " GROUP BY currency;", nativeQuery = true)
//	List<Object[]> totalProfitGroupedByCurrency(UUID userId);
}
