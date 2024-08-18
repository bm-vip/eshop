package com.eshop.app.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QParameterGroupEntity is a Querydsl query type for ParameterGroupEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QParameterGroupEntity extends EntityPathBase<ParameterGroupEntity> {

    private static final long serialVersionUID = -169876713L;

    public static final QParameterGroupEntity parameterGroupEntity = new QParameterGroupEntity("parameterGroupEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath code = createString("code");

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdDate = _super.createdDate;

    public final BooleanPath deleted = createBoolean("deleted");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    //inherited
    public final DateTimePath<java.util.Date> modifiedDate = _super.modifiedDate;

    public final StringPath title = createString("title");

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QParameterGroupEntity(String variable) {
        super(ParameterGroupEntity.class, forVariable(variable));
    }

    public QParameterGroupEntity(Path<? extends ParameterGroupEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QParameterGroupEntity(PathMetadata metadata) {
        super(ParameterGroupEntity.class, metadata);
    }

}

