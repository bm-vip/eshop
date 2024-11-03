package com.eshop.app.controller.impl;

import com.eshop.app.filter.QuestionFilter;
import com.eshop.app.model.QuestionModel;
import com.eshop.app.service.QuestionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Question Rest Service v1")
@RequestMapping(value = "/api/v1/question")
public class QuestionRestController extends BaseRestControllerImpl<QuestionFilter, QuestionModel, Long>  {

    private QuestionService questionService;

    public QuestionRestController(QuestionService service) {
        super(service, QuestionFilter.class);
        this.questionService = service;
    }
}
