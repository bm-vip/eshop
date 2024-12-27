package com.eshop.app.controller.impl;

import com.eshop.app.filter.RoleDetailFilter;
import com.eshop.app.model.RoleDetailModel;
import com.eshop.app.service.RoleDetailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "Role Rest Service v1")
@RequestMapping(value = "/api/v1/role-detail")
public class RoleDetailRestController extends BaseRestControllerImpl<RoleDetailFilter, RoleDetailModel, Long> {

    private RoleDetailService roleDetailService;

    public RoleDetailRestController(RoleDetailService service) {
        super(service);
        this.roleDetailService = service;
    }
}
