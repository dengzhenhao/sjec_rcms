package com.sjec.rcms.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ENTITY_SPARE_PART_APPLY_PIC.
 */
public class EntitySparePartApplyPic {

    public Integer ID;
    public String PicPath;
    public String App_Quotation_Data_ID;
    public String CreateTime;
    public String Creator;
    public String CreateIP;
    public String UpdateTime;
    public String Updater;
    public String UpdateIP;

    public EntitySparePartApplyPic() {
    }

    public EntitySparePartApplyPic(Integer ID, String PicPath, String App_Quotation_Data_ID, String CreateTime, String Creator, String CreateIP, String UpdateTime, String Updater, String UpdateIP) {
        this.ID = ID;
        this.PicPath = PicPath;
        this.App_Quotation_Data_ID = App_Quotation_Data_ID;
        this.CreateTime = CreateTime;
        this.Creator = Creator;
        this.CreateIP = CreateIP;
        this.UpdateTime = UpdateTime;
        this.Updater = Updater;
        this.UpdateIP = UpdateIP;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getPicPath() {
        return PicPath;
    }

    public void setPicPath(String PicPath) {
        this.PicPath = PicPath;
    }

    public String getApp_Quotation_Data_ID() {
        return App_Quotation_Data_ID;
    }

    public void setApp_Quotation_Data_ID(String App_Quotation_Data_ID) {
        this.App_Quotation_Data_ID = App_Quotation_Data_ID;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String Creator) {
        this.Creator = Creator;
    }

    public String getCreateIP() {
        return CreateIP;
    }

    public void setCreateIP(String CreateIP) {
        this.CreateIP = CreateIP;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }

    public String getUpdater() {
        return Updater;
    }

    public void setUpdater(String Updater) {
        this.Updater = Updater;
    }

    public String getUpdateIP() {
        return UpdateIP;
    }

    public void setUpdateIP(String UpdateIP) {
        this.UpdateIP = UpdateIP;
    }

}
