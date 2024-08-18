package com.eshop.app.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSubscriptionEntity is a Querydsl query type for SubscriptionEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubscriptionEntity extends EntityPathBase<SubscriptionEntity> {

    private static final long serialVersionUID = 1681509918L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSubscriptionEntity subscriptionEntity = new QSubscriptionEntity("subscriptionEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdDate = _super.createdDate;

    public final BooleanPath deleted = createBoolean("deleted");

    public final NumberPath<Integer> discountPercentage = createNumber("discountPercentage", Integer.class);

    public final DateTimePath<java.util.Date> expireDate = createDateTime("expireDate", java.util.Date.class);

    public final NumberPath<java.math.BigDecimal> finalPrice = createNumber("finalPrice", java.math.BigDecimal.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    //inherited
    public final DateTimePath<java.util.Date> modifiedDate = _super.modifiedDate;

    public final EnumPath<com.eshop.app.enums.EntityStatusType> status = createEnum("status", com.eshop.app.enums.EntityStatusType.class);

    public final QSubscriptionPackageEntity subscriptionPackage;

    public final QUserEntity user;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QSubscriptionEntity(String variable) {
        this(SubscriptionEntity.class, forVariable(variable), INITS);
    }

    public QSubscriptionEntity(Path<? extends SubscriptionEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSubscriptionEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSubscriptionEntity(PathMetadata metadata, PathInits inits) {
        this(SubscriptionEntity.class, metadata, inits);
    }

    public QSubscriptionEntity(Class<? extends SubscriptionEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.subscriptionPackage = inits.isInitialized("subscriptionPackage") ? new QSubscriptionPackageEntity(forProperty("subscriptionPackage")) : null;
        this.user = inits.isInitialized("user") ? new QUserEntity(forProperty("user"), inits.get("user")) : null;
    }

}

