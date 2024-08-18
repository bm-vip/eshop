package com.eshop.app.mapping;

import com.eshop.app.entity.ParameterGroupEntity;
import com.eshop.app.model.ParameterGroupModel;
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
public class ParameterGroupMapperImpl implements ParameterGroupMapper {

    @Override
    public ParameterGroupModel toModel(ParameterGroupEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ParameterGroupModel parameterGroupModel = new ParameterGroupModel();

        if ( entity.getId() != null ) {
            parameterGroupModel.setId( entity.getId() );
        }
        if ( entity.getModifiedDate() != null ) {
            parameterGroupModel.setModifiedDate( entity.getModifiedDate() );
        }
        if ( entity.getCreatedDate() != null ) {
            parameterGroupModel.setCreatedDate( entity.getCreatedDate() );
        }
        parameterGroupModel.setVersion( entity.getVersion() );
        if ( entity.getSelectTitle() != null ) {
            parameterGroupModel.setSelectTitle( entity.getSelectTitle() );
        }
        if ( entity.getCode() != null ) {
            parameterGroupModel.setCode( entity.getCode() );
        }
        if ( entity.getTitle() != null ) {
            parameterGroupModel.setTitle( entity.getTitle() );
        }

        return parameterGroupModel;
    }

    @Override
    public List<ParameterGroupModel> toModel(List<ParameterGroupEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ParameterGroupModel> list = new ArrayList<ParameterGroupModel>( entities.size() );
        for ( ParameterGroupEntity parameterGroupEntity : entities ) {
            list.add( toModel( parameterGroupEntity ) );
        }

        return list;
    }

    @Override
    public ParameterGroupEntity toEntity(ParameterGroupModel model) {
        if ( model == null ) {
            return null;
        }

        ParameterGroupEntity parameterGroupEntity = new ParameterGroupEntity();

        if ( model.getCreatedDate() != null ) {
            parameterGroupEntity.setCreatedDate( model.getCreatedDate() );
        }
        if ( model.getModifiedDate() != null ) {
            parameterGroupEntity.setModifiedDate( model.getModifiedDate() );
        }
        parameterGroupEntity.setVersion( model.getVersion() );
        if ( model.getSelectTitle() != null ) {
            parameterGroupEntity.setSelectTitle( model.getSelectTitle() );
        }
        if ( model.getId() != null ) {
            parameterGroupEntity.setId( model.getId() );
        }
        if ( model.getCode() != null ) {
            parameterGroupEntity.setCode( model.getCode() );
        }
        if ( model.getTitle() != null ) {
            parameterGroupEntity.setTitle( model.getTitle() );
        }

        return parameterGroupEntity;
    }

    @Override
    public List<ParameterGroupEntity> toEntity(List<ParameterGroupModel> models) {
        if ( models == null ) {
            return null;
        }

        List<ParameterGroupEntity> list = new ArrayList<ParameterGroupEntity>( models.size() );
        for ( ParameterGroupModel parameterGroupModel : models ) {
            list.add( toEntity( parameterGroupModel ) );
        }

        return list;
    }

    @Override
    public ParameterGroupEntity updateEntity(ParameterGroupModel model, ParameterGroupEntity entity) {
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
        if ( model.getCode() != null ) {
            entity.setCode( model.getCode() );
        }
        else {
            entity.setCode( null );
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
    public List<ParameterGroupEntity> updateEntity(List<ParameterGroupModel> modelList, List<ParameterGroupEntity> entityList) {
        if ( modelList == null ) {
            return null;
        }

        entityList.clear();
        for ( ParameterGroupModel parameterGroupModel : modelList ) {
            entityList.add( toEntity( parameterGroupModel ) );
        }

        return entityList;
    }
}
