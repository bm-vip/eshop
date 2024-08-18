package com.eshop.app.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWalletEntity is a Querydsl query type for WalletEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWalletEntity extends EntityPathBase<WalletEntity> {

    private static final long serialVersionUID = -1705600390L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWalletEntity walletEntity = new QWalletEntity("walletEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final BooleanPath active = createBoolean("active");

    public final StringPath address = createString("address");

    public final NumberPath<java.math.BigDecimal> amount = createNumber("amount", java.math.BigDecimal.class);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdDate = _super.createdDate;

    public final EnumPath<com.eshop.app.enums.CurrencyType> currency = createEnum("currency", com.eshop.app.enums.CurrencyType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    //inherited
    public final DateTimePath<java.util.Date> modifiedDate = _super.modifiedDate;

    public final StringPath transactionHash = createString("transactionHash");

    public final EnumPath<com.eshop.app.enums.TransactionType> transactionType = createEnum("transactionType", com.eshop.app.enums.TransactionType.class);

    public final QUserEntity user;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QWalletEntity(String variable) {
        this(WalletEntity.class, forVariable(variable), INITS);
    }

    public QWalletEntity(Path<? extends WalletEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWalletEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWalletEntity(PathMetadata metadata, PathInits inits) {
        this(WalletEntity.class, metadata, inits);
    }

    public QWalletEntity(Class<? extends WalletEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUserEntity(forProperty("user"), inits.get("user")) : null;
    }

}

