package com.eshop.app.mapping;


import com.eshop.app.entity.SubscriptionPackageDetailEntity;
import com.eshop.app.model.SubscriptionPackageDetailModel;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {SubscriptionPackageMapper.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface SubscriptionPackageDetailMapper extends BaseMapper<SubscriptionPackageDetailModel, SubscriptionPackageDetailEntity> {

}
