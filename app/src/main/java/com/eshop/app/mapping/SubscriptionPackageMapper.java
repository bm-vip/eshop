package com.eshop.app.mapping;

import com.eshop.app.entity.SubscriptionPackageEntity;
import com.eshop.app.model.SubscriptionPackageModel;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface SubscriptionPackageMapper extends BaseMapper<SubscriptionPackageModel, SubscriptionPackageEntity> {

}
