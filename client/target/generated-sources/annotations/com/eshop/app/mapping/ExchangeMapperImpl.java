package com.eshop.app.mapping;

import com.eshop.app.entity.ExchangeEntity;
import com.eshop.app.model.ExchangeModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-18T23:41:13+0330",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class ExchangeMapperImpl implements ExchangeMapper {

    @Override
    public ExchangeModel toModel(ExchangeEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ExchangeModel exchangeModel = new ExchangeModel();

        if ( entity.getId() != null ) {
            exchangeModel.setId( entity.getId() );
        }
        if ( entity.getModifiedDate() != null ) {
            exchangeModel.setModifiedDate( entity.getModifiedDate() );
        }
        if ( entity.getCreatedDate() != null ) {
            exchangeModel.setCreatedDate( entity.getCreatedDate() );
        }
        exchangeModel.setVersion( entity.getVersion() );
        if ( entity.getSelectTitle() != null ) {
            exchangeModel.setSelectTitle( entity.getSelectTitle() );
        }
        if ( entity.getName() != null ) {
            exchangeModel.setName( entity.getName() );
        }
        if ( entity.getLogo() != null ) {
            exchangeModel.setLogo( entity.getLogo() );
        }

        return exchangeModel;
    }

    @Override
    public List<ExchangeModel> toModel(List<ExchangeEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ExchangeModel> list = new ArrayList<ExchangeModel>( entities.size() );
        for ( ExchangeEntity exchangeEntity : entities ) {
            list.add( toModel( exchangeEntity ) );
        }

        return list;
    }

    @Override
    public ExchangeEntity toEntity(ExchangeModel model) {
        if ( model == null ) {
            return null;
        }

        ExchangeEntity exchangeEntity = new ExchangeEntity();

        if ( model.getCreatedDate() != null ) {
            exchangeEntity.setCreatedDate( model.getCreatedDate() );
        }
        if ( model.getModifiedDate() != null ) {
            exchangeEntity.setModifiedDate( model.getModifiedDate() );
        }
        exchangeEntity.setVersion( model.getVersion() );
        if ( model.getSelectTitle() != null ) {
            exchangeEntity.setSelectTitle( model.getSelectTitle() );
        }
        if ( model.getId() != null ) {
            exchangeEntity.setId( model.getId() );
        }
        if ( model.getName() != null ) {
            exchangeEntity.setName( model.getName() );
        }
        if ( model.getLogo() != null ) {
            exchangeEntity.setLogo( model.getLogo() );
        }

        return exchangeEntity;
    }

    @Override
    public List<ExchangeEntity> toEntity(List<ExchangeModel> models) {
        if ( models == null ) {
            return null;
        }

        List<ExchangeEntity> list = new ArrayList<ExchangeEntity>( models.size() );
        for ( ExchangeModel exchangeModel : models ) {
            list.add( toEntity( exchangeModel ) );
        }

        return list;
    }

    @Override
    public ExchangeEntity updateEntity(ExchangeModel model, ExchangeEntity entity) {
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
        if ( model.getLogo() != null ) {
            entity.setLogo( model.getLogo() );
        }
        else {
            entity.setLogo( null );
        }

        return entity;
    }

    @Override
    public List<ExchangeEntity> updateEntity(List<ExchangeModel> modelList, List<ExchangeEntity> entityList) {
        if ( modelList == null ) {
            return null;
        }

        entityList.clear();
        for ( ExchangeModel exchangeModel : modelList ) {
            entityList.add( toEntity( exchangeModel ) );
        }

        return entityList;
    }
}
