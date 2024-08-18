package com.eshop.app.mapping;


import com.eshop.app.entity.ParameterEntity;
import com.eshop.app.model.ParameterModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ParameterGroupMapper.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ParameterMapper extends BaseMapper<ParameterModel, ParameterEntity> {

}
