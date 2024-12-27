package com.eshop.app.service;

import com.eshop.app.filter.ParameterFilter;
import com.eshop.app.model.ParameterModel;

import java.util.List;

public interface ParameterService extends BaseService<ParameterFilter, ParameterModel, Long> , LogicalDeletedService<Long>{
    ParameterModel findByCode(String code);
    List<ParameterModel> findAllByParameterGroupCode(String parameterGroupCode);
}
