package com.sjec.rcms.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ENTITY_QUOTATION_STATUS_LOG.
 */
public class EntityQuotationStatusLog {

    public Integer ID;
    public Integer SourceStatus;
    public Integer TargetStatus;
    public Integer QuotationID;
    public String CreateTime;
    public String Creator;
    public String CreateIP;
    public String UpdateTime;
    public String Updater;
    public String UpdateIP;

    public EntityQuotationStatusLog() {
    }

    public EntityQuotationStatusLog(Integer ID, Integer SourceStatus, Integer TargetStatus, Integer QuotationID, String CreateTime, String Creator, String CreateIP, String UpdateTime, String Updater, String UpdateIP) {
        this.ID = ID;
        this.SourceStatus = SourceStatus;
        this.TargetStatus = TargetStatus;
        this.QuotationID = QuotationID;
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

    public Integer getSourceStatus() {
        return SourceStatus;
    }

    public void setSourceStatus(Integer SourceStatus) {
        this.SourceStatus = SourceStatus;
    }

    public Integer getTargetStatus() {
        return TargetStatus;
    }

    public void setTargetStatus(Integer TargetStatus) {
        this.TargetStatus = TargetStatus;
    }

    public Integer getQuotationID() {
        return QuotationID;
    }

    public void setQuotationID(Integer QuotationID) {
        this.QuotationID = QuotationID;
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
