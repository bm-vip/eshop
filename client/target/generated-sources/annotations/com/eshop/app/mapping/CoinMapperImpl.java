package com.eshop.app.mapping;

import com.eshop.app.entity.CoinEntity;
import com.eshop.app.model.CoinModel;
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
public class CoinMapperImpl implements CoinMapper {

    @Override
    public CoinModel toModel(CoinEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CoinModel coinModel = new CoinModel();

        if ( entity.getId() != null ) {
            coinModel.setId( entity.getId() );
        }
        if ( entity.getModifiedDate() != null ) {
            coinModel.setModifiedDate( entity.getModifiedDate() );
        }
        if ( entity.getCreatedDate() != null ) {
            coinModel.setCreatedDate( entity.getCreatedDate() );
        }
        coinModel.setVersion( entity.getVersion() );
        if ( entity.getSelectTitle() != null ) {
            coinModel.setSelectTitle( entity.getSelectTitle() );
        }
        if ( entity.getName() != null ) {
            coinModel.setName( entity.getName() );
        }
        if ( entity.getLogo() != null ) {
            coinModel.setLogo( entity.getLogo() );
        }

        return coinModel;
    }

    @Override
    public List<CoinModel> toModel(List<CoinEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<CoinModel> list = new ArrayList<CoinModel>( entities.size() );
        for ( CoinEntity coinEntity : entities ) {
            list.add( toModel( coinEntity ) );
        }

        return list;
    }

    @Override
    public CoinEntity toEntity(CoinModel model) {
        if ( model == null ) {
            return null;
        }

        CoinEntity coinEntity = new CoinEntity();

        if ( model.getCreatedDate() != null ) {
            coinEntity.setCreatedDate( model.getCreatedDate() );
        }
        if ( model.getModifiedDate() != null ) {
            coinEntity.setModifiedDate( model.getModifiedDate() );
        }
        coinEntity.setVersion( model.getVersion() );
        if ( model.getSelectTitle() != null ) {
            coinEntity.setSelectTitle( model.getSelectTitle() );
        }
        if ( model.getId() != null ) {
            coinEntity.setId( model.getId() );
        }
        if ( model.getName() != null ) {
            coinEntity.setName( model.getName() );
        }
        if ( model.getLogo() != null ) {
            coinEntity.setLogo( model.getLogo() );
        }

        return coinEntity;
    }

    @Override
    public List<CoinEntity> toEntity(List<CoinModel> models) {
        if ( models == null ) {
            return null;
        }

        List<CoinEntity> list = new ArrayList<CoinEntity>( models.size() );
        for ( CoinModel coinModel : models ) {
            list.add( toEntity( coinModel ) );
        }

        return list;
    }

    @Override
    public CoinEntity updateEntity(CoinModel model, CoinEntity entity) {
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
    public List<CoinEntity> updateEntity(List<CoinModel> modelList, List<CoinEntity> entityList) {
        if ( modelList == null ) {
            return null;
        }

        entityList.clear();
        for ( CoinModel coinModel : modelList ) {
            entityList.add( toEntity( coinModel ) );
        }

        return entityList;
    }
}
