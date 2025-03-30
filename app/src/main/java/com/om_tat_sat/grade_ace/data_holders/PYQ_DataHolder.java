package com.om_tat_sat.grade_ace.data_holders;

public class PYQ_DataHolder {
    String name,semester,message,targeted,parentName, parentSemester;
    public PYQ_DataHolder(String message, String name, String semester, String targeted, String parentName, String parentSemester) {
        this.message = message;
        this.name = name;
        this.semester = semester;
        this.targeted = targeted;
        this.parentName = parentName;
        this.parentSemester = parentSemester;
    }

    public String getParentSemester() {
        return parentSemester;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getSemester() {
        return semester;
    }
    public String getParentName() {
        return parentName;
    }
    public String getTargeted() {
        return targeted;
    }
}
