package com.sjec.rcms.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ENTITY_KNOWLEDGE_TYPE.
 */
public class EntityKnowledgeType {

    public String InnerID;
    public String ClassCode;
    public String ClassName;
    public String Tag;

    public EntityKnowledgeType() {
    }

    public EntityKnowledgeType(String InnerID, String ClassCode, String ClassName, String Tag) {
        this.InnerID = InnerID;
        this.ClassCode = ClassCode;
        this.ClassName = ClassName;
        this.Tag = Tag;
    }

    public String getInnerID() {
        return InnerID;
    }

    public void setInnerID(String InnerID) {
        this.InnerID = InnerID;
    }

    public String getClassCode() {
        return ClassCode;
    }

    public void setClassCode(String ClassCode) {
        this.ClassCode = ClassCode;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String ClassName) {
        this.ClassName = ClassName;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String Tag) {
        this.Tag = Tag;
    }

}
