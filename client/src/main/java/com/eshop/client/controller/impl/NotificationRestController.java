package com.eshop.client.controller.impl;

import com.eshop.client.controller.LogicalDeletedRestController;
import com.eshop.client.filter.NotificationFilter;
import com.eshop.client.model.NotificationModel;
import com.eshop.client.service.LogicalDeletedService;
import com.eshop.client.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Tag(name = "Notification Rest Service v1")
@RequestMapping(value = "/api/v1/notification")
public class NotificationRestController extends BaseRestControllerImpl<NotificationFilter, NotificationModel, Long> implements LogicalDeletedRestController<Long> {

    private NotificationService notificationService;

    public NotificationRestController(NotificationService service) {
        super(service);
        this.notificationService = service;
    }
    @Override
    public LogicalDeletedService<Long> getService() {
        return notificationService;
    }

    @GetMapping("findAll-by-recipientId/{recipientId}")
    public ResponseEntity<Page<NotificationModel>> findAllByRecipientId(@PathVariable UUID recipientId, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(notificationService.findAllByRecipientId(recipientId,pageable));
    }
    @GetMapping("findAll-by-senderId/{senderId}")
    public ResponseEntity<Page<NotificationModel>> findAllBySenderId(@PathVariable UUID senderId, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(notificationService.findAllBySenderId(senderId,pageable));
    }
}
