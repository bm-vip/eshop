package com.eshop.client.controller.impl;

import com.eshop.client.filter.RoleFilter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.eshop.client.model.RoleModel;
import com.eshop.client.service.RoleService;


@RestController
@Tag(name = "Role Rest Service v1")
@RequestMapping(value = "/api/v1/role")
public class RoleRestController extends BaseRestControllerImpl<RoleFilter, RoleModel, Long> {

    private RoleService roleService;

    public RoleRestController(RoleService service) {
        super(service, RoleFilter.class);
        this.roleService = service;
    }
}
