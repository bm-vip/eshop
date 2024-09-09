package com.eshop.client.service;

public interface OneTimePasswordService  extends LogicalDeletedService<Long>{
    String create(long userId);
    boolean verify(long userId, String password);
}
