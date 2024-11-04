package com.eshop.app.controller.impl;

import com.eshop.app.controller.LogicalDeletedRestController;
import com.eshop.app.filter.ParameterGroupFilter;
import com.eshop.app.model.ParameterGroupModel;
import com.eshop.app.service.LogicalDeletedService;
import com.eshop.app.service.ParameterGroupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "ParameterGroup Rest Service v1")
@RequestMapping(value = "/api/v1/parameterGroup")
public class ParameterGroupRestController extends BaseRestControllerImpl<ParameterGroupFilter, ParameterGroupModel, Long> implements LogicalDeletedRestController<Long> {

    private ParameterGroupService parameterGroupService;

    public ParameterGroupRestController(ParameterGroupService service) {
        super(service);
        this.parameterGroupService = service;
    }
    @Override
    public LogicalDeletedService<Long> getService() {
        return parameterGroupService;
    }
}
