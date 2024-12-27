package com.eshop.app.service.impl;

import com.eshop.app.entity.RoleDetailEntity;
import com.eshop.app.entity.QRoleDetailEntity;
import com.eshop.app.enums.CurrencyType;
import com.eshop.app.enums.NetworkType;
import com.eshop.app.filter.RoleDetailFilter;
import com.eshop.app.mapping.RoleDetailMapper;
import com.eshop.app.model.RoleDetailModel;
import com.eshop.app.model.RoleModel;
import com.eshop.app.repository.RoleDetailRepository;
import com.eshop.app.service.RoleDetailService;
import com.eshop.exception.common.NotFoundException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RoleDetailServiceImpl extends BaseServiceImpl<RoleDetailFilter, RoleDetailModel, RoleDetailEntity, Long> implements RoleDetailService {

    private RoleDetailRepository roleDetailRepository;
    private RoleDetailMapper mapper;

    @Autowired
    public RoleDetailServiceImpl(RoleDetailRepository roleDetailRepository, RoleDetailMapper mapper) {
        super(roleDetailRepository, mapper);
        this.roleDetailRepository = roleDetailRepository;
        this.mapper = mapper;
    }

    @Override
    public Predicate queryBuilder(RoleDetailFilter filter) {
        QRoleDetailEntity p = QRoleDetailEntity.roleDetailEntity;
        BooleanBuilder builder = new BooleanBuilder();
        filter.getId().ifPresent(v->builder.and(p.id.eq(v)));
        filter.getNetwork().ifPresent(v->builder.and(p.network.eq(v)));
        filter.getCurrency().ifPresent(v->builder.and(p.currency.eq(v)));
        filter.getRoleId().ifPresent(v->builder.and(p.role.id.eq(v)));
        filter.getDescription().ifPresent(v->builder.and(p.description.contains(v)));

        return builder;
    }

    @Override
    public RoleDetailModel findByRoleNameAndNetworkAndCurrency(String role, NetworkType network, CurrencyType currency) {
        var entity = roleDetailRepository.findByRoleRoleAndNetworkAndCurrency(role,network,currency).orElseThrow(()->new NotFoundException("RoleDetail not found"));
        return mapper.toModel(entity);
    }
}
