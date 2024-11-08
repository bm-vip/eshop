package com.eshop.app.mapping;

import com.eshop.app.entity.UserEntity;
import com.eshop.app.model.UserModel;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {RoleMapper.class, CountryMapper.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper extends BaseMapper<UserModel, UserEntity> {

    @Override
    @Mappings({
            @Mapping(target = "parent.roles", ignore = true)
    })
    UserModel toModel(final UserEntity entity);

}
