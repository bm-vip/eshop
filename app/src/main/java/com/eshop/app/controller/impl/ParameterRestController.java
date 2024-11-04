package com.eshop.app.controller.impl;

import com.eshop.app.controller.LogicalDeletedRestController;
import com.eshop.app.filter.ParameterFilter;
import com.eshop.app.model.ParameterModel;
import com.eshop.app.service.LogicalDeletedService;
import com.eshop.app.service.ParameterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Parameter Rest Service v1")
@RequestMapping(value = "/api/v1/parameter")
public class ParameterRestController extends BaseRestControllerImpl<ParameterFilter, ParameterModel, Long> implements LogicalDeletedRestController<Long> {

    private ParameterService parameterService;

    public ParameterRestController(ParameterService service) {
        super(service);
        this.parameterService = service;
    }
    @Override
    public LogicalDeletedService<Long> getService() {
        return parameterService;
    }
}
