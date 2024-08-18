package com.eshop.app.service.impl;

import com.eshop.app.entity.QRoleEntity;
import com.eshop.app.entity.RoleEntity;
import com.eshop.app.enums.RoleType;
import com.eshop.app.filter.RoleFilter;
import com.eshop.app.mapping.RoleMapper;
import com.eshop.app.model.RoleModel;
import com.eshop.app.repository.RoleRepository;
import com.eshop.app.service.RoleService;
import com.eshop.exception.common.NotFoundException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleFilter,RoleModel, RoleEntity, Long> implements RoleService {

    private final RoleRepository repository;

    public RoleServiceImpl(RoleRepository repository, RoleMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
    }

    @Override
    public Predicate queryBuilder(RoleFilter filter) {
        QRoleEntity path = QRoleEntity.roleEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if(!RoleType.hasRole(RoleType.ADMIN)) {
            builder.and(path.role.ne(RoleType.ADMIN));
        }

        filter.getId().ifPresent(value -> builder.and(path.id.eq(value)));
        filter.getRole().ifPresent(value -> builder.and(path.role.eq(value)));
        filter.getTitle().ifPresent(value -> builder.and(path.title.eq(value)));

        return builder;
    }

    @Override
    public RoleModel findByRole(String role) {
        return mapper.toModel(repository.findByRole(role).orElseThrow(() -> new NotFoundException("role: " + role)));
    }
}