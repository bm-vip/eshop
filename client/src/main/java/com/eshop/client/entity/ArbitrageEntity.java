package com.eshop.client.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_arbitrage")
public class ArbitrageEntity extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="seq_arbitrage", sequenceName="seq_arbitrage",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_arbitrage")
	private Long id;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;
	@ManyToOne
	@JoinColumn(name = "exchange_id")
	private ExchangeEntity exchange;
	@ManyToOne
	@JoinColumn(name = "coin_id")
	private CoinEntity coin;
	@ManyToOne
	@JoinColumn(name = "subscription_id")
	private SubscriptionEntity subscription;

	@Override
	public String getSelectTitle() {
		return user.getSelectTitle().concat(" ").concat(coin.getName()).concat(" ").concat(exchange.getName());
	}
}
