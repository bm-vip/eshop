package com.eshop.client.mapping;


import com.eshop.client.entity.SubscriptionPackageDetailEntity;
import com.eshop.client.model.SubscriptionPackageDetailModel;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {SubscriptionPackageMapper.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface SubscriptionPackageDetailMapper extends BaseMapper<SubscriptionPackageDetailModel, SubscriptionPackageDetailEntity> {

}
