package com.eshop.app.mapping;

import com.eshop.app.entity.AnswerEntity;
import com.eshop.app.entity.CoinEntity;
import com.eshop.app.entity.QuestionEntity;
import com.eshop.app.model.AnswerModel;
import com.eshop.app.model.CoinModel;
import com.eshop.app.model.QuestionModel;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AnswerMapper extends BaseMapper<AnswerModel, AnswerEntity> {
    @Override
    @Mappings({
            @Mapping(target = "question", qualifiedByName = "questionEntityToQuestionModel")
    })
    AnswerModel toModel(final AnswerEntity entity);

    @Named("questionEntityToQuestionModel")
    @Mapping(target = "answers", ignore = true)
    QuestionModel questionEntityToQuestionModel(QuestionEntity entity);
}
