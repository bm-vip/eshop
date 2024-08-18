package com.eshop.app.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QParameterEntity is a Querydsl query type for ParameterEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QParameterEntity extends EntityPathBase<ParameterEntity> {

    private static final long serialVersionUID = -1837081874L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QParameterEntity parameterEntity = new QParameterEntity("parameterEntity");

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

    public final QParameterGroupEntity parameterGroup;

    public final StringPath title = createString("title");

    public final StringPath value = createString("value");

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QParameterEntity(String variable) {
        this(ParameterEntity.class, forVariable(variable), INITS);
    }

    public QParameterEntity(Path<? extends ParameterEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QParameterEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QParameterEntity(PathMetadata metadata, PathInits inits) {
        this(ParameterEntity.class, metadata, inits);
    }

    public QParameterEntity(Class<? extends ParameterEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parameterGroup = inits.isInitialized("parameterGroup") ? new QParameterGroupEntity(forProperty("parameterGroup")) : null;
    }

}

