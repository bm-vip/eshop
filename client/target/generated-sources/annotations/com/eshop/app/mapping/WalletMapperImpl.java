package com.eshop.app.mapping;

import com.eshop.app.entity.RoleEntity;
import com.eshop.app.entity.UserEntity;
import com.eshop.app.entity.WalletEntity;
import com.eshop.app.model.RoleModel;
import com.eshop.app.model.UserModel;
import com.eshop.app.model.WalletModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-18T23:41:13+0330",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class WalletMapperImpl implements WalletMapper {

    @Override
    public List<WalletModel> toModel(List<WalletEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<WalletModel> list = new ArrayList<WalletModel>( entities.size() );
        for ( WalletEntity walletEntity : entities ) {
            list.add( toModel( walletEntity ) );
        }

        return list;
    }

    @Override
    public WalletEntity toEntity(WalletModel model) {
        if ( model == null ) {
            return null;
        }

        WalletEntity walletEntity = new WalletEntity();

        if ( model.getCreatedDate() != null ) {
            walletEntity.setCreatedDate( model.getCreatedDate() );
        }
        if ( model.getModifiedDate() != null ) {
            walletEntity.setModifiedDate( model.getModifiedDate() );
        }
        walletEntity.setVersion( model.getVersion() );
        if ( model.getSelectTitle() != null ) {
            walletEntity.setSelectTitle( model.getSelectTitle() );
        }
        if ( model.getId() != null ) {
            walletEntity.setId( model.getId() );
        }
        if ( model.getAmount() != null ) {
            walletEntity.setAmount( model.getAmount() );
        }
        if ( model.getCurrency() != null ) {
            walletEntity.setCurrency( model.getCurrency() );
        }
        if ( model.getTransactionType() != null ) {
            walletEntity.setTransactionType( model.getTransactionType() );
        }
        if ( model.getTransactionHash() != null ) {
            walletEntity.setTransactionHash( model.getTransactionHash() );
        }
        if ( model.getAddress() != null ) {
            walletEntity.setAddress( model.getAddress() );
        }
        if ( model.getUser() != null ) {
            walletEntity.setUser( userModelToUserEntity( model.getUser() ) );
        }
        walletEntity.setActive( model.isActive() );

        return walletEntity;
    }

    @Override
    public List<WalletEntity> toEntity(List<WalletModel> models) {
        if ( models == null ) {
            return null;
        }

        List<WalletEntity> list = new ArrayList<WalletEntity>( models.size() );
        for ( WalletModel walletModel : models ) {
            list.add( toEntity( walletModel ) );
        }

        return list;
    }

    @Override
    public WalletEntity updateEntity(WalletModel model, WalletEntity entity) {
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
        if ( model.getAmount() != null ) {
            entity.setAmount( model.getAmount() );
        }
        else {
            entity.setAmount( null );
        }
        if ( model.getCurrency() != null ) {
            entity.setCurrency( model.getCurrency() );
        }
        else {
            entity.setCurrency( null );
        }
        if ( model.getTransactionType() != null ) {
            entity.setTransactionType( model.getTransactionType() );
        }
        else {
            entity.setTransactionType( null );
        }
        if ( model.getTransactionHash() != null ) {
            entity.setTransactionHash( model.getTransactionHash() );
        }
        else {
            entity.setTransactionHash( null );
        }
        if ( model.getAddress() != null ) {
            entity.setAddress( model.getAddress() );
        }
        else {
            entity.setAddress( null );
        }
        if ( model.getUser() != null ) {
            if ( entity.getUser() == null ) {
                entity.setUser( new UserEntity() );
            }
            userModelToUserEntity1( model.getUser(), entity.getUser() );
        }
        else {
            entity.setUser( null );
        }
        entity.setActive( model.isActive() );

        return entity;
    }

    @Override
    public List<WalletEntity> updateEntity(List<WalletModel> modelList, List<WalletEntity> entityList) {
        if ( modelList == null ) {
            return null;
        }

        entityList.clear();
        for ( WalletModel walletModel : modelList ) {
            entityList.add( toEntity( walletModel ) );
        }

        return entityList;
    }

    @Override
    public WalletModel toModel(WalletEntity entity) {
        if ( entity == null ) {
            return null;
        }

        WalletModel walletModel = new WalletModel();

        if ( entity.getId() != null ) {
            walletModel.setId( entity.getId() );
        }
        if ( entity.getModifiedDate() != null ) {
            walletModel.setModifiedDate( entity.getModifiedDate() );
        }
        if ( entity.getCreatedDate() != null ) {
            walletModel.setCreatedDate( entity.getCreatedDate() );
        }
        walletModel.setVersion( entity.getVersion() );
        if ( entity.getSelectTitle() != null ) {
            walletModel.setSelectTitle( entity.getSelectTitle() );
        }
        if ( entity.getAmount() != null ) {
            walletModel.setAmount( entity.getAmount() );
        }
        if ( entity.getTransactionType() != null ) {
            walletModel.setTransactionType( entity.getTransactionType() );
        }
        if ( entity.getTransactionHash() != null ) {
            walletModel.setTransactionHash( entity.getTransactionHash() );
        }
        if ( entity.getCurrency() != null ) {
            walletModel.setCurrency( entity.getCurrency() );
        }
        if ( entity.getUser() != null ) {
            walletModel.setUser( userEntityToUserModel( entity.getUser() ) );
        }
        if ( entity.getAddress() != null ) {
            walletModel.setAddress( entity.getAddress() );
        }
        walletModel.setActive( entity.isActive() );

        return walletModel;
    }

    protected RoleEntity roleModelToRoleEntity(RoleModel roleModel) {
        if ( roleModel == null ) {
            return null;
        }

        RoleEntity roleEntity = new RoleEntity();

        if ( roleModel.getCreatedDate() != null ) {
            roleEntity.setCreatedDate( roleModel.getCreatedDate() );
        }
        if ( roleModel.getModifiedDate() != null ) {
            roleEntity.setModifiedDate( roleModel.getModifiedDate() );
        }
        roleEntity.setVersion( roleModel.getVersion() );
        if ( roleModel.getSelectTitle() != null ) {
            roleEntity.setSelectTitle( roleModel.getSelectTitle() );
        }
        if ( roleModel.getId() != null ) {
            roleEntity.setId( roleModel.getId() );
        }
        if ( roleModel.getRole() != null ) {
            roleEntity.setRole( roleModel.getRole() );
        }
        if ( roleModel.getTitle() != null ) {
            roleEntity.setTitle( roleModel.getTitle() );
        }

        return roleEntity;
    }

    protected Set<RoleEntity> roleModelSetToRoleEntitySet(Set<RoleModel> set) {
        if ( set == null ) {
            return null;
        }

        Set<RoleEntity> set1 = new HashSet<RoleEntity>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( RoleModel roleModel : set ) {
            set1.add( roleModelToRoleEntity( roleModel ) );
        }

        return set1;
    }

    protected UserEntity userModelToUserEntity(UserModel userModel) {
        if ( userModel == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        if ( userModel.getCreatedDate() != null ) {
            userEntity.setCreatedDate( userModel.getCreatedDate() );
        }
        if ( userModel.getModifiedDate() != null ) {
            userEntity.setModifiedDate( userModel.getModifiedDate() );
        }
        userEntity.setVersion( userModel.getVersion() );
        if ( userModel.getSelectTitle() != null ) {
            userEntity.setSelectTitle( userModel.getSelectTitle() );
        }
        if ( userModel.getId() != null ) {
            userEntity.setId( userModel.getId() );
        }
        if ( userModel.getUserName() != null ) {
            userEntity.setUserName( userModel.getUserName() );
        }
        if ( userModel.getEmail() != null ) {
            userEntity.setEmail( userModel.getEmail() );
        }
        if ( userModel.getPassword() != null ) {
            userEntity.setPassword( userModel.getPassword() );
        }
        if ( userModel.getFirstName() != null ) {
            userEntity.setFirstName( userModel.getFirstName() );
        }
        if ( userModel.getLastName() != null ) {
            userEntity.setLastName( userModel.getLastName() );
        }
        if ( userModel.getUid() != null ) {
            userEntity.setUid( userModel.getUid() );
        }
        if ( userModel.getActive() != null ) {
            userEntity.setActive( userModel.getActive() );
        }
        if ( userModel.getParent() != null ) {
            userEntity.setParent( userModelToUserEntity( userModel.getParent() ) );
        }
        if ( userModel.getTreePath() != null ) {
            userEntity.setTreePath( userModel.getTreePath() );
        }
        if ( userModel.getWalletAddress() != null ) {
            userEntity.setWalletAddress( userModel.getWalletAddress() );
        }
        Set<RoleEntity> set = roleModelSetToRoleEntitySet( userModel.getRoles() );
        if ( set != null ) {
            userEntity.setRoles( set );
        }

        return userEntity;
    }

    protected void userModelToUserEntity1(UserModel userModel, UserEntity mappingTarget) {
        if ( userModel == null ) {
            return;
        }

        if ( userModel.getCreatedDate() != null ) {
            mappingTarget.setCreatedDate( userModel.getCreatedDate() );
        }
        else {
            mappingTarget.setCreatedDate( null );
        }
        if ( userModel.getModifiedDate() != null ) {
            mappingTarget.setModifiedDate( userModel.getModifiedDate() );
        }
        else {
            mappingTarget.setModifiedDate( null );
        }
        mappingTarget.setVersion( userModel.getVersion() );
        if ( userModel.getSelectTitle() != null ) {
            mappingTarget.setSelectTitle( userModel.getSelectTitle() );
        }
        else {
            mappingTarget.setSelectTitle( null );
        }
        if ( userModel.getId() != null ) {
            mappingTarget.setId( userModel.getId() );
        }
        else {
            mappingTarget.setId( null );
        }
        if ( userModel.getUserName() != null ) {
            mappingTarget.setUserName( userModel.getUserName() );
        }
        else {
            mappingTarget.setUserName( null );
        }
        if ( userModel.getEmail() != null ) {
            mappingTarget.setEmail( userModel.getEmail() );
        }
        else {
            mappingTarget.setEmail( null );
        }
        if ( userModel.getPassword() != null ) {
            mappingTarget.setPassword( userModel.getPassword() );
        }
        else {
            mappingTarget.setPassword( null );
        }
        if ( userModel.getFirstName() != null ) {
            mappingTarget.setFirstName( userModel.getFirstName() );
        }
        else {
            mappingTarget.setFirstName( null );
        }
        if ( userModel.getLastName() != null ) {
            mappingTarget.setLastName( userModel.getLastName() );
        }
        else {
            mappingTarget.setLastName( null );
        }
        if ( userModel.getUid() != null ) {
            mappingTarget.setUid( userModel.getUid() );
        }
        else {
            mappingTarget.setUid( null );
        }
        if ( userModel.getActive() != null ) {
            mappingTarget.setActive( userModel.getActive() );
        }
        if ( userModel.getParent() != null ) {
            if ( mappingTarget.getParent() == null ) {
                mappingTarget.setParent( new UserEntity() );
            }
            userModelToUserEntity1( userModel.getParent(), mappingTarget.getParent() );
        }
        else {
            mappingTarget.setParent( null );
        }
        if ( userModel.getTreePath() != null ) {
            mappingTarget.setTreePath( userModel.getTreePath() );
        }
        else {
            mappingTarget.setTreePath( null );
        }
        if ( userModel.getWalletAddress() != null ) {
            mappingTarget.setWalletAddress( userModel.getWalletAddress() );
        }
        else {
            mappingTarget.setWalletAddress( null );
        }
        if ( mappingTarget.getRoles() != null ) {
            Set<RoleEntity> set = roleModelSetToRoleEntitySet( userModel.getRoles() );
            if ( set != null ) {
                mappingTarget.getRoles().clear();
                mappingTarget.getRoles().addAll( set );
            }
        }
        else {
            Set<RoleEntity> set = roleModelSetToRoleEntitySet( userModel.getRoles() );
            if ( set != null ) {
                mappingTarget.setRoles( set );
            }
        }
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
