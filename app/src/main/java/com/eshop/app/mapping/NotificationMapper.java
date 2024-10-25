package com.eshop.app.mapping;


import com.eshop.app.entity.NotificationEntity;
import com.eshop.app.model.NotificationModel;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ParameterGroupMapper.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface NotificationMapper extends BaseMapper<NotificationModel, NotificationEntity> {

}
