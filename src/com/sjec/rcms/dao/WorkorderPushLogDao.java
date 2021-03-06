package com.sjec.rcms.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.sjec.rcms.dao.WorkorderPushLog;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table WORKORDER_PUSH_LOG.
*/
public class WorkorderPushLogDao extends AbstractDao<WorkorderPushLog, Void> {

    public static final String TABLENAME = "WORKORDER_PUSH_LOG";

    /**
     * Properties of entity WorkorderPushLog.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Telephone = new Property(0, String.class, "Telephone", false, "TELEPHONE");
        public final static Property UserName = new Property(1, String.class, "UserName", false, "USER_NAME");
        public final static Property PushResult = new Property(2, String.class, "PushResult", false, "PUSH_RESULT");
        public final static Property PushTime = new Property(3, String.class, "PushTime", false, "PUSH_TIME");
    };


    public WorkorderPushLogDao(DaoConfig config) {
        super(config);
    }
    
    public WorkorderPushLogDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'WORKORDER_PUSH_LOG' (" + //
                "'TELEPHONE' TEXT," + // 0: Telephone
                "'USER_NAME' TEXT," + // 1: UserName
                "'PUSH_RESULT' TEXT," + // 2: PushResult
                "'PUSH_TIME' TEXT);"); // 3: PushTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'WORKORDER_PUSH_LOG'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, WorkorderPushLog entity) {
        stmt.clearBindings();
 
        String Telephone = entity.getTelephone();
        if (Telephone != null) {
            stmt.bindString(1, Telephone);
        }
 
        String UserName = entity.getUserName();
        if (UserName != null) {
            stmt.bindString(2, UserName);
        }
 
        String PushResult = entity.getPushResult();
        if (PushResult != null) {
            stmt.bindString(3, PushResult);
        }
 
        String PushTime = entity.getPushTime();
        if (PushTime != null) {
            stmt.bindString(4, PushTime);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public WorkorderPushLog readEntity(Cursor cursor, int offset) {
        WorkorderPushLog entity = new WorkorderPushLog( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // Telephone
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // UserName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // PushResult
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // PushTime
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, WorkorderPushLog entity, int offset) {
        entity.setTelephone(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setUserName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPushResult(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPushTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(WorkorderPushLog entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(WorkorderPushLog entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
