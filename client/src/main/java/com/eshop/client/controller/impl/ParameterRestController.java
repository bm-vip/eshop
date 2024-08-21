package com.eshop.client.controller.impl;

import com.eshop.client.controller.LogicalDeletedRestController;
import com.eshop.client.filter.ParameterFilter;
import com.eshop.client.model.ParameterModel;
import com.eshop.client.service.LogicalDeletedService;
import com.eshop.client.service.ParameterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Parameter Rest Service v1")
@RequestMapping(value = "/api/v1/parameter")
public class ParameterRestController extends BaseRestControllerImpl<ParameterFilter, ParameterModel, Long> implements LogicalDeletedRestController<Long> {

    private ParameterService parameterService;

    public ParameterRestController(ParameterService service) {
        super(service, ParameterFilter.class);
        this.parameterService = service;
    }
    @Override
    public LogicalDeletedService<Long> getService() {
        return parameterService;
    }
}
