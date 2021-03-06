package com.sjec.rcms.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.sjec.rcms.dao.EntitySparePartQuotation;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ENTITY_SPARE_PART_QUOTATION.
*/
public class EntitySparePartQuotationDao extends AbstractDao<EntitySparePartQuotation, Void> {

    public static final String TABLENAME = "ENTITY_SPARE_PART_QUOTATION";

    /**
     * Properties of entity EntitySparePartQuotation.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property ID = new Property(0, Integer.class, "ID", false, "ID");
        public final static Property Owner = new Property(1, String.class, "Owner", false, "OWNER");
        public final static Property Quotation_Name = new Property(2, String.class, "Quotation_Name", false, "QUOTATION__NAME");
        public final static Property Status = new Property(3, String.class, "Status", false, "STATUS");
        public final static Property Quotation_Type = new Property(4, String.class, "Quotation_Type", false, "QUOTATION__TYPE");
        public final static Property Creator = new Property(5, String.class, "Creator", false, "CREATOR");
        public final static Property CreateTime = new Property(6, String.class, "CreateTime", false, "CREATE_TIME");
        public final static Property CreateIP = new Property(7, String.class, "CreateIP", false, "CREATE_IP");
        public final static Property Updater = new Property(8, String.class, "Updater", false, "UPDATER");
        public final static Property UpdateTime = new Property(9, String.class, "UpdateTime", false, "UPDATE_TIME");
        public final static Property UpdateIP = new Property(10, String.class, "UpdateIP", false, "UPDATE_IP");
        public final static Property Version_Code = new Property(11, String.class, "Version_Code", false, "VERSION__CODE");
        public final static Property Check_Status = new Property(12, String.class, "Check_Status", false, "CHECK__STATUS");
        public final static Property Billing_Status = new Property(13, String.class, "Billing_Status", false, "BILLING__STATUS");
        public final static Property Proceeds_Status = new Property(14, String.class, "Proceeds_Status", false, "PROCEEDS__STATUS");
        public final static Property Total_Amount = new Property(15, String.class, "Total_Amount", false, "TOTAL__AMOUNT");
        public final static Property Total_Amount_Discount = new Property(16, String.class, "Total_Amount_Discount", false, "TOTAL__AMOUNT__DISCOUNT");
        public final static Property Discount = new Property(17, String.class, "Discount", false, "DISCOUNT");
        public final static Property SourceType = new Property(18, String.class, "SourceType", false, "SOURCE_TYPE");
        public final static Property Person_Amount = new Property(19, String.class, "Person_Amount", false, "PERSON__AMOUNT");
        public final static Property Delivery_Amount = new Property(20, String.class, "Delivery_Amount", false, "DELIVERY__AMOUNT");
        public final static Property Check_Amount = new Property(21, String.class, "Check_Amount", false, "CHECK__AMOUNT");
        public final static Property CompanyType = new Property(22, String.class, "CompanyType", false, "COMPANY_TYPE");
        public final static Property Source = new Property(23, String.class, "Source", false, "SOURCE");
        public final static Property CompanyID = new Property(24, String.class, "CompanyID", false, "COMPANY_ID");
        public final static Property BigCustomer_ID = new Property(25, String.class, "BigCustomer_ID", false, "BIG_CUSTOMER__ID");
        public final static Property OrderNum = new Property(26, String.class, "OrderNum", false, "ORDER_NUM");
        public final static Property MaintenanceType = new Property(27, String.class, "MaintenanceType", false, "MAINTENANCE_TYPE");
        public final static Property Use_Status = new Property(28, String.class, "Use_Status", false, "USE__STATUS");
        public final static Property DeviceNo = new Property(29, String.class, "DeviceNo", false, "DEVICE_NO");
        public final static Property ProgramName = new Property(30, String.class, "ProgramName", false, "PROGRAM_NAME");
    };


    public EntitySparePartQuotationDao(DaoConfig config) {
        super(config);
    }
    
    public EntitySparePartQuotationDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ENTITY_SPARE_PART_QUOTATION' (" + //
                "'ID' INTEGER," + // 0: ID
                "'OWNER' TEXT," + // 1: Owner
                "'QUOTATION__NAME' TEXT," + // 2: Quotation_Name
                "'STATUS' TEXT," + // 3: Status
                "'QUOTATION__TYPE' TEXT," + // 4: Quotation_Type
                "'CREATOR' TEXT," + // 5: Creator
                "'CREATE_TIME' TEXT," + // 6: CreateTime
                "'CREATE_IP' TEXT," + // 7: CreateIP
                "'UPDATER' TEXT," + // 8: Updater
                "'UPDATE_TIME' TEXT," + // 9: UpdateTime
                "'UPDATE_IP' TEXT," + // 10: UpdateIP
                "'VERSION__CODE' TEXT," + // 11: Version_Code
                "'CHECK__STATUS' TEXT," + // 12: Check_Status
                "'BILLING__STATUS' TEXT," + // 13: Billing_Status
                "'PROCEEDS__STATUS' TEXT," + // 14: Proceeds_Status
                "'TOTAL__AMOUNT' TEXT," + // 15: Total_Amount
                "'TOTAL__AMOUNT__DISCOUNT' TEXT," + // 16: Total_Amount_Discount
                "'DISCOUNT' TEXT," + // 17: Discount
                "'SOURCE_TYPE' TEXT," + // 18: SourceType
                "'PERSON__AMOUNT' TEXT," + // 19: Person_Amount
                "'DELIVERY__AMOUNT' TEXT," + // 20: Delivery_Amount
                "'CHECK__AMOUNT' TEXT," + // 21: Check_Amount
                "'COMPANY_TYPE' TEXT," + // 22: CompanyType
                "'SOURCE' TEXT," + // 23: Source
                "'COMPANY_ID' TEXT," + // 24: CompanyID
                "'BIG_CUSTOMER__ID' TEXT," + // 25: BigCustomer_ID
                "'ORDER_NUM' TEXT," + // 26: OrderNum
                "'MAINTENANCE_TYPE' TEXT," + // 27: MaintenanceType
                "'USE__STATUS' TEXT," + // 28: Use_Status
                "'DEVICE_NO' TEXT," + // 29: DeviceNo
                "'PROGRAM_NAME' TEXT);"); // 30: ProgramName
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ENTITY_SPARE_PART_QUOTATION'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, EntitySparePartQuotation entity) {
        stmt.clearBindings();
 
        Integer ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
 
        String Owner = entity.getOwner();
        if (Owner != null) {
            stmt.bindString(2, Owner);
        }
 
        String Quotation_Name = entity.getQuotation_Name();
        if (Quotation_Name != null) {
            stmt.bindString(3, Quotation_Name);
        }
 
        String Status = entity.getStatus();
        if (Status != null) {
            stmt.bindString(4, Status);
        }
 
        String Quotation_Type = entity.getQuotation_Type();
        if (Quotation_Type != null) {
            stmt.bindString(5, Quotation_Type);
        }
 
        String Creator = entity.getCreator();
        if (Creator != null) {
            stmt.bindString(6, Creator);
        }
 
        String CreateTime = entity.getCreateTime();
        if (CreateTime != null) {
            stmt.bindString(7, CreateTime);
        }
 
        String CreateIP = entity.getCreateIP();
        if (CreateIP != null) {
            stmt.bindString(8, CreateIP);
        }
 
        String Updater = entity.getUpdater();
        if (Updater != null) {
            stmt.bindString(9, Updater);
        }
 
        String UpdateTime = entity.getUpdateTime();
        if (UpdateTime != null) {
            stmt.bindString(10, UpdateTime);
        }
 
        String UpdateIP = entity.getUpdateIP();
        if (UpdateIP != null) {
            stmt.bindString(11, UpdateIP);
        }
 
        String Version_Code = entity.getVersion_Code();
        if (Version_Code != null) {
            stmt.bindString(12, Version_Code);
        }
 
        String Check_Status = entity.getCheck_Status();
        if (Check_Status != null) {
            stmt.bindString(13, Check_Status);
        }
 
        String Billing_Status = entity.getBilling_Status();
        if (Billing_Status != null) {
            stmt.bindString(14, Billing_Status);
        }
 
        String Proceeds_Status = entity.getProceeds_Status();
        if (Proceeds_Status != null) {
            stmt.bindString(15, Proceeds_Status);
        }
 
        String Total_Amount = entity.getTotal_Amount();
        if (Total_Amount != null) {
            stmt.bindString(16, Total_Amount);
        }
 
        String Total_Amount_Discount = entity.getTotal_Amount_Discount();
        if (Total_Amount_Discount != null) {
            stmt.bindString(17, Total_Amount_Discount);
        }
 
        String Discount = entity.getDiscount();
        if (Discount != null) {
            stmt.bindString(18, Discount);
        }
 
        String SourceType = entity.getSourceType();
        if (SourceType != null) {
            stmt.bindString(19, SourceType);
        }
 
        String Person_Amount = entity.getPerson_Amount();
        if (Person_Amount != null) {
            stmt.bindString(20, Person_Amount);
        }
 
        String Delivery_Amount = entity.getDelivery_Amount();
        if (Delivery_Amount != null) {
            stmt.bindString(21, Delivery_Amount);
        }
 
        String Check_Amount = entity.getCheck_Amount();
        if (Check_Amount != null) {
            stmt.bindString(22, Check_Amount);
        }
 
        String CompanyType = entity.getCompanyType();
        if (CompanyType != null) {
            stmt.bindString(23, CompanyType);
        }
 
        String Source = entity.getSource();
        if (Source != null) {
            stmt.bindString(24, Source);
        }
 
        String CompanyID = entity.getCompanyID();
        if (CompanyID != null) {
            stmt.bindString(25, CompanyID);
        }
 
        String BigCustomer_ID = entity.getBigCustomer_ID();
        if (BigCustomer_ID != null) {
            stmt.bindString(26, BigCustomer_ID);
        }
 
        String OrderNum = entity.getOrderNum();
        if (OrderNum != null) {
            stmt.bindString(27, OrderNum);
        }
 
        String MaintenanceType = entity.getMaintenanceType();
        if (MaintenanceType != null) {
            stmt.bindString(28, MaintenanceType);
        }
 
        String Use_Status = entity.getUse_Status();
        if (Use_Status != null) {
            stmt.bindString(29, Use_Status);
        }
 
        String DeviceNo = entity.getDeviceNo();
        if (DeviceNo != null) {
            stmt.bindString(30, DeviceNo);
        }
 
        String ProgramName = entity.getProgramName();
        if (ProgramName != null) {
            stmt.bindString(31, ProgramName);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public EntitySparePartQuotation readEntity(Cursor cursor, int offset) {
        EntitySparePartQuotation entity = new EntitySparePartQuotation( //
            cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0), // ID
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Owner
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // Quotation_Name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // Status
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // Quotation_Type
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // Creator
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // CreateTime
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // CreateIP
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // Updater
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // UpdateTime
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // UpdateIP
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // Version_Code
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // Check_Status
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // Billing_Status
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // Proceeds_Status
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // Total_Amount
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // Total_Amount_Discount
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // Discount
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // SourceType
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // Person_Amount
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // Delivery_Amount
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // Check_Amount
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // CompanyType
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // Source
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // CompanyID
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25), // BigCustomer_ID
            cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26), // OrderNum
            cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27), // MaintenanceType
            cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28), // Use_Status
            cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29), // DeviceNo
            cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30) // ProgramName
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, EntitySparePartQuotation entity, int offset) {
        entity.setID(cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0));
        entity.setOwner(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setQuotation_Name(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setStatus(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setQuotation_Type(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCreator(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCreateTime(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCreateIP(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setUpdater(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setUpdateTime(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setUpdateIP(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setVersion_Code(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setCheck_Status(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setBilling_Status(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setProceeds_Status(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setTotal_Amount(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setTotal_Amount_Discount(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setDiscount(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setSourceType(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setPerson_Amount(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setDelivery_Amount(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setCheck_Amount(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setCompanyType(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setSource(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setCompanyID(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setBigCustomer_ID(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
        entity.setOrderNum(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
        entity.setMaintenanceType(cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27));
        entity.setUse_Status(cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28));
        entity.setDeviceNo(cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29));
        entity.setProgramName(cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(EntitySparePartQuotation entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(EntitySparePartQuotation entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
