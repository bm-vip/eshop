package com.eshop.app.model;

import com.eshop.app.enums.AnswerType;
import com.eshop.app.enums.CurrencyType;
import com.eshop.app.enums.QuestionType;
import com.eshop.app.enums.TransactionType;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class QuestionModel extends BaseModel<Long> {
    @NotNull
    private String title;
    private Integer displayOrder;
    @NotNull
    private QuestionType type;
    @NotNull
    private AnswerType answerType;
    private UserModel user;
    private List<AnswerModel> answers = new ArrayList<>();;
    private boolean active;
}
