package com.om_tat_sat.grade_ace.data_holders;

public class PYQ_DataHolder {
    String name,semester,message,targeted;
    public PYQ_DataHolder(String message, String name, String semester, String targeted) {
        this.message = message;
        this.name = name;
        this.semester = semester;
        this.targeted = targeted;
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

    public String getTargeted() {
        return targeted;
    }
}
