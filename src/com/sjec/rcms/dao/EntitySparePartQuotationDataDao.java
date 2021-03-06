package com.sjec.rcms.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.sjec.rcms.dao.EntitySparePartQuotationData;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ENTITY_SPARE_PART_QUOTATION_DATA.
*/
public class EntitySparePartQuotationDataDao extends AbstractDao<EntitySparePartQuotationData, Void> {

    public static final String TABLENAME = "ENTITY_SPARE_PART_QUOTATION_DATA";

    /**
     * Properties of entity EntitySparePartQuotationData.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property ID = new Property(0, Integer.class, "ID", false, "ID");
        public final static Property Quotation_ID = new Property(1, String.class, "Quotation_ID", false, "QUOTATION__ID");
        public final static Property Owner = new Property(2, String.class, "Owner", false, "OWNER");
        public final static Property Status = new Property(3, String.class, "Status", false, "STATUS");
        public final static Property Data_ID = new Property(4, String.class, "Data_ID", false, "DATA__ID");
        public final static Property Data_Type = new Property(5, String.class, "Data_Type", false, "DATA__TYPE");
        public final static Property Draw_Code = new Property(6, String.class, "Draw_Code", false, "DRAW__CODE");
        public final static Property Product_Code = new Property(7, String.class, "Product_Code", false, "PRODUCT__CODE");
        public final static Property Code = new Property(8, String.class, "Code", false, "CODE");
        public final static Property FakeCode = new Property(9, String.class, "FakeCode", false, "FAKE_CODE");
        public final static Property Name = new Property(10, String.class, "Name", false, "NAME");
        public final static Property SPCF = new Property(11, String.class, "SPCF", false, "SPCF");
        public final static Property Unit = new Property(12, String.class, "Unit", false, "UNIT");
        public final static Property Use_Status = new Property(13, String.class, "Use_Status", false, "USE__STATUS");
        public final static Property Purchase_Price = new Property(14, String.class, "Purchase_Price", false, "PURCHASE__PRICE");
        public final static Property Make_Model = new Property(15, String.class, "Make_Model", false, "MAKE__MODEL");
        public final static Property Make_Company = new Property(16, String.class, "Make_Company", false, "MAKE__COMPANY");
        public final static Property Weight = new Property(17, String.class, "Weight", false, "WEIGHT");
        public final static Property Size = new Property(18, String.class, "Size", false, "SIZE");
        public final static Property Delivery_Period = new Property(19, String.class, "Delivery_Period", false, "DELIVERY__PERIOD");
        public final static Property Remark = new Property(20, String.class, "Remark", false, "REMARK");
        public final static Property Finance_Price = new Property(21, String.class, "Finance_Price", false, "FINANCE__PRICE");
        public final static Property Discount = new Property(22, String.class, "Discount", false, "DISCOUNT");
        public final static Property Discount_Price = new Property(23, String.class, "Discount_Price", false, "DISCOUNT__PRICE");
        public final static Property Quantity = new Property(24, String.class, "Quantity", false, "QUANTITY");
        public final static Property Total_Price = new Property(25, String.class, "Total_Price", false, "TOTAL__PRICE");
        public final static Property Creator = new Property(26, String.class, "Creator", false, "CREATOR");
        public final static Property CreateTime = new Property(27, String.class, "CreateTime", false, "CREATE_TIME");
        public final static Property CreateIP = new Property(28, String.class, "CreateIP", false, "CREATE_IP");
        public final static Property Updater = new Property(29, String.class, "Updater", false, "UPDATER");
        public final static Property UpdateTime = new Property(30, String.class, "UpdateTime", false, "UPDATE_TIME");
        public final static Property UpdateIP = new Property(31, String.class, "UpdateIP", false, "UPDATE_IP");
        public final static Property DeliveryTime = new Property(32, String.class, "DeliveryTime", false, "DELIVERY_TIME");
        public final static Property SetupTime = new Property(33, String.class, "SetupTime", false, "SETUP_TIME");
    };


    public EntitySparePartQuotationDataDao(DaoConfig config) {
        super(config);
    }
    
    public EntitySparePartQuotationDataDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ENTITY_SPARE_PART_QUOTATION_DATA' (" + //
                "'ID' INTEGER," + // 0: ID
                "'QUOTATION__ID' TEXT," + // 1: Quotation_ID
                "'OWNER' TEXT," + // 2: Owner
                "'STATUS' TEXT," + // 3: Status
                "'DATA__ID' TEXT," + // 4: Data_ID
                "'DATA__TYPE' TEXT," + // 5: Data_Type
                "'DRAW__CODE' TEXT," + // 6: Draw_Code
                "'PRODUCT__CODE' TEXT," + // 7: Product_Code
                "'CODE' TEXT," + // 8: Code
                "'FAKE_CODE' TEXT," + // 9: FakeCode
                "'NAME' TEXT," + // 10: Name
                "'SPCF' TEXT," + // 11: SPCF
                "'UNIT' TEXT," + // 12: Unit
                "'USE__STATUS' TEXT," + // 13: Use_Status
                "'PURCHASE__PRICE' TEXT," + // 14: Purchase_Price
                "'MAKE__MODEL' TEXT," + // 15: Make_Model
                "'MAKE__COMPANY' TEXT," + // 16: Make_Company
                "'WEIGHT' TEXT," + // 17: Weight
                "'SIZE' TEXT," + // 18: Size
                "'DELIVERY__PERIOD' TEXT," + // 19: Delivery_Period
                "'REMARK' TEXT," + // 20: Remark
                "'FINANCE__PRICE' TEXT," + // 21: Finance_Price
                "'DISCOUNT' TEXT," + // 22: Discount
                "'DISCOUNT__PRICE' TEXT," + // 23: Discount_Price
                "'QUANTITY' TEXT," + // 24: Quantity
                "'TOTAL__PRICE' TEXT," + // 25: Total_Price
                "'CREATOR' TEXT," + // 26: Creator
                "'CREATE_TIME' TEXT," + // 27: CreateTime
                "'CREATE_IP' TEXT," + // 28: CreateIP
                "'UPDATER' TEXT," + // 29: Updater
                "'UPDATE_TIME' TEXT," + // 30: UpdateTime
                "'UPDATE_IP' TEXT," + // 31: UpdateIP
                "'DELIVERY_TIME' TEXT," + // 32: DeliveryTime
                "'SETUP_TIME' TEXT);"); // 33: SetupTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ENTITY_SPARE_PART_QUOTATION_DATA'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, EntitySparePartQuotationData entity) {
        stmt.clearBindings();
 
        Integer ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
 
        String Quotation_ID = entity.getQuotation_ID();
        if (Quotation_ID != null) {
            stmt.bindString(2, Quotation_ID);
        }
 
        String Owner = entity.getOwner();
        if (Owner != null) {
            stmt.bindString(3, Owner);
        }
 
        String Status = entity.getStatus();
        if (Status != null) {
            stmt.bindString(4, Status);
        }
 
        String Data_ID = entity.getData_ID();
        if (Data_ID != null) {
            stmt.bindString(5, Data_ID);
        }
 
        String Data_Type = entity.getData_Type();
        if (Data_Type != null) {
            stmt.bindString(6, Data_Type);
        }
 
        String Draw_Code = entity.getDraw_Code();
        if (Draw_Code != null) {
            stmt.bindString(7, Draw_Code);
        }
 
        String Product_Code = entity.getProduct_Code();
        if (Product_Code != null) {
            stmt.bindString(8, Product_Code);
        }
 
        String Code = entity.getCode();
        if (Code != null) {
            stmt.bindString(9, Code);
        }
 
        String FakeCode = entity.getFakeCode();
        if (FakeCode != null) {
            stmt.bindString(10, FakeCode);
        }
 
        String Name = entity.getName();
        if (Name != null) {
            stmt.bindString(11, Name);
        }
 
        String SPCF = entity.getSPCF();
        if (SPCF != null) {
            stmt.bindString(12, SPCF);
        }
 
        String Unit = entity.getUnit();
        if (Unit != null) {
            stmt.bindString(13, Unit);
        }
 
        String Use_Status = entity.getUse_Status();
        if (Use_Status != null) {
            stmt.bindString(14, Use_Status);
        }
 
        String Purchase_Price = entity.getPurchase_Price();
        if (Purchase_Price != null) {
            stmt.bindString(15, Purchase_Price);
        }
 
        String Make_Model = entity.getMake_Model();
        if (Make_Model != null) {
            stmt.bindString(16, Make_Model);
        }
 
        String Make_Company = entity.getMake_Company();
        if (Make_Company != null) {
            stmt.bindString(17, Make_Company);
        }
 
        String Weight = entity.getWeight();
        if (Weight != null) {
            stmt.bindString(18, Weight);
        }
 
        String Size = entity.getSize();
        if (Size != null) {
            stmt.bindString(19, Size);
        }
 
        String Delivery_Period = entity.getDelivery_Period();
        if (Delivery_Period != null) {
            stmt.bindString(20, Delivery_Period);
        }
 
        String Remark = entity.getRemark();
        if (Remark != null) {
            stmt.bindString(21, Remark);
        }
 
        String Finance_Price = entity.getFinance_Price();
        if (Finance_Price != null) {
            stmt.bindString(22, Finance_Price);
        }
 
        String Discount = entity.getDiscount();
        if (Discount != null) {
            stmt.bindString(23, Discount);
        }
 
        String Discount_Price = entity.getDiscount_Price();
        if (Discount_Price != null) {
            stmt.bindString(24, Discount_Price);
        }
 
        String Quantity = entity.getQuantity();
        if (Quantity != null) {
            stmt.bindString(25, Quantity);
        }
 
        String Total_Price = entity.getTotal_Price();
        if (Total_Price != null) {
            stmt.bindString(26, Total_Price);
        }
 
        String Creator = entity.getCreator();
        if (Creator != null) {
            stmt.bindString(27, Creator);
        }
 
        String CreateTime = entity.getCreateTime();
        if (CreateTime != null) {
            stmt.bindString(28, CreateTime);
        }
 
        String CreateIP = entity.getCreateIP();
        if (CreateIP != null) {
            stmt.bindString(29, CreateIP);
        }
 
        String Updater = entity.getUpdater();
        if (Updater != null) {
            stmt.bindString(30, Updater);
        }
 
        String UpdateTime = entity.getUpdateTime();
        if (UpdateTime != null) {
            stmt.bindString(31, UpdateTime);
        }
 
        String UpdateIP = entity.getUpdateIP();
        if (UpdateIP != null) {
            stmt.bindString(32, UpdateIP);
        }
 
        String DeliveryTime = entity.getDeliveryTime();
        if (DeliveryTime != null) {
            stmt.bindString(33, DeliveryTime);
        }
 
        String SetupTime = entity.getSetupTime();
        if (SetupTime != null) {
            stmt.bindString(34, SetupTime);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public EntitySparePartQuotationData readEntity(Cursor cursor, int offset) {
        EntitySparePartQuotationData entity = new EntitySparePartQuotationData( //
            cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0), // ID
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Quotation_ID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // Owner
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // Status
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // Data_ID
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // Data_Type
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // Draw_Code
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // Product_Code
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // Code
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // FakeCode
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // Name
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // SPCF
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // Unit
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // Use_Status
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // Purchase_Price
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // Make_Model
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // Make_Company
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // Weight
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // Size
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // Delivery_Period
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // Remark
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // Finance_Price
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // Discount
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // Discount_Price
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // Quantity
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25), // Total_Price
            cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26), // Creator
            cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27), // CreateTime
            cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28), // CreateIP
            cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29), // Updater
            cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30), // UpdateTime
            cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31), // UpdateIP
            cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32), // DeliveryTime
            cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33) // SetupTime
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, EntitySparePartQuotationData entity, int offset) {
        entity.setID(cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0));
        entity.setQuotation_ID(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setOwner(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setStatus(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setData_ID(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setData_Type(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDraw_Code(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setProduct_Code(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setCode(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setFakeCode(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setName(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setSPCF(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setUnit(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setUse_Status(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setPurchase_Price(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setMake_Model(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setMake_Company(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setWeight(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setSize(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setDelivery_Period(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setRemark(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setFinance_Price(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setDiscount(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setDiscount_Price(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setQuantity(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setTotal_Price(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
        entity.setCreator(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
        entity.setCreateTime(cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27));
        entity.setCreateIP(cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28));
        entity.setUpdater(cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29));
        entity.setUpdateTime(cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30));
        entity.setUpdateIP(cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31));
        entity.setDeliveryTime(cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32));
        entity.setSetupTime(cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(EntitySparePartQuotationData entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(EntitySparePartQuotationData entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
