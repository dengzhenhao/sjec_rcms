package com.sjec.rcms.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ENTITY_COMPANY_WORKER.
 */
public class EntityCompanyWorker {

    public String InnerID;
    public String StaffNo;
    public String Name;
    public String Telephone;

    public EntityCompanyWorker() {
    }

    public EntityCompanyWorker(String InnerID, String StaffNo, String Name, String Telephone) {
        this.InnerID = InnerID;
        this.StaffNo = StaffNo;
        this.Name = Name;
        this.Telephone = Telephone;
    }

    public String getInnerID() {
        return InnerID;
    }

    public void setInnerID(String InnerID) {
        this.InnerID = InnerID;
    }

    public String getStaffNo() {
        return StaffNo;
    }

    public void setStaffNo(String StaffNo) {
        this.StaffNo = StaffNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String Telephone) {
        this.Telephone = Telephone;
    }

}