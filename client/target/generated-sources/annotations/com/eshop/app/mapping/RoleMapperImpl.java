package com.eshop.app.mapping;

import com.eshop.app.entity.RoleEntity;
import com.eshop.app.model.RoleModel;
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
public class RoleMapperImpl implements RoleMapper {

    @Override
    public RoleModel toModel(RoleEntity entity) {
        if ( entity == null ) {
            return null;
        }

        RoleModel roleModel = new RoleModel();

        if ( entity.getId() != null ) {
            roleModel.setId( entity.getId() );
        }
        if ( entity.getModifiedDate() != null ) {
            roleModel.setModifiedDate( entity.getModifiedDate() );
        }
        if ( entity.getCreatedDate() != null ) {
            roleModel.setCreatedDate( entity.getCreatedDate() );
        }
        roleModel.setVersion( entity.getVersion() );
        if ( entity.getSelectTitle() != null ) {
            roleModel.setSelectTitle( entity.getSelectTitle() );
        }
        if ( entity.getRole() != null ) {
            roleModel.setRole( entity.getRole() );
        }
        if ( entity.getTitle() != null ) {
            roleModel.setTitle( entity.getTitle() );
        }

        return roleModel;
    }

    @Override
    public List<RoleModel> toModel(List<RoleEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<RoleModel> list = new ArrayList<RoleModel>( entities.size() );
        for ( RoleEntity roleEntity : entities ) {
            list.add( toModel( roleEntity ) );
        }

        return list;
    }

    @Override
    public RoleEntity toEntity(RoleModel model) {
        if ( model == null ) {
            return null;
        }

        RoleEntity roleEntity = new RoleEntity();

        if ( model.getCreatedDate() != null ) {
            roleEntity.setCreatedDate( model.getCreatedDate() );
        }
        if ( model.getModifiedDate() != null ) {
            roleEntity.setModifiedDate( model.getModifiedDate() );
        }
        roleEntity.setVersion( model.getVersion() );
        if ( model.getSelectTitle() != null ) {
            roleEntity.setSelectTitle( model.getSelectTitle() );
        }
        if ( model.getId() != null ) {
            roleEntity.setId( model.getId() );
        }
        if ( model.getRole() != null ) {
            roleEntity.setRole( model.getRole() );
        }
        if ( model.getTitle() != null ) {
            roleEntity.setTitle( model.getTitle() );
        }

        return roleEntity;
    }

    @Override
    public List<RoleEntity> toEntity(List<RoleModel> models) {
        if ( models == null ) {
            return null;
        }

        List<RoleEntity> list = new ArrayList<RoleEntity>( models.size() );
        for ( RoleModel roleModel : models ) {
            list.add( toEntity( roleModel ) );
        }

        return list;
    }

    @Override
    public RoleEntity updateEntity(RoleModel model, RoleEntity entity) {
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
        if ( model.getRole() != null ) {
            entity.setRole( model.getRole() );
        }
        else {
            entity.setRole( null );
        }
        if ( model.getTitle() != null ) {
            entity.setTitle( model.getTitle() );
        }
        else {
            entity.setTitle( null );
        }

        return entity;
    }

    @Override
    public List<RoleEntity> updateEntity(List<RoleModel> modelList, List<RoleEntity> entityList) {
        if ( modelList == null ) {
            return null;
        }

        entityList.clear();
        for ( RoleModel roleModel : modelList ) {
            entityList.add( toEntity( roleModel ) );
        }

        return entityList;
    }
}
