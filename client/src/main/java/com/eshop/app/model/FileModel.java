package com.eshop.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class FileModel {
    private String name;
    private Date modifiedDate;
    public String getUrl(){
        return "api/v1/files/" + name;
    }
}
