package com.eshop.app.mapping;

import com.eshop.app.entity.SubscriptionPackageEntity;
import com.eshop.app.model.SubscriptionPackageModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-18T23:41:14+0330",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class SubscriptionPackageMapperImpl implements SubscriptionPackageMapper {

    @Override
    public SubscriptionPackageModel toModel(SubscriptionPackageEntity entity) {
        if ( entity == null ) {
            return null;
        }

        SubscriptionPackageModel subscriptionPackageModel = new SubscriptionPackageModel();

        if ( entity.getId() != null ) {
            subscriptionPackageModel.setId( entity.getId() );
        }
        if ( entity.getModifiedDate() != null ) {
            subscriptionPackageModel.setModifiedDate( entity.getModifiedDate() );
        }
        if ( entity.getCreatedDate() != null ) {
            subscriptionPackageModel.setCreatedDate( entity.getCreatedDate() );
        }
        subscriptionPackageModel.setVersion( entity.getVersion() );
        if ( entity.getSelectTitle() != null ) {
            subscriptionPackageModel.setSelectTitle( entity.getSelectTitle() );
        }
        if ( entity.getName() != null ) {
            subscriptionPackageModel.setName( entity.getName() );
        }
        if ( entity.getDuration() != null ) {
            subscriptionPackageModel.setDuration( entity.getDuration() );
        }
        if ( entity.getOrderCount() != null ) {
            subscriptionPackageModel.setOrderCount( entity.getOrderCount() );
        }
        if ( entity.getPrice() != null ) {
            subscriptionPackageModel.setPrice( entity.getPrice() );
        }
        if ( entity.getMaxPrice() != null ) {
            subscriptionPackageModel.setMaxPrice( entity.getMaxPrice() );
        }
        if ( entity.getCurrency() != null ) {
            subscriptionPackageModel.setCurrency( entity.getCurrency() );
        }
        if ( entity.getDescription() != null ) {
            subscriptionPackageModel.setDescription( entity.getDescription() );
        }
        if ( entity.getStatus() != null ) {
            subscriptionPackageModel.setStatus( entity.getStatus() );
        }
        if ( entity.getMinTradingReward() != null ) {
            subscriptionPackageModel.setMinTradingReward( entity.getMinTradingReward() );
        }
        if ( entity.getMaxTradingReward() != null ) {
            subscriptionPackageModel.setMaxTradingReward( entity.getMaxTradingReward() );
        }
        if ( entity.getSelfReferralBonus() != null ) {
            subscriptionPackageModel.setSelfReferralBonus( entity.getSelfReferralBonus() );
        }
        if ( entity.getParentReferralBonus() != null ) {
            subscriptionPackageModel.setParentReferralBonus( entity.getParentReferralBonus() );
        }

        return subscriptionPackageModel;
    }

    @Override
    public List<SubscriptionPackageModel> toModel(List<SubscriptionPackageEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<SubscriptionPackageModel> list = new ArrayList<SubscriptionPackageModel>( entities.size() );
        for ( SubscriptionPackageEntity subscriptionPackageEntity : entities ) {
            list.add( toModel( subscriptionPackageEntity ) );
        }

        return list;
    }

    @Override
    public SubscriptionPackageEntity toEntity(SubscriptionPackageModel model) {
        if ( model == null ) {
            return null;
        }

        SubscriptionPackageEntity subscriptionPackageEntity = new SubscriptionPackageEntity();

        if ( model.getCreatedDate() != null ) {
            subscriptionPackageEntity.setCreatedDate( model.getCreatedDate() );
        }
        if ( model.getModifiedDate() != null ) {
            subscriptionPackageEntity.setModifiedDate( model.getModifiedDate() );
        }
        subscriptionPackageEntity.setVersion( model.getVersion() );
        if ( model.getSelectTitle() != null ) {
            subscriptionPackageEntity.setSelectTitle( model.getSelectTitle() );
        }
        if ( model.getId() != null ) {
            subscriptionPackageEntity.setId( model.getId() );
        }
        if ( model.getName() != null ) {
            subscriptionPackageEntity.setName( model.getName() );
        }
        if ( model.getDuration() != null ) {
            subscriptionPackageEntity.setDuration( model.getDuration() );
        }
        if ( model.getOrderCount() != null ) {
            subscriptionPackageEntity.setOrderCount( model.getOrderCount() );
        }
        if ( model.getPrice() != null ) {
            subscriptionPackageEntity.setPrice( model.getPrice() );
        }
        if ( model.getMaxPrice() != null ) {
            subscriptionPackageEntity.setMaxPrice( model.getMaxPrice() );
        }
        if ( model.getCurrency() != null ) {
            subscriptionPackageEntity.setCurrency( model.getCurrency() );
        }
        if ( model.getDescription() != null ) {
            subscriptionPackageEntity.setDescription( model.getDescription() );
        }
        if ( model.getStatus() != null ) {
            subscriptionPackageEntity.setStatus( model.getStatus() );
        }
        if ( model.getMinTradingReward() != null ) {
            subscriptionPackageEntity.setMinTradingReward( model.getMinTradingReward() );
        }
        if ( model.getMaxTradingReward() != null ) {
            subscriptionPackageEntity.setMaxTradingReward( model.getMaxTradingReward() );
        }
        if ( model.getSelfReferralBonus() != null ) {
            subscriptionPackageEntity.setSelfReferralBonus( model.getSelfReferralBonus() );
        }
        if ( model.getParentReferralBonus() != null ) {
            subscriptionPackageEntity.setParentReferralBonus( model.getParentReferralBonus() );
        }

        return subscriptionPackageEntity;
    }

    @Override
    public List<SubscriptionPackageEntity> toEntity(List<SubscriptionPackageModel> models) {
        if ( models == null ) {
            return null;
        }

        List<SubscriptionPackageEntity> list = new ArrayList<SubscriptionPackageEntity>( models.size() );
        for ( SubscriptionPackageModel subscriptionPackageModel : models ) {
            list.add( toEntity( subscriptionPackageModel ) );
        }

        return list;
    }

    @Override
    public SubscriptionPackageEntity updateEntity(SubscriptionPackageModel model, SubscriptionPackageEntity entity) {
        if ( model == null ) {
            return null;
        }

        if ( model.getCreatedDate() != null ) {
            entity.setCreatedDate( model.getCreatedDate() );
        }
        else {
            entity.setCreatedDate( null );
        }
        if ( model.getModifiedDate() != null ) {
            entity.setModifiedDate( model.getModifiedDate() );
        }
        else {
            entity.setModifiedDate( null );
        }
        entity.setVersion( model.getVersion() );
        if ( model.getSelectTitle() != null ) {
            entity.setSelectTitle( model.getSelectTitle() );
        }
        else {
            entity.setSelectTitle( null );
        }
        if ( model.getId() != null ) {
            entity.setId( model.getId() );
        }
        else {
            entity.setId( null );
        }
        if ( model.getName() != null ) {
            entity.setName( model.getName() );
        }
        else {
            entity.setName( null );
        }
        if ( model.getDuration() != null ) {
            entity.setDuration( model.getDuration() );
        }
        else {
            entity.setDuration( null );
        }
        if ( model.getOrderCount() != null ) {
            entity.setOrderCount( model.getOrderCount() );
        }
        else {
            entity.setOrderCount( null );
        }
        if ( model.getPrice() != null ) {
            entity.setPrice( model.getPrice() );
        }
        else {
            entity.setPrice( null );
        }
        if ( model.getMaxPrice() != null ) {
            entity.setMaxPrice( model.getMaxPrice() );
        }
        else {
            entity.setMaxPrice( null );
        }
        if ( model.getCurrency() != null ) {
            entity.setCurrency( model.getCurrency() );
        }
        else {
            entity.setCurrency( null );
        }
        if ( model.getDescription() != null ) {
            entity.setDescription( model.getDescription() );
        }
        else {
            entity.setDescription( null );
        }
        if ( model.getStatus() != null ) {
            entity.setStatus( model.getStatus() );
        }
        else {
            entity.setStatus( null );
        }
        if ( model.getMinTradingReward() != null ) {
            entity.setMinTradingReward( model.getMinTradingReward() );
        }
        else {
            entity.setMinTradingReward( null );
        }
        if ( model.getMaxTradingReward() != null ) {
            entity.setMaxTradingReward( model.getMaxTradingReward() );
        }
        else {
            entity.setMaxTradingReward( null );
        }
        if ( model.getSelfReferralBonus() != null ) {
            entity.setSelfReferralBonus( model.getSelfReferralBonus() );
        }
        else {
            entity.setSelfReferralBonus( null );
        }
        if ( model.getParentReferralBonus() != null ) {
            entity.setParentReferralBonus( model.getParentReferralBonus() );
        }
        else {
            entity.setParentReferralBonus( null );
        }

        return entity;
    }

    @Override
    public List<SubscriptionPackageEntity> updateEntity(List<SubscriptionPackageModel> modelList, List<SubscriptionPackageEntity> entityList) {
        if ( modelList == null ) {
            return null;
        }

        entityList.clear();
        for ( SubscriptionPackageModel subscriptionPackageModel : modelList ) {
            entityList.add( toEntity( subscriptionPackageModel ) );
        }

        return entityList;
    }
}
