package com.eshop.app.service;

import com.eshop.app.filter.SubscriptionPackageDetailFilter;
import com.eshop.app.model.SubscriptionPackageDetailModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubscriptionPackageDetailService extends BaseService<SubscriptionPackageDetailFilter, SubscriptionPackageDetailModel, Long>{
    Page<SubscriptionPackageDetailModel> findBySubscriptionPackageId(long subscriptionPackageId, Pageable pageable);
}
