package com.eshop.app.mapping;

import com.eshop.app.entity.CoinEntity;
import com.eshop.app.model.CoinModel;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CoinMapper extends BaseMapper<CoinModel, CoinEntity> {
}
