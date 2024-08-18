package com.eshop.app.mapping;

import com.eshop.app.entity.ParameterGroupEntity;
import com.eshop.app.model.ParameterGroupModel;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ParameterGroupMapper extends BaseMapper<ParameterGroupModel, ParameterGroupEntity> {
}
