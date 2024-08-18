package com.eshop.app.service;

import com.eshop.app.entity.ParameterGroupEntity;
import com.eshop.app.filter.ParameterGroupFilter;
import com.eshop.app.model.ParameterGroupModel;

public interface ParameterGroupService extends BaseService<ParameterGroupFilter, ParameterGroupModel, Long> , LogicalDeletedService<Long> {
    ParameterGroupEntity findByCode(String code);
}
