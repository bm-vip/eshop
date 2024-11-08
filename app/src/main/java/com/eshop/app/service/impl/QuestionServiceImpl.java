package com.eshop.app.service.impl;

import com.eshop.app.entity.QuestionEntity;
import com.eshop.app.entity.QQuestionEntity;
import com.eshop.app.filter.QuestionFilter;
import com.eshop.app.mapping.AnswerMapper;
import com.eshop.app.mapping.QuestionMapper;
import com.eshop.app.model.AnswerModel;
import com.eshop.app.model.QuestionModel;
import com.eshop.app.repository.AnswerRepository;
import com.eshop.app.repository.QuestionRepository;
import com.eshop.app.service.QuestionService;
import com.eshop.app.validation.Validation;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class QuestionServiceImpl extends BaseServiceImpl<QuestionFilter, QuestionModel, QuestionEntity, Long> implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final AnswerMapper answerMapper;
    private final AnswerRepository answerRepository;
    private final List<Validation<AnswerModel>> validations;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository, QuestionMapper questionMapper, AnswerMapper answerMapper, AnswerRepository answerRepository, List<Validation<AnswerModel>> validations) {
        super(questionRepository, questionMapper);
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
        this.answerMapper = answerMapper;
        this.answerRepository = answerRepository;
        this.validations = validations;
    }

    @Override
    public Predicate queryBuilder(QuestionFilter filter) {
        QQuestionEntity p = QQuestionEntity.questionEntity;
        BooleanBuilder builder = new BooleanBuilder();
        filter.getId().ifPresent(v->builder.and(p.id.eq(v)));
        filter.getTitle().ifPresent(v->builder.and(p.title.toLowerCase().contains(v.toLowerCase())));
        filter.getAnswerType().ifPresent(v->builder.and(p.answerType.eq(v)));
        filter.getType().ifPresent(v->builder.and(p.type.eq(v)));
        filter.getActive().ifPresent(v->builder.and(p.active.eq(v)));
        filter.getUserId().ifPresent(v->builder.and(p.user.id.eq(v)));
        filter.getAnswerId().ifPresent(v->builder.and(p.answers.any().id.eq(v)));

        return builder;
    }

    @Override
    @Transactional
    public QuestionModel update(QuestionModel model) {
        var updatedQuestion = super.update(model);
        var answers = answerRepository.findAllByQuestionId(model.getId());
        answers.forEach(answerEntity-> validations.forEach(v->v.validate(answerMapper.toModel(answerEntity))));
        return updatedQuestion;
    }
}
