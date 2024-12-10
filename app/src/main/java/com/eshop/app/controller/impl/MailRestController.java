package com.eshop.app.controller.impl;

import com.eshop.app.filter.ExchangeFilter;
import com.eshop.app.model.ExchangeModel;
import com.eshop.app.model.MailModel;
import com.eshop.app.model.NotificationModel;
import com.eshop.app.service.ExchangeService;
import com.eshop.app.service.MailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Mail Rest Service v1")
@RequestMapping(value = "/api/v1/mail")
public class MailRestController  {

    private MailService mailService;

    public MailRestController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping
    public ResponseEntity<Void> send(@RequestBody MailModel mailModel) {
        mailService.send(mailModel.getTo(),mailModel.getSubject(), mailModel.getBody());
        return ResponseEntity.noContent().build();
    }
}
