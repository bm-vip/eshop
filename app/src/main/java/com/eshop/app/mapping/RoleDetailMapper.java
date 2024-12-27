package com.eshop.app.mapping;

import com.eshop.app.entity.RoleDetailEntity;
import com.eshop.app.model.RoleDetailModel;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RoleDetailMapper extends BaseMapper<RoleDetailModel, RoleDetailEntity> {
}
