package com.sjec.rcms.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.sjec.rcms.dao.EntityCheck;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ENTITY_CHECK.
*/
public class EntityCheckDao extends AbstractDao<EntityCheck, Void> {

    public static final String TABLENAME = "ENTITY_CHECK";

    /**
     * Properties of entity EntityCheck.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property InnerID = new Property(0, String.class, "InnerID", false, "INNER_ID");
        public final static Property Add_UserID = new Property(1, String.class, "Add_UserID", false, "ADD__USER_ID");
        public final static Property Add_Time = new Property(2, String.class, "Add_Time", false, "ADD__TIME");
        public final static Property Add_IP = new Property(3, String.class, "Add_IP", false, "ADD__IP");
        public final static Property Update_UserID = new Property(4, String.class, "Update_UserID", false, "UPDATE__USER_ID");
        public final static Property Update_Time = new Property(5, String.class, "Update_Time", false, "UPDATE__TIME");
        public final static Property Update_IP = new Property(6, String.class, "Update_IP", false, "UPDATE__IP");
        public final static Property CompleteDate	 = new Property(7, String.class, "CompleteDate	", false, "COMPLETE_DATE	");
        public final static Property ConfirmUserID = new Property(8, String.class, "ConfirmUserID", false, "CONFIRM_USER_ID");
        public final static Property ConfirmRemark = new Property(9, String.class, "ConfirmRemark", false, "CONFIRM_REMARK");
        public final static Property IsNeedAbarbeitung = new Property(10, Integer.class, "IsNeedAbarbeitung", false, "IS_NEED_ABARBEITUNG");
        public final static Property StartLongitude = new Property(11, String.class, "StartLongitude", false, "START_LONGITUDE");
        public final static Property StartLatitude = new Property(12, String.class, "StartLatitude", false, "START_LATITUDE");
        public final static Property EndLongitude = new Property(13, String.class, "EndLongitude", false, "END_LONGITUDE");
        public final static Property EndLatitude = new Property(14, String.class, "EndLatitude", false, "END_LATITUDE");
        public final static Property CheckDesc = new Property(15, String.class, "CheckDesc", false, "CHECK_DESC");
    };


    public EntityCheckDao(DaoConfig config) {
        super(config);
    }
    
    public EntityCheckDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ENTITY_CHECK' (" + //
                "'INNER_ID' TEXT," + // 0: InnerID
                "'ADD__USER_ID' TEXT," + // 1: Add_UserID
                "'ADD__TIME' TEXT," + // 2: Add_Time
                "'ADD__IP' TEXT," + // 3: Add_IP
                "'UPDATE__USER_ID' TEXT," + // 4: Update_UserID
                "'UPDATE__TIME' TEXT," + // 5: Update_Time
                "'UPDATE__IP' TEXT," + // 6: Update_IP
                "'COMPLETE_DATE	' TEXT," + // 7: CompleteDate	
                "'CONFIRM_USER_ID' TEXT," + // 8: ConfirmUserID
                "'CONFIRM_REMARK' TEXT," + // 9: ConfirmRemark
                "'IS_NEED_ABARBEITUNG' INTEGER," + // 10: IsNeedAbarbeitung
                "'START_LONGITUDE' TEXT," + // 11: StartLongitude
                "'START_LATITUDE' TEXT," + // 12: StartLatitude
                "'END_LONGITUDE' TEXT," + // 13: EndLongitude
                "'END_LATITUDE' TEXT," + // 14: EndLatitude
                "'CHECK_DESC' TEXT);"); // 15: CheckDesc
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ENTITY_CHECK'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, EntityCheck entity) {
        stmt.clearBindings();
 
        String InnerID = entity.getInnerID();
        if (InnerID != null) {
            stmt.bindString(1, InnerID);
        }
 
        String Add_UserID = entity.getAdd_UserID();
        if (Add_UserID != null) {
            stmt.bindString(2, Add_UserID);
        }
 
        String Add_Time = entity.getAdd_Time();
        if (Add_Time != null) {
            stmt.bindString(3, Add_Time);
        }
 
        String Add_IP = entity.getAdd_IP();
        if (Add_IP != null) {
            stmt.bindString(4, Add_IP);
        }
 
        String Update_UserID = entity.getUpdate_UserID();
        if (Update_UserID != null) {
            stmt.bindString(5, Update_UserID);
        }
 
        String Update_Time = entity.getUpdate_Time();
        if (Update_Time != null) {
            stmt.bindString(6, Update_Time);
        }
 
        String Update_IP = entity.getUpdate_IP();
        if (Update_IP != null) {
            stmt.bindString(7, Update_IP);
        }
 
        String CompleteDate	 = entity.getCompleteDate	();
        if (CompleteDate	 != null) {
            stmt.bindString(8, CompleteDate	);
        }
 
        String ConfirmUserID = entity.getConfirmUserID();
        if (ConfirmUserID != null) {
            stmt.bindString(9, ConfirmUserID);
        }
 
        String ConfirmRemark = entity.getConfirmRemark();
        if (ConfirmRemark != null) {
            stmt.bindString(10, ConfirmRemark);
        }
 
        Integer IsNeedAbarbeitung = entity.getIsNeedAbarbeitung();
        if (IsNeedAbarbeitung != null) {
            stmt.bindLong(11, IsNeedAbarbeitung);
        }
 
        String StartLongitude = entity.getStartLongitude();
        if (StartLongitude != null) {
            stmt.bindString(12, StartLongitude);
        }
 
        String StartLatitude = entity.getStartLatitude();
        if (StartLatitude != null) {
            stmt.bindString(13, StartLatitude);
        }
 
        String EndLongitude = entity.getEndLongitude();
        if (EndLongitude != null) {
            stmt.bindString(14, EndLongitude);
        }
 
        String EndLatitude = entity.getEndLatitude();
        if (EndLatitude != null) {
            stmt.bindString(15, EndLatitude);
        }
 
        String CheckDesc = entity.getCheckDesc();
        if (CheckDesc != null) {
            stmt.bindString(16, CheckDesc);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public EntityCheck readEntity(Cursor cursor, int offset) {
        EntityCheck entity = new EntityCheck( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // InnerID
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Add_UserID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // Add_Time
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // Add_IP
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // Update_UserID
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // Update_Time
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // Update_IP
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // CompleteDate	
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // ConfirmUserID
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // ConfirmRemark
            cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10), // IsNeedAbarbeitung
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // StartLongitude
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // StartLatitude
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // EndLongitude
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // EndLatitude
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15) // CheckDesc
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, EntityCheck entity, int offset) {
        entity.setInnerID(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setAdd_UserID(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAdd_Time(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAdd_IP(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUpdate_UserID(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUpdate_Time(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setUpdate_IP(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCompleteDate	(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setConfirmUserID(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setConfirmRemark(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setIsNeedAbarbeitung(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
        entity.setStartLongitude(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setStartLatitude(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setEndLongitude(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setEndLatitude(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setCheckDesc(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(EntityCheck entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(EntityCheck entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
