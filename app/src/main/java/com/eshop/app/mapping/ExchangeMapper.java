package com.eshop.app.mapping;

import com.eshop.app.entity.ExchangeEntity;
import com.eshop.app.model.ExchangeModel;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ExchangeMapper extends BaseMapper<ExchangeModel, ExchangeEntity> {
}
