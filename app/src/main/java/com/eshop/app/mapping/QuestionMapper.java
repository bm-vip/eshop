package com.eshop.app.mapping;

import com.eshop.app.entity.AnswerEntity;
import com.eshop.app.entity.QuestionEntity;
import com.eshop.app.entity.UserEntity;
import com.eshop.app.model.AnswerModel;
import com.eshop.app.model.QuestionModel;
import com.eshop.app.model.UserModel;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface QuestionMapper extends BaseMapper<QuestionModel, QuestionEntity> {
    @Override
    @Mappings({
            @Mapping(target = "answers", qualifiedByName = "answerEntityToAnswerModel")
    })
    QuestionModel toModel(final QuestionEntity entity);

    @Named("answerEntityToAnswerModel")
    @Mapping(target = "question", ignore = true)
    AnswerModel answerEntityToAnswerModel(AnswerEntity entity);
}
