package com.eshop.app.controller.impl;

import com.eshop.app.filter.AnswerFilter;
import com.eshop.app.model.AnswerModel;
import com.eshop.app.service.AnswerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Answer Rest Service v1")
@RequestMapping(value = "/api/v1/answer")
public class AnswerRestController extends BaseRestControllerImpl<AnswerFilter, AnswerModel, Long>  {

    private AnswerService answerService;

    public AnswerRestController(AnswerService service) {
        super(service, AnswerFilter.class);
        this.answerService = service;
    }
}
