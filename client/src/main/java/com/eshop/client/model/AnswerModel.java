package com.eshop.client.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class AnswerModel extends BaseModel<Long> {
    @NotNull
    private QuestionModel question;
    @NotNull
    private String title;
    private Integer displayOrder;
    @NotNull
    private boolean active;

}
