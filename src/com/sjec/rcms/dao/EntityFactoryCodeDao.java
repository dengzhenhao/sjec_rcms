package com.sjec.rcms.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.sjec.rcms.dao.EntityFactoryCode;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ENTITY_FACTORY_CODE.
*/
public class EntityFactoryCodeDao extends AbstractDao<EntityFactoryCode, Void> {

    public static final String TABLENAME = "ENTITY_FACTORY_CODE";

    /**
     * Properties of entity EntityFactoryCode.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property InnerID = new Property(0, String.class, "InnerID", false, "INNER_ID");
        public final static Property CompanyName = new Property(1, String.class, "CompanyName", false, "COMPANY_NAME");
        public final static Property CompanyCode = new Property(2, String.class, "CompanyCode", false, "COMPANY_CODE");
        public final static Property CompanyIndex = new Property(3, String.class, "CompanyIndex", false, "COMPANY_INDEX");
    };


    public EntityFactoryCodeDao(DaoConfig config) {
        super(config);
    }
    
    public EntityFactoryCodeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ENTITY_FACTORY_CODE' (" + //
                "'INNER_ID' TEXT," + // 0: InnerID
                "'COMPANY_NAME' TEXT," + // 1: CompanyName
                "'COMPANY_CODE' TEXT," + // 2: CompanyCode
                "'COMPANY_INDEX' TEXT);"); // 3: CompanyIndex
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ENTITY_FACTORY_CODE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, EntityFactoryCode entity) {
        stmt.clearBindings();
 
        String InnerID = entity.getInnerID();
        if (InnerID != null) {
            stmt.bindString(1, InnerID);
        }
 
        String CompanyName = entity.getCompanyName();
        if (CompanyName != null) {
            stmt.bindString(2, CompanyName);
        }
 
        String CompanyCode = entity.getCompanyCode();
        if (CompanyCode != null) {
            stmt.bindString(3, CompanyCode);
        }
 
        String CompanyIndex = entity.getCompanyIndex();
        if (CompanyIndex != null) {
            stmt.bindString(4, CompanyIndex);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public EntityFactoryCode readEntity(Cursor cursor, int offset) {
        EntityFactoryCode entity = new EntityFactoryCode( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // InnerID
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // CompanyName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // CompanyCode
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // CompanyIndex
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, EntityFactoryCode entity, int offset) {
        entity.setInnerID(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setCompanyName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCompanyCode(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCompanyIndex(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(EntityFactoryCode entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(EntityFactoryCode entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
