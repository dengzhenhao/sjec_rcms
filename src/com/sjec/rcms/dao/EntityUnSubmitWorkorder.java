package com.sjec.rcms.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ENTITY_UN_SUBMIT_WORKORDER.
 */
public class EntityUnSubmitWorkorder {

    public String InnerID;
    public Integer Type;
    public String Status;
    public String Latitude;
    public String Longitude;
    public String Remark;
    public String ResultType;
    public String IsNeedAbarbeitung;
    public String UserID;

    public EntityUnSubmitWorkorder() {
    }

    public EntityUnSubmitWorkorder(String InnerID) {
        this.InnerID = InnerID;
    }

    public EntityUnSubmitWorkorder(String InnerID, Integer Type, String Status, String Latitude, String Longitude, String Remark, String ResultType, String IsNeedAbarbeitung, String UserID) {
        this.InnerID = InnerID;
        this.Type = Type;
        this.Status = Status;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Remark = Remark;
        this.ResultType = ResultType;
        this.IsNeedAbarbeitung = IsNeedAbarbeitung;
        this.UserID = UserID;
    }

    public String getInnerID() {
        return InnerID;
    }

    public void setInnerID(String InnerID) {
        this.InnerID = InnerID;
    }

    public Integer getType() {
        return Type;
    }

    public void setType(Integer Type) {
        this.Type = Type;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getResultType() {
        return ResultType;
    }

    public void setResultType(String ResultType) {
        this.ResultType = ResultType;
    }

    public String getIsNeedAbarbeitung() {
        return IsNeedAbarbeitung;
    }

    public void setIsNeedAbarbeitung(String IsNeedAbarbeitung) {
        this.IsNeedAbarbeitung = IsNeedAbarbeitung;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

}
