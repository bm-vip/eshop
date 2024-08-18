package com.eshop.app.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSubscriptionPackageEntity is a Querydsl query type for SubscriptionPackageEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubscriptionPackageEntity extends EntityPathBase<SubscriptionPackageEntity> {

    private static final long serialVersionUID = 1695769902L;

    public static final QSubscriptionPackageEntity subscriptionPackageEntity = new QSubscriptionPackageEntity("subscriptionPackageEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdDate = _super.createdDate;

    public final EnumPath<com.eshop.app.enums.CurrencyType> currency = createEnum("currency", com.eshop.app.enums.CurrencyType.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> duration = createNumber("duration", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<java.math.BigDecimal> maxPrice = createNumber("maxPrice", java.math.BigDecimal.class);

    public final NumberPath<Float> maxTradingReward = createNumber("maxTradingReward", Float.class);

    public final NumberPath<Float> minTradingReward = createNumber("minTradingReward", Float.class);

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    //inherited
    public final DateTimePath<java.util.Date> modifiedDate = _super.modifiedDate;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> orderCount = createNumber("orderCount", Integer.class);

    public final NumberPath<Float> parentReferralBonus = createNumber("parentReferralBonus", Float.class);

    public final NumberPath<java.math.BigDecimal> price = createNumber("price", java.math.BigDecimal.class);

    public final NumberPath<Float> selfReferralBonus = createNumber("selfReferralBonus", Float.class);

    public final EnumPath<com.eshop.app.enums.EntityStatusType> status = createEnum("status", com.eshop.app.enums.EntityStatusType.class);

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QSubscriptionPackageEntity(String variable) {
        super(SubscriptionPackageEntity.class, forVariable(variable));
    }

    public QSubscriptionPackageEntity(Path<? extends SubscriptionPackageEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSubscriptionPackageEntity(PathMetadata metadata) {
        super(SubscriptionPackageEntity.class, metadata);
    }

}

