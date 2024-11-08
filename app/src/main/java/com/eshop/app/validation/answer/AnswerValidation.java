package com.eshop.app.validation.answer;

import com.eshop.app.enums.AnswerType;
import com.eshop.app.model.AnswerModel;
import com.eshop.app.repository.AnswerRepository;
import com.eshop.app.repository.QuestionRepository;
import com.eshop.app.validation.Validation;
import com.eshop.exception.common.BadRequestException;
import com.eshop.exception.common.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.eshop.app.util.MapperHelper.get;

@Component
@RequiredArgsConstructor
public class AnswerValidation implements Validation<AnswerModel> {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    @Override
    public void validate(AnswerModel answerModel, Object... args) {
        AnswerType answerType = get(()->answerModel.getQuestion().getAnswerType());
        if(answerType == null) {
            var question = questionRepository.findById(answerModel.getQuestion().getId()).orElseThrow(() -> new NotFoundException("Question not found by id " + answerModel.getQuestion().getId()));
            answerType = question.getAnswerType();
        }

        if(!answerType.equals(AnswerType.MULTIPLE)) {
            Long answerId = 0L;
            if(answerModel.getId() != null)
                answerId = answerModel.getId();
            if(answerRepository.countAllByIdIsNotAndQuestionId(answerId, answerModel.getQuestion().getId()) > 0)
                throw new BadRequestException("This question can not have multiple answer");
        }
        if(answerType.equals(AnswerType.INTEGER) && !isInteger(answerModel.getTitle())){
            throw new BadRequestException("This question should have single Integer answer, please change the answer to integer format.");
        }
        if(answerType.equals(AnswerType.BOOLEAN) && !isBoolean(answerModel.getTitle())){
            throw new BadRequestException("This question should have single Integer answer, please change the answer to boolean format.");
        }

    }
    private boolean isInteger(String value){
        try {
            Integer.valueOf(value);
            return true;
        } catch (Exception e){
            return false;
        }
    }
    private boolean isBoolean(String value){
        if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("no"))
            return true;
        return false;
    }
}
