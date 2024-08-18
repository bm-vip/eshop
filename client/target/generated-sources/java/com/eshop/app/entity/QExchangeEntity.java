package com.eshop.app.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QExchangeEntity is a Querydsl query type for ExchangeEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExchangeEntity extends EntityPathBase<ExchangeEntity> {

    private static final long serialVersionUID = -1483229788L;

    public static final QExchangeEntity exchangeEntity = new QExchangeEntity("exchangeEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath logo = createString("logo");

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    //inherited
    public final DateTimePath<java.util.Date> modifiedDate = _super.modifiedDate;

    public final StringPath name = createString("name");

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QExchangeEntity(String variable) {
        super(ExchangeEntity.class, forVariable(variable));
    }

    public QExchangeEntity(Path<? extends ExchangeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QExchangeEntity(PathMetadata metadata) {
        super(ExchangeEntity.class, metadata);
    }

}

