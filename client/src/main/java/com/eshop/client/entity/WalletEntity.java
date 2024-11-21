package com.eshop.client.entity;

import com.eshop.client.enums.CurrencyType;
import com.eshop.client.enums.TransactionType;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "tbl_wallet")
public class WalletEntity extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="seq_wallet", sequenceName="seq_wallet",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_wallet")
	private Long id;
	@NotNull
	private BigDecimal amount;
	@Enumerated(value = EnumType.STRING)
	private CurrencyType currency;
	@Column(name = "transaction_type", nullable = false)
	@Enumerated(value = EnumType.STRING)
	private TransactionType transactionType;
	@Column(name = "transaction_hash", unique = true)
	private String transactionHash;
	@Column(name = "address", nullable = false)
	private String address;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	private boolean active;

	@Override
	public String getSelectTitle() {
		if(transactionType == null) return null;
		StringBuilder builder = new StringBuilder();
		if (transactionType.equals(TransactionType.WITHDRAWAL))
			builder.append("-");
		else builder.append("+");
		return builder.append(amount).append(" ").append(currency).append(" ").append(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(modifiedDate)).toString();
	}
}
