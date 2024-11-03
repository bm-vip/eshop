package com.eshop.app.model;

import com.eshop.app.enums.CurrencyType;
import com.eshop.app.enums.TransactionType;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

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
