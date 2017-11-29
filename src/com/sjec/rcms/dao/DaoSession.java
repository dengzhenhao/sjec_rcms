package com.sjec.rcms.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.sjec.rcms.dao.EntityUser;
import com.sjec.rcms.dao.EntityKnowledgeType;
import com.sjec.rcms.dao.EntityDevice;
import com.sjec.rcms.dao.EntityWorkorder;
import com.sjec.rcms.dao.EntityVillage;
import com.sjec.rcms.dao.EntityFactoryCode;
import com.sjec.rcms.dao.EntityRepair;
import com.sjec.rcms.dao.EntityMaintain;
import com.sjec.rcms.dao.EntityWorkorderPushLog;
import com.sjec.rcms.dao.EntityPauseConfirm;
import com.sjec.rcms.dao.EntityCompanyWorker;
import com.sjec.rcms.dao.EntityAssistWorker;
import com.sjec.rcms.dao.EntityPauseLog;
import com.sjec.rcms.dao.EntityWorkOrderPic;
import com.sjec.rcms.dao.EntityCheck;
import com.sjec.rcms.dao.EntityUserCompany;
import com.sjec.rcms.dao.EntityUserStaff;
import com.sjec.rcms.dao.EntitySparePartQuotation;
import com.sjec.rcms.dao.EntitySparePartQuotationData;
import com.sjec.rcms.dao.EntitySparePartApply;
import com.sjec.rcms.dao.EntitySparePartApplyPic;
import com.sjec.rcms.dao.EntitySparePartApplyData;
import com.sjec.rcms.dao.EntityQuotationStatusLog;
import com.sjec.rcms.dao.EntityBigCustomer;
import com.sjec.rcms.dao.EntityUnSubmitWorkorder;

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
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig entityUserDaoConfig;
    private final DaoConfig entityKnowledgeTypeDaoConfig;
    private final DaoConfig entityDeviceDaoConfig;
    private final DaoConfig entityWorkorderDaoConfig;
    private final DaoConfig entityVillageDaoConfig;
    private final DaoConfig entityFactoryCodeDaoConfig;
    private final DaoConfig entityRepairDaoConfig;
    private final DaoConfig entityMaintainDaoConfig;
    private final DaoConfig entityWorkorderPushLogDaoConfig;
    private final DaoConfig entityPauseConfirmDaoConfig;
    private final DaoConfig entityCompanyWorkerDaoConfig;
    private final DaoConfig entityAssistWorkerDaoConfig;
    private final DaoConfig entityPauseLogDaoConfig;
    private final DaoConfig entityWorkOrderPicDaoConfig;
    private final DaoConfig entityCheckDaoConfig;
    private final DaoConfig entityUserCompanyDaoConfig;
    private final DaoConfig entityUserStaffDaoConfig;
    private final DaoConfig entitySparePartQuotationDaoConfig;
    private final DaoConfig entitySparePartQuotationDataDaoConfig;
    private final DaoConfig entitySparePartApplyDaoConfig;
    private final DaoConfig entitySparePartApplyPicDaoConfig;
    private final DaoConfig entitySparePartApplyDataDaoConfig;
    private final DaoConfig entityQuotationStatusLogDaoConfig;
    private final DaoConfig entityBigCustomerDaoConfig;
    private final DaoConfig entityUnSubmitWorkorderDaoConfig;

    private final EntityUserDao entityUserDao;
    private final EntityKnowledgeTypeDao entityKnowledgeTypeDao;
    private final EntityDeviceDao entityDeviceDao;
    private final EntityWorkorderDao entityWorkorderDao;
    private final EntityVillageDao entityVillageDao;
    private final EntityFactoryCodeDao entityFactoryCodeDao;
    private final EntityRepairDao entityRepairDao;
    private final EntityMaintainDao entityMaintainDao;
    private final EntityWorkorderPushLogDao entityWorkorderPushLogDao;
    private final EntityPauseConfirmDao entityPauseConfirmDao;
    private final EntityCompanyWorkerDao entityCompanyWorkerDao;
    private final EntityAssistWorkerDao entityAssistWorkerDao;
    private final EntityPauseLogDao entityPauseLogDao;
    private final EntityWorkOrderPicDao entityWorkOrderPicDao;
    private final EntityCheckDao entityCheckDao;
    private final EntityUserCompanyDao entityUserCompanyDao;
    private final EntityUserStaffDao entityUserStaffDao;
    private final EntitySparePartQuotationDao entitySparePartQuotationDao;
    private final EntitySparePartQuotationDataDao entitySparePartQuotationDataDao;
    private final EntitySparePartApplyDao entitySparePartApplyDao;
    private final EntitySparePartApplyPicDao entitySparePartApplyPicDao;
    private final EntitySparePartApplyDataDao entitySparePartApplyDataDao;
    private final EntityQuotationStatusLogDao entityQuotationStatusLogDao;
    private final EntityBigCustomerDao entityBigCustomerDao;
    private final EntityUnSubmitWorkorderDao entityUnSubmitWorkorderDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        entityUserDaoConfig = daoConfigMap.get(EntityUserDao.class).clone();
        entityUserDaoConfig.initIdentityScope(type);

        entityKnowledgeTypeDaoConfig = daoConfigMap.get(EntityKnowledgeTypeDao.class).clone();
        entityKnowledgeTypeDaoConfig.initIdentityScope(type);

        entityDeviceDaoConfig = daoConfigMap.get(EntityDeviceDao.class).clone();
        entityDeviceDaoConfig.initIdentityScope(type);

        entityWorkorderDaoConfig = daoConfigMap.get(EntityWorkorderDao.class).clone();
        entityWorkorderDaoConfig.initIdentityScope(type);

        entityVillageDaoConfig = daoConfigMap.get(EntityVillageDao.class).clone();
        entityVillageDaoConfig.initIdentityScope(type);

        entityFactoryCodeDaoConfig = daoConfigMap.get(EntityFactoryCodeDao.class).clone();
        entityFactoryCodeDaoConfig.initIdentityScope(type);

        entityRepairDaoConfig = daoConfigMap.get(EntityRepairDao.class).clone();
        entityRepairDaoConfig.initIdentityScope(type);

        entityMaintainDaoConfig = daoConfigMap.get(EntityMaintainDao.class).clone();
        entityMaintainDaoConfig.initIdentityScope(type);

        entityWorkorderPushLogDaoConfig = daoConfigMap.get(EntityWorkorderPushLogDao.class).clone();
        entityWorkorderPushLogDaoConfig.initIdentityScope(type);

        entityPauseConfirmDaoConfig = daoConfigMap.get(EntityPauseConfirmDao.class).clone();
        entityPauseConfirmDaoConfig.initIdentityScope(type);

        entityCompanyWorkerDaoConfig = daoConfigMap.get(EntityCompanyWorkerDao.class).clone();
        entityCompanyWorkerDaoConfig.initIdentityScope(type);

        entityAssistWorkerDaoConfig = daoConfigMap.get(EntityAssistWorkerDao.class).clone();
        entityAssistWorkerDaoConfig.initIdentityScope(type);

        entityPauseLogDaoConfig = daoConfigMap.get(EntityPauseLogDao.class).clone();
        entityPauseLogDaoConfig.initIdentityScope(type);

        entityWorkOrderPicDaoConfig = daoConfigMap.get(EntityWorkOrderPicDao.class).clone();
        entityWorkOrderPicDaoConfig.initIdentityScope(type);

        entityCheckDaoConfig = daoConfigMap.get(EntityCheckDao.class).clone();
        entityCheckDaoConfig.initIdentityScope(type);

        entityUserCompanyDaoConfig = daoConfigMap.get(EntityUserCompanyDao.class).clone();
        entityUserCompanyDaoConfig.initIdentityScope(type);

        entityUserStaffDaoConfig = daoConfigMap.get(EntityUserStaffDao.class).clone();
        entityUserStaffDaoConfig.initIdentityScope(type);

        entitySparePartQuotationDaoConfig = daoConfigMap.get(EntitySparePartQuotationDao.class).clone();
        entitySparePartQuotationDaoConfig.initIdentityScope(type);

        entitySparePartQuotationDataDaoConfig = daoConfigMap.get(EntitySparePartQuotationDataDao.class).clone();
        entitySparePartQuotationDataDaoConfig.initIdentityScope(type);

        entitySparePartApplyDaoConfig = daoConfigMap.get(EntitySparePartApplyDao.class).clone();
        entitySparePartApplyDaoConfig.initIdentityScope(type);

        entitySparePartApplyPicDaoConfig = daoConfigMap.get(EntitySparePartApplyPicDao.class).clone();
        entitySparePartApplyPicDaoConfig.initIdentityScope(type);

        entitySparePartApplyDataDaoConfig = daoConfigMap.get(EntitySparePartApplyDataDao.class).clone();
        entitySparePartApplyDataDaoConfig.initIdentityScope(type);

        entityQuotationStatusLogDaoConfig = daoConfigMap.get(EntityQuotationStatusLogDao.class).clone();
        entityQuotationStatusLogDaoConfig.initIdentityScope(type);

        entityBigCustomerDaoConfig = daoConfigMap.get(EntityBigCustomerDao.class).clone();
        entityBigCustomerDaoConfig.initIdentityScope(type);

        entityUnSubmitWorkorderDaoConfig = daoConfigMap.get(EntityUnSubmitWorkorderDao.class).clone();
        entityUnSubmitWorkorderDaoConfig.initIdentityScope(type);

        entityUserDao = new EntityUserDao(entityUserDaoConfig, this);
        entityKnowledgeTypeDao = new EntityKnowledgeTypeDao(entityKnowledgeTypeDaoConfig, this);
        entityDeviceDao = new EntityDeviceDao(entityDeviceDaoConfig, this);
        entityWorkorderDao = new EntityWorkorderDao(entityWorkorderDaoConfig, this);
        entityVillageDao = new EntityVillageDao(entityVillageDaoConfig, this);
        entityFactoryCodeDao = new EntityFactoryCodeDao(entityFactoryCodeDaoConfig, this);
        entityRepairDao = new EntityRepairDao(entityRepairDaoConfig, this);
        entityMaintainDao = new EntityMaintainDao(entityMaintainDaoConfig, this);
        entityWorkorderPushLogDao = new EntityWorkorderPushLogDao(entityWorkorderPushLogDaoConfig, this);
        entityPauseConfirmDao = new EntityPauseConfirmDao(entityPauseConfirmDaoConfig, this);
        entityCompanyWorkerDao = new EntityCompanyWorkerDao(entityCompanyWorkerDaoConfig, this);
        entityAssistWorkerDao = new EntityAssistWorkerDao(entityAssistWorkerDaoConfig, this);
        entityPauseLogDao = new EntityPauseLogDao(entityPauseLogDaoConfig, this);
        entityWorkOrderPicDao = new EntityWorkOrderPicDao(entityWorkOrderPicDaoConfig, this);
        entityCheckDao = new EntityCheckDao(entityCheckDaoConfig, this);
        entityUserCompanyDao = new EntityUserCompanyDao(entityUserCompanyDaoConfig, this);
        entityUserStaffDao = new EntityUserStaffDao(entityUserStaffDaoConfig, this);
        entitySparePartQuotationDao = new EntitySparePartQuotationDao(entitySparePartQuotationDaoConfig, this);
        entitySparePartQuotationDataDao = new EntitySparePartQuotationDataDao(entitySparePartQuotationDataDaoConfig, this);
        entitySparePartApplyDao = new EntitySparePartApplyDao(entitySparePartApplyDaoConfig, this);
        entitySparePartApplyPicDao = new EntitySparePartApplyPicDao(entitySparePartApplyPicDaoConfig, this);
        entitySparePartApplyDataDao = new EntitySparePartApplyDataDao(entitySparePartApplyDataDaoConfig, this);
        entityQuotationStatusLogDao = new EntityQuotationStatusLogDao(entityQuotationStatusLogDaoConfig, this);
        entityBigCustomerDao = new EntityBigCustomerDao(entityBigCustomerDaoConfig, this);
        entityUnSubmitWorkorderDao = new EntityUnSubmitWorkorderDao(entityUnSubmitWorkorderDaoConfig, this);

        registerDao(EntityUser.class, entityUserDao);
        registerDao(EntityKnowledgeType.class, entityKnowledgeTypeDao);
        registerDao(EntityDevice.class, entityDeviceDao);
        registerDao(EntityWorkorder.class, entityWorkorderDao);
        registerDao(EntityVillage.class, entityVillageDao);
        registerDao(EntityFactoryCode.class, entityFactoryCodeDao);
        registerDao(EntityRepair.class, entityRepairDao);
        registerDao(EntityMaintain.class, entityMaintainDao);
        registerDao(EntityWorkorderPushLog.class, entityWorkorderPushLogDao);
        registerDao(EntityPauseConfirm.class, entityPauseConfirmDao);
        registerDao(EntityCompanyWorker.class, entityCompanyWorkerDao);
        registerDao(EntityAssistWorker.class, entityAssistWorkerDao);
        registerDao(EntityPauseLog.class, entityPauseLogDao);
        registerDao(EntityWorkOrderPic.class, entityWorkOrderPicDao);
        registerDao(EntityCheck.class, entityCheckDao);
        registerDao(EntityUserCompany.class, entityUserCompanyDao);
        registerDao(EntityUserStaff.class, entityUserStaffDao);
        registerDao(EntitySparePartQuotation.class, entitySparePartQuotationDao);
        registerDao(EntitySparePartQuotationData.class, entitySparePartQuotationDataDao);
        registerDao(EntitySparePartApply.class, entitySparePartApplyDao);
        registerDao(EntitySparePartApplyPic.class, entitySparePartApplyPicDao);
        registerDao(EntitySparePartApplyData.class, entitySparePartApplyDataDao);
        registerDao(EntityQuotationStatusLog.class, entityQuotationStatusLogDao);
        registerDao(EntityBigCustomer.class, entityBigCustomerDao);
        registerDao(EntityUnSubmitWorkorder.class, entityUnSubmitWorkorderDao);
    }
    
    public void clear() {
        entityUserDaoConfig.getIdentityScope().clear();
        entityKnowledgeTypeDaoConfig.getIdentityScope().clear();
        entityDeviceDaoConfig.getIdentityScope().clear();
        entityWorkorderDaoConfig.getIdentityScope().clear();
        entityVillageDaoConfig.getIdentityScope().clear();
        entityFactoryCodeDaoConfig.getIdentityScope().clear();
        entityRepairDaoConfig.getIdentityScope().clear();
        entityMaintainDaoConfig.getIdentityScope().clear();
        entityWorkorderPushLogDaoConfig.getIdentityScope().clear();
        entityPauseConfirmDaoConfig.getIdentityScope().clear();
        entityCompanyWorkerDaoConfig.getIdentityScope().clear();
        entityAssistWorkerDaoConfig.getIdentityScope().clear();
        entityPauseLogDaoConfig.getIdentityScope().clear();
        entityWorkOrderPicDaoConfig.getIdentityScope().clear();
        entityCheckDaoConfig.getIdentityScope().clear();
        entityUserCompanyDaoConfig.getIdentityScope().clear();
        entityUserStaffDaoConfig.getIdentityScope().clear();
        entitySparePartQuotationDaoConfig.getIdentityScope().clear();
        entitySparePartQuotationDataDaoConfig.getIdentityScope().clear();
        entitySparePartApplyDaoConfig.getIdentityScope().clear();
        entitySparePartApplyPicDaoConfig.getIdentityScope().clear();
        entitySparePartApplyDataDaoConfig.getIdentityScope().clear();
        entityQuotationStatusLogDaoConfig.getIdentityScope().clear();
        entityBigCustomerDaoConfig.getIdentityScope().clear();
        entityUnSubmitWorkorderDaoConfig.getIdentityScope().clear();
    }

    public EntityUserDao getEntityUserDao() {
        return entityUserDao;
    }

    public EntityKnowledgeTypeDao getEntityKnowledgeTypeDao() {
        return entityKnowledgeTypeDao;
    }

    public EntityDeviceDao getEntityDeviceDao() {
        return entityDeviceDao;
    }

    public EntityWorkorderDao getEntityWorkorderDao() {
        return entityWorkorderDao;
    }

    public EntityVillageDao getEntityVillageDao() {
        return entityVillageDao;
    }

    public EntityFactoryCodeDao getEntityFactoryCodeDao() {
        return entityFactoryCodeDao;
    }

    public EntityRepairDao getEntityRepairDao() {
        return entityRepairDao;
    }

    public EntityMaintainDao getEntityMaintainDao() {
        return entityMaintainDao;
    }

    public EntityWorkorderPushLogDao getEntityWorkorderPushLogDao() {
        return entityWorkorderPushLogDao;
    }

    public EntityPauseConfirmDao getEntityPauseConfirmDao() {
        return entityPauseConfirmDao;
    }

    public EntityCompanyWorkerDao getEntityCompanyWorkerDao() {
        return entityCompanyWorkerDao;
    }

    public EntityAssistWorkerDao getEntityAssistWorkerDao() {
        return entityAssistWorkerDao;
    }

    public EntityPauseLogDao getEntityPauseLogDao() {
        return entityPauseLogDao;
    }

    public EntityWorkOrderPicDao getEntityWorkOrderPicDao() {
        return entityWorkOrderPicDao;
    }

    public EntityCheckDao getEntityCheckDao() {
        return entityCheckDao;
    }

    public EntityUserCompanyDao getEntityUserCompanyDao() {
        return entityUserCompanyDao;
    }

    public EntityUserStaffDao getEntityUserStaffDao() {
        return entityUserStaffDao;
    }

    public EntitySparePartQuotationDao getEntitySparePartQuotationDao() {
        return entitySparePartQuotationDao;
    }

    public EntitySparePartQuotationDataDao getEntitySparePartQuotationDataDao() {
        return entitySparePartQuotationDataDao;
    }

    public EntitySparePartApplyDao getEntitySparePartApplyDao() {
        return entitySparePartApplyDao;
    }

    public EntitySparePartApplyPicDao getEntitySparePartApplyPicDao() {
        return entitySparePartApplyPicDao;
    }

    public EntitySparePartApplyDataDao getEntitySparePartApplyDataDao() {
        return entitySparePartApplyDataDao;
    }

    public EntityQuotationStatusLogDao getEntityQuotationStatusLogDao() {
        return entityQuotationStatusLogDao;
    }

    public EntityBigCustomerDao getEntityBigCustomerDao() {
        return entityBigCustomerDao;
    }

    public EntityUnSubmitWorkorderDao getEntityUnSubmitWorkorderDao() {
        return entityUnSubmitWorkorderDao;
    }

}
