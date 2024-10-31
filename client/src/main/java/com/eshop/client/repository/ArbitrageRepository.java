package com.eshop.client.repository;

import com.eshop.client.entity.ArbitrageEntity;
import com.eshop.client.model.CoinUsageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ArbitrageRepository extends BaseRepository<ArbitrageEntity, Long> {
    long countAllByUserId(long userId);
    @Query("select a from ArbitrageEntity a where a.user.id=:userId and a.subscription.id=:subscriptionId and DATE(a.createdDate) =:createdDate order by a.createdDate desc")
    List<ArbitrageEntity> findByUserIdAndSubscriptionIdAndCreatedDateOrderByCreatedDateDesc(Long userId, Long subscriptionId, Date createdDate);

    @Query("SELECT NEW com.eshop.client.model.CoinUsageDTO(" +
            "a.coin.name, " +
            "COUNT(a.coin.name)) " +
            "FROM ArbitrageEntity a " +
            "GROUP BY a.coin.name " +
            "ORDER BY COUNT(a.coin.name) DESC")
    Page<CoinUsageDTO> findMostUsedCoins(Pageable pageable);
}
