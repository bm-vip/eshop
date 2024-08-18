package com.eshop.app.mapping;

import com.eshop.app.entity.ParameterEntity;
import com.eshop.app.entity.ParameterGroupEntity;
import com.eshop.app.model.ParameterModel;
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
public class ParameterMapperImpl implements ParameterMapper {

    @Autowired
    private ParameterGroupMapper parameterGroupMapper;

    @Override
    public ParameterModel toModel(ParameterEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ParameterModel parameterModel = new ParameterModel();

        if ( entity.getId() != null ) {
            parameterModel.setId( entity.getId() );
        }
        if ( entity.getModifiedDate() != null ) {
            parameterModel.setModifiedDate( entity.getModifiedDate() );
        }
        if ( entity.getCreatedDate() != null ) {
            parameterModel.setCreatedDate( entity.getCreatedDate() );
        }
        parameterModel.setVersion( entity.getVersion() );
        if ( entity.getSelectTitle() != null ) {
            parameterModel.setSelectTitle( entity.getSelectTitle() );
        }
        if ( entity.getCode() != null ) {
            parameterModel.setCode( entity.getCode() );
        }
        if ( entity.getTitle() != null ) {
            parameterModel.setTitle( entity.getTitle() );
        }
        if ( entity.getValue() != null ) {
            parameterModel.setValue( entity.getValue() );
        }
        if ( entity.getParameterGroup() != null ) {
            parameterModel.setParameterGroup( parameterGroupMapper.toModel( entity.getParameterGroup() ) );
        }

        return parameterModel;
    }

    @Override
    public List<ParameterModel> toModel(List<ParameterEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ParameterModel> list = new ArrayList<ParameterModel>( entities.size() );
        for ( ParameterEntity parameterEntity : entities ) {
            list.add( toModel( parameterEntity ) );
        }

        return list;
    }

    @Override
    public ParameterEntity toEntity(ParameterModel model) {
        if ( model == null ) {
            return null;
        }

        ParameterEntity parameterEntity = new ParameterEntity();

        if ( model.getCreatedDate() != null ) {
            parameterEntity.setCreatedDate( model.getCreatedDate() );
        }
        if ( model.getModifiedDate() != null ) {
            parameterEntity.setModifiedDate( model.getModifiedDate() );
        }
        parameterEntity.setVersion( model.getVersion() );
        if ( model.getSelectTitle() != null ) {
            parameterEntity.setSelectTitle( model.getSelectTitle() );
        }
        if ( model.getId() != null ) {
            parameterEntity.setId( model.getId() );
        }
        if ( model.getCode() != null ) {
            parameterEntity.setCode( model.getCode() );
        }
        if ( model.getTitle() != null ) {
            parameterEntity.setTitle( model.getTitle() );
        }
        if ( model.getValue() != null ) {
            parameterEntity.setValue( model.getValue() );
        }
        if ( model.getParameterGroup() != null ) {
            parameterEntity.setParameterGroup( parameterGroupMapper.toEntity( model.getParameterGroup() ) );
        }

        return parameterEntity;
    }

    @Override
    public List<ParameterEntity> toEntity(List<ParameterModel> models) {
        if ( models == null ) {
            return null;
        }

        List<ParameterEntity> list = new ArrayList<ParameterEntity>( models.size() );
        for ( ParameterModel parameterModel : models ) {
            list.add( toEntity( parameterModel ) );
        }

        return list;
    }

    @Override
    public ParameterEntity updateEntity(ParameterModel model, ParameterEntity entity) {
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
        if ( model.getValue() != null ) {
            entity.setValue( model.getValue() );
        }
        else {
            entity.setValue( null );
        }
        if ( model.getParameterGroup() != null ) {
            if ( entity.getParameterGroup() == null ) {
                entity.setParameterGroup( new ParameterGroupEntity() );
            }
            parameterGroupMapper.updateEntity( model.getParameterGroup(), entity.getParameterGroup() );
        }
        else {
            entity.setParameterGroup( null );
        }

        return entity;
    }

    @Override
    public List<ParameterEntity> updateEntity(List<ParameterModel> modelList, List<ParameterEntity> entityList) {
        if ( modelList == null ) {
            return null;
        }

        entityList.clear();
        for ( ParameterModel parameterModel : modelList ) {
            entityList.add( toEntity( parameterModel ) );
        }

        return entityList;
    }
}
