package com.eshop.app.mapping;

import com.eshop.app.entity.RoleEntity;
import com.eshop.app.entity.UserEntity;
import com.eshop.app.model.RoleModel;
import com.eshop.app.model.UserModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-18T23:41:13+0330",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<UserModel> toModel(List<UserEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<UserModel> list = new ArrayList<UserModel>( entities.size() );
        for ( UserEntity userEntity : entities ) {
            list.add( toModel( userEntity ) );
        }

        return list;
    }

    @Override
    public UserEntity toEntity(UserModel model) {
        if ( model == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        if ( model.getCreatedDate() != null ) {
            userEntity.setCreatedDate( model.getCreatedDate() );
        }
        if ( model.getModifiedDate() != null ) {
            userEntity.setModifiedDate( model.getModifiedDate() );
        }
        userEntity.setVersion( model.getVersion() );
        if ( model.getSelectTitle() != null ) {
            userEntity.setSelectTitle( model.getSelectTitle() );
        }
        if ( model.getId() != null ) {
            userEntity.setId( model.getId() );
        }
        if ( model.getUserName() != null ) {
            userEntity.setUserName( model.getUserName() );
        }
        if ( model.getEmail() != null ) {
            userEntity.setEmail( model.getEmail() );
        }
        if ( model.getPassword() != null ) {
            userEntity.setPassword( model.getPassword() );
        }
        if ( model.getFirstName() != null ) {
            userEntity.setFirstName( model.getFirstName() );
        }
        if ( model.getLastName() != null ) {
            userEntity.setLastName( model.getLastName() );
        }
        if ( model.getUid() != null ) {
            userEntity.setUid( model.getUid() );
        }
        if ( model.getActive() != null ) {
            userEntity.setActive( model.getActive() );
        }
        if ( model.getParent() != null ) {
            userEntity.setParent( toEntity( model.getParent() ) );
        }
        if ( model.getTreePath() != null ) {
            userEntity.setTreePath( model.getTreePath() );
        }
        if ( model.getWalletAddress() != null ) {
            userEntity.setWalletAddress( model.getWalletAddress() );
        }
        Set<RoleEntity> set = roleModelSetToRoleEntitySet( model.getRoles() );
        if ( set != null ) {
            userEntity.setRoles( set );
        }

        return userEntity;
    }

    @Override
    public List<UserEntity> toEntity(List<UserModel> models) {
        if ( models == null ) {
            return null;
        }

        List<UserEntity> list = new ArrayList<UserEntity>( models.size() );
        for ( UserModel userModel : models ) {
            list.add( toEntity( userModel ) );
        }

        return list;
    }

    @Override
    public UserEntity updateEntity(UserModel model, UserEntity entity) {
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
        if ( model.getUserName() != null ) {
            entity.setUserName( model.getUserName() );
        }
        else {
            entity.setUserName( null );
        }
        if ( model.getEmail() != null ) {
            entity.setEmail( model.getEmail() );
        }
        else {
            entity.setEmail( null );
        }
        if ( model.getPassword() != null ) {
            entity.setPassword( model.getPassword() );
        }
        else {
            entity.setPassword( null );
        }
        if ( model.getFirstName() != null ) {
            entity.setFirstName( model.getFirstName() );
        }
        else {
            entity.setFirstName( null );
        }
        if ( model.getLastName() != null ) {
            entity.setLastName( model.getLastName() );
        }
        else {
            entity.setLastName( null );
        }
        if ( model.getUid() != null ) {
            entity.setUid( model.getUid() );
        }
        else {
            entity.setUid( null );
        }
        if ( model.getActive() != null ) {
            entity.setActive( model.getActive() );
        }
        if ( model.getParent() != null ) {
            if ( entity.getParent() == null ) {
                entity.setParent( new UserEntity() );
            }
            updateEntity( model.getParent(), entity.getParent() );
        }
        else {
            entity.setParent( null );
        }
        if ( model.getTreePath() != null ) {
            entity.setTreePath( model.getTreePath() );
        }
        else {
            entity.setTreePath( null );
        }
        if ( model.getWalletAddress() != null ) {
            entity.setWalletAddress( model.getWalletAddress() );
        }
        else {
            entity.setWalletAddress( null );
        }
        if ( entity.getRoles() != null ) {
            Set<RoleEntity> set = roleModelSetToRoleEntitySet( model.getRoles() );
            if ( set != null ) {
                entity.getRoles().clear();
                entity.getRoles().addAll( set );
            }
        }
        else {
            Set<RoleEntity> set = roleModelSetToRoleEntitySet( model.getRoles() );
            if ( set != null ) {
                entity.setRoles( set );
            }
        }

        return entity;
    }

    @Override
    public List<UserEntity> updateEntity(List<UserModel> modelList, List<UserEntity> entityList) {
        if ( modelList == null ) {
            return null;
        }

        entityList.clear();
        for ( UserModel userModel : modelList ) {
            entityList.add( toEntity( userModel ) );
        }

        return entityList;
    }

    @Override
    public UserModel toModel(UserEntity entity) {
        if ( entity == null ) {
            return null;
        }

        UserModel userModel = new UserModel();

        if ( entity.getId() != null ) {
            userModel.setId( entity.getId() );
        }
        if ( entity.getModifiedDate() != null ) {
            userModel.setModifiedDate( entity.getModifiedDate() );
        }
        if ( entity.getCreatedDate() != null ) {
            userModel.setCreatedDate( entity.getCreatedDate() );
        }
        userModel.setVersion( entity.getVersion() );
        if ( entity.getSelectTitle() != null ) {
            userModel.setSelectTitle( entity.getSelectTitle() );
        }
        if ( entity.getUserName() != null ) {
            userModel.setUserName( entity.getUserName() );
        }
        if ( entity.getEmail() != null ) {
            userModel.setEmail( entity.getEmail() );
        }
        if ( entity.getPassword() != null ) {
            userModel.setPassword( entity.getPassword() );
        }
        if ( entity.getFirstName() != null ) {
            userModel.setFirstName( entity.getFirstName() );
        }
        if ( entity.getLastName() != null ) {
            userModel.setLastName( entity.getLastName() );
        }
        userModel.setActive( entity.isActive() );
        if ( entity.getUid() != null ) {
            userModel.setUid( entity.getUid() );
        }
        if ( entity.getParent() != null ) {
            userModel.setParent( userEntityToUserModel( entity.getParent() ) );
        }
        if ( entity.getTreePath() != null ) {
            userModel.setTreePath( entity.getTreePath() );
        }
        if ( entity.getWalletAddress() != null ) {
            userModel.setWalletAddress( entity.getWalletAddress() );
        }
        Set<RoleModel> set = roleEntitySetToRoleModelSet( entity.getRoles() );
        if ( set != null ) {
            userModel.setRoles( set );
        }

        return userModel;
    }

    protected Set<RoleEntity> roleModelSetToRoleEntitySet(Set<RoleModel> set) {
        if ( set == null ) {
            return null;
        }

        Set<RoleEntity> set1 = new HashSet<RoleEntity>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( RoleModel roleModel : set ) {
            set1.add( roleMapper.toEntity( roleModel ) );
        }

        return set1;
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
        if ( userEntity.getParent() != null ) {
            userModel.setParent( toModel( userEntity.getParent() ) );
        }
        if ( userEntity.getTreePath() != null ) {
            userModel.setTreePath( userEntity.getTreePath() );
        }
        if ( userEntity.getWalletAddress() != null ) {
            userModel.setWalletAddress( userEntity.getWalletAddress() );
        }

        return userModel;
    }

    protected Set<RoleModel> roleEntitySetToRoleModelSet(Set<RoleEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<RoleModel> set1 = new HashSet<RoleModel>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( RoleEntity roleEntity : set ) {
            set1.add( roleMapper.toModel( roleEntity ) );
        }

        return set1;
    }
}
