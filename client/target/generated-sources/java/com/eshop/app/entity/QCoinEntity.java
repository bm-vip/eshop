package com.eshop.app.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCoinEntity is a Querydsl query type for CoinEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoinEntity extends EntityPathBase<CoinEntity> {

    private static final long serialVersionUID = 1347607186L;

    public static final QCoinEntity coinEntity = new QCoinEntity("coinEntity");

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

    public QCoinEntity(String variable) {
        super(CoinEntity.class, forVariable(variable));
    }

    public QCoinEntity(Path<? extends CoinEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCoinEntity(PathMetadata metadata) {
        super(CoinEntity.class, metadata);
    }

}

