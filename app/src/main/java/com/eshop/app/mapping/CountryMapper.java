package com.eshop.app.mapping;

import com.eshop.app.entity.CountryEntity;
import com.eshop.app.model.CountryModel;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CountryMapper extends BaseMapper<CountryModel, CountryEntity> {
}
