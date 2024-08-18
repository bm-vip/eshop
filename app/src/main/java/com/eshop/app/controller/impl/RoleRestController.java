package com.eshop.app.controller.impl;

import com.eshop.app.filter.RoleFilter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.eshop.app.model.RoleModel;
import com.eshop.app.service.RoleService;


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
