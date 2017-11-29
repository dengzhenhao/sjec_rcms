package com.sjec.rcms.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

import com.sjec.rcms.dao.EntityUserDao;
import com.sjec.rcms.dao.EntityKnowledgeTypeDao;
import com.sjec.rcms.dao.EntityDeviceDao;
import com.sjec.rcms.dao.EntityWorkorderDao;
import com.sjec.rcms.dao.EntityVillageDao;
import com.sjec.rcms.dao.EntityFactoryCodeDao;
import com.sjec.rcms.dao.EntityRepairDao;
import com.sjec.rcms.dao.EntityMaintainDao;
import com.sjec.rcms.dao.EntityWorkorderPushLogDao;
import com.sjec.rcms.dao.EntityPauseConfirmDao;
import com.sjec.rcms.dao.EntityCompanyWorkerDao;
import com.sjec.rcms.dao.EntityAssistWorkerDao;
import com.sjec.rcms.dao.EntityPauseLogDao;
import com.sjec.rcms.dao.EntityWorkOrderPicDao;
import com.sjec.rcms.dao.EntityCheckDao;
import com.sjec.rcms.dao.EntityUserCompanyDao;
import com.sjec.rcms.dao.EntityUserStaffDao;
import com.sjec.rcms.dao.EntitySparePartQuotationDao;
import com.sjec.rcms.dao.EntitySparePartQuotationDataDao;
import com.sjec.rcms.dao.EntitySparePartApplyDao;
import com.sjec.rcms.dao.EntitySparePartApplyPicDao;
import com.sjec.rcms.dao.EntitySparePartApplyDataDao;
import com.sjec.rcms.dao.EntityQuotationStatusLogDao;
import com.sjec.rcms.dao.EntityBigCustomerDao;
import com.sjec.rcms.dao.EntityUnSubmitWorkorderDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * Master of DAO (schema version 1002): knows all DAOs.
*/
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1002;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        EntityUserDao.createTable(db, ifNotExists);
        EntityKnowledgeTypeDao.createTable(db, ifNotExists);
        EntityDeviceDao.createTable(db, ifNotExists);
        EntityWorkorderDao.createTable(db, ifNotExists);
        EntityVillageDao.createTable(db, ifNotExists);
        EntityFactoryCodeDao.createTable(db, ifNotExists);
        EntityRepairDao.createTable(db, ifNotExists);
        EntityMaintainDao.createTable(db, ifNotExists);
        EntityWorkorderPushLogDao.createTable(db, ifNotExists);
        EntityPauseConfirmDao.createTable(db, ifNotExists);
        EntityCompanyWorkerDao.createTable(db, ifNotExists);
        EntityAssistWorkerDao.createTable(db, ifNotExists);
        EntityPauseLogDao.createTable(db, ifNotExists);
        EntityWorkOrderPicDao.createTable(db, ifNotExists);
        EntityCheckDao.createTable(db, ifNotExists);
        EntityUserCompanyDao.createTable(db, ifNotExists);
        EntityUserStaffDao.createTable(db, ifNotExists);
        EntitySparePartQuotationDao.createTable(db, ifNotExists);
        EntitySparePartQuotationDataDao.createTable(db, ifNotExists);
        EntitySparePartApplyDao.createTable(db, ifNotExists);
        EntitySparePartApplyPicDao.createTable(db, ifNotExists);
        EntitySparePartApplyDataDao.createTable(db, ifNotExists);
        EntityQuotationStatusLogDao.createTable(db, ifNotExists);
        EntityBigCustomerDao.createTable(db, ifNotExists);
        EntityUnSubmitWorkorderDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        EntityUserDao.dropTable(db, ifExists);
        EntityKnowledgeTypeDao.dropTable(db, ifExists);
        EntityDeviceDao.dropTable(db, ifExists);
        EntityWorkorderDao.dropTable(db, ifExists);
        EntityVillageDao.dropTable(db, ifExists);
        EntityFactoryCodeDao.dropTable(db, ifExists);
        EntityRepairDao.dropTable(db, ifExists);
        EntityMaintainDao.dropTable(db, ifExists);
        EntityWorkorderPushLogDao.dropTable(db, ifExists);
        EntityPauseConfirmDao.dropTable(db, ifExists);
        EntityCompanyWorkerDao.dropTable(db, ifExists);
        EntityAssistWorkerDao.dropTable(db, ifExists);
        EntityPauseLogDao.dropTable(db, ifExists);
        EntityWorkOrderPicDao.dropTable(db, ifExists);
        EntityCheckDao.dropTable(db, ifExists);
        EntityUserCompanyDao.dropTable(db, ifExists);
        EntityUserStaffDao.dropTable(db, ifExists);
        EntitySparePartQuotationDao.dropTable(db, ifExists);
        EntitySparePartQuotationDataDao.dropTable(db, ifExists);
        EntitySparePartApplyDao.dropTable(db, ifExists);
        EntitySparePartApplyPicDao.dropTable(db, ifExists);
        EntitySparePartApplyDataDao.dropTable(db, ifExists);
        EntityQuotationStatusLogDao.dropTable(db, ifExists);
        EntityBigCustomerDao.dropTable(db, ifExists);
        EntityUnSubmitWorkorderDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }
    
    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(EntityUserDao.class);
        registerDaoClass(EntityKnowledgeTypeDao.class);
        registerDaoClass(EntityDeviceDao.class);
        registerDaoClass(EntityWorkorderDao.class);
        registerDaoClass(EntityVillageDao.class);
        registerDaoClass(EntityFactoryCodeDao.class);
        registerDaoClass(EntityRepairDao.class);
        registerDaoClass(EntityMaintainDao.class);
        registerDaoClass(EntityWorkorderPushLogDao.class);
        registerDaoClass(EntityPauseConfirmDao.class);
        registerDaoClass(EntityCompanyWorkerDao.class);
        registerDaoClass(EntityAssistWorkerDao.class);
        registerDaoClass(EntityPauseLogDao.class);
        registerDaoClass(EntityWorkOrderPicDao.class);
        registerDaoClass(EntityCheckDao.class);
        registerDaoClass(EntityUserCompanyDao.class);
        registerDaoClass(EntityUserStaffDao.class);
        registerDaoClass(EntitySparePartQuotationDao.class);
        registerDaoClass(EntitySparePartQuotationDataDao.class);
        registerDaoClass(EntitySparePartApplyDao.class);
        registerDaoClass(EntitySparePartApplyPicDao.class);
        registerDaoClass(EntitySparePartApplyDataDao.class);
        registerDaoClass(EntityQuotationStatusLogDao.class);
        registerDaoClass(EntityBigCustomerDao.class);
        registerDaoClass(EntityUnSubmitWorkorderDao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }
    
}