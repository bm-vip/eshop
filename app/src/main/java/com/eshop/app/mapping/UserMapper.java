package com.eshop.app.mapping;

import com.eshop.app.entity.UserEntity;
import com.eshop.app.model.UserModel;
import org.mapstruct.*;

import javax.persistence.EntityNotFoundException;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {RoleMapper.class, CountryMapper.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper extends BaseMapper<UserModel, UserEntity> {

    @Override
    @Mappings({
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "parent", qualifiedByName = "mapParentSafely")
    })
    UserModel toModel(final UserEntity entity);

    @Override
    @Mappings({
            @Mapping(target = "password", ignore = true)
    })
    UserEntity toEntity(final UserModel model);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "password", ignore = true)
    })
    UserEntity updateEntity(UserModel model, @MappingTarget UserEntity entity);

    @Named("mapParentSafely")
    @Mappings({
            @Mapping(target = "parent", ignore = true)
    })
    default UserModel mapParentSafely(UserEntity parentEntity) {
        try {
            return parentEntity != null ? toModel(parentEntity) : null;
        } catch (Exception e) {
            return null;
        }
    }
}
