package com.eshop.app.mapping;

import com.eshop.app.entity.SubscriptionEntity;
import com.eshop.app.entity.SubscriptionPackageEntity;
import com.eshop.app.entity.UserEntity;
import com.eshop.app.model.SubscriptionModel;
import com.eshop.app.model.UserModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-18T23:41:14+0330",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class SubscriptionMapperImpl implements SubscriptionMapper {

    @Autowired
    private SubscriptionPackageMapper subscriptionPackageMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<SubscriptionModel> toModel(List<SubscriptionEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<SubscriptionModel> list = new ArrayList<SubscriptionModel>( entities.size() );
        for ( SubscriptionEntity subscriptionEntity : entities ) {
            list.add( toModel( subscriptionEntity ) );
        }

        return list;
    }

    @Override
    public SubscriptionEntity toEntity(SubscriptionModel model) {
        if ( model == null ) {
            return null;
        }

        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();

        if ( model.getCreatedDate() != null ) {
            subscriptionEntity.setCreatedDate( model.getCreatedDate() );
        }
        if ( model.getModifiedDate() != null ) {
            subscriptionEntity.setModifiedDate( model.getModifiedDate() );
        }
        subscriptionEntity.setVersion( model.getVersion() );
        if ( model.getSelectTitle() != null ) {
            subscriptionEntity.setSelectTitle( model.getSelectTitle() );
        }
        if ( model.getId() != null ) {
            subscriptionEntity.setId( model.getId() );
        }
        if ( model.getUser() != null ) {
            subscriptionEntity.setUser( userMapper.toEntity( model.getUser() ) );
        }
        if ( model.getSubscriptionPackage() != null ) {
            subscriptionEntity.setSubscriptionPackage( subscriptionPackageMapper.toEntity( model.getSubscriptionPackage() ) );
        }
        if ( model.getExpireDate() != null ) {
            subscriptionEntity.setExpireDate( model.getExpireDate() );
        }
        if ( model.getDiscountPercentage() != null ) {
            subscriptionEntity.setDiscountPercentage( model.getDiscountPercentage() );
        }
        if ( model.getFinalPrice() != null ) {
            subscriptionEntity.setFinalPrice( model.getFinalPrice() );
        }
        if ( model.getStatus() != null ) {
            subscriptionEntity.setStatus( model.getStatus() );
        }

        return subscriptionEntity;
    }

    @Override
    public List<SubscriptionEntity> toEntity(List<SubscriptionModel> models) {
        if ( models == null ) {
            return null;
        }

        List<SubscriptionEntity> list = new ArrayList<SubscriptionEntity>( models.size() );
        for ( SubscriptionModel subscriptionModel : models ) {
            list.add( toEntity( subscriptionModel ) );
        }

        return list;
    }

    @Override
    public SubscriptionEntity updateEntity(SubscriptionModel model, SubscriptionEntity entity) {
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
        if ( model.getUser() != null ) {
            if ( entity.getUser() == null ) {
                entity.setUser( new UserEntity() );
            }
            userMapper.updateEntity( model.getUser(), entity.getUser() );
        }
        else {
            entity.setUser( null );
        }
        if ( model.getSubscriptionPackage() != null ) {
            if ( entity.getSubscriptionPackage() == null ) {
                entity.setSubscriptionPackage( new SubscriptionPackageEntity() );
            }
            subscriptionPackageMapper.updateEntity( model.getSubscriptionPackage(), entity.getSubscriptionPackage() );
        }
        else {
            entity.setSubscriptionPackage( null );
        }
        if ( model.getExpireDate() != null ) {
            entity.setExpireDate( model.getExpireDate() );
        }
        else {
            entity.setExpireDate( null );
        }
        if ( model.getDiscountPercentage() != null ) {
            entity.setDiscountPercentage( model.getDiscountPercentage() );
        }
        else {
            entity.setDiscountPercentage( null );
        }
        if ( model.getFinalPrice() != null ) {
            entity.setFinalPrice( model.getFinalPrice() );
        }
        else {
            entity.setFinalPrice( null );
        }
        if ( model.getStatus() != null ) {
            entity.setStatus( model.getStatus() );
        }
        else {
            entity.setStatus( null );
        }

        return entity;
    }

    @Override
    public List<SubscriptionEntity> updateEntity(List<SubscriptionModel> modelList, List<SubscriptionEntity> entityList) {
        if ( modelList == null ) {
            return null;
        }

        entityList.clear();
        for ( SubscriptionModel subscriptionModel : modelList ) {
            entityList.add( toEntity( subscriptionModel ) );
        }

        return entityList;
    }

    @Override
    public SubscriptionModel toModel(SubscriptionEntity entity) {
        if ( entity == null ) {
            return null;
        }

        SubscriptionModel subscriptionModel = new SubscriptionModel();

        if ( entity.getId() != null ) {
            subscriptionModel.setId( entity.getId() );
        }
        if ( entity.getModifiedDate() != null ) {
            subscriptionModel.setModifiedDate( entity.getModifiedDate() );
        }
        if ( entity.getCreatedDate() != null ) {
            subscriptionModel.setCreatedDate( entity.getCreatedDate() );
        }
        subscriptionModel.setVersion( entity.getVersion() );
        if ( entity.getSelectTitle() != null ) {
            subscriptionModel.setSelectTitle( entity.getSelectTitle() );
        }
        if ( entity.getUser() != null ) {
            subscriptionModel.setUser( userEntityToUserModel( entity.getUser() ) );
        }
        if ( entity.getSubscriptionPackage() != null ) {
            subscriptionModel.setSubscriptionPackage( subscriptionPackageMapper.toModel( entity.getSubscriptionPackage() ) );
        }
        if ( entity.getExpireDate() != null ) {
            subscriptionModel.setExpireDate( entity.getExpireDate() );
        }
        if ( entity.getDiscountPercentage() != null ) {
            subscriptionModel.setDiscountPercentage( entity.getDiscountPercentage() );
        }
        if ( entity.getFinalPrice() != null ) {
            subscriptionModel.setFinalPrice( entity.getFinalPrice() );
        }
        if ( entity.getStatus() != null ) {
            subscriptionModel.setStatus( entity.getStatus() );
        }

        return subscriptionModel;
    }

    protected UserModel userEntityToUserModel(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserModel userModel = new UserModel();

        if ( userEntity.getId() != null ) {
            userModel.setId( userEntity.getId() );
        }
        if ( userEntity.getModifiedDate() != null ) {
            userModel.setModifiedDate( userEntity.getModifiedDate() );
        }
        if ( userEntity.getCreatedDate() != null ) {
            userModel.setCreatedDate( userEntity.getCreatedDate() );
        }
        userModel.setVersion( userEntity.getVersion() );
        if ( userEntity.getSelectTitle() != null ) {
            userModel.setSelectTitle( userEntity.getSelectTitle() );
        }
        if ( userEntity.getUserName() != null ) {
            userModel.setUserName( userEntity.getUserName() );
        }
        if ( userEntity.getEmail() != null ) {
            userModel.setEmail( userEntity.getEmail() );
        }
        if ( userEntity.getPassword() != null ) {
            userModel.setPassword( userEntity.getPassword() );
        }
        if ( userEntity.getFirstName() != null ) {
            userModel.setFirstName( userEntity.getFirstName() );
        }
        if ( userEntity.getLastName() != null ) {
            userModel.setLastName( userEntity.getLastName() );
        }
        userModel.setActive( userEntity.isActive() );
        if ( userEntity.getUid() != null ) {
            userModel.setUid( userEntity.getUid() );
        }
        if ( userEntity.getTreePath() != null ) {
            userModel.setTreePath( userEntity.getTreePath() );
        }
        if ( userEntity.getWalletAddress() != null ) {
            userModel.setWalletAddress( userEntity.getWalletAddress() );
        }

        return userModel;
    }
}
