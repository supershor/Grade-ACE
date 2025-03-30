package com.om_tat_sat.grade_ace.data_holders;

public class Study_Prep_Material_Holder {
    String name;
    String message1;
    String message2;
    String message3;
    String message1Name;
    String message2Name;
    String message3Name;
    String semester;
    public Study_Prep_Material_Holder(String message1, String message1Name, String message2, String message2Name, String message3, String message3Name, String name, String semester) {
        this.message1 = message1;
        this.message1Name = message1Name;
        this.message2 = message2;
        this.message2Name = message2Name;
        this.message3 = message3;
        this.message3Name = message3Name;
        this.name = name;
        this.semester=semester;
    }
    public Study_Prep_Material_Holder(String name, String message1Name){
        this.name = name;
        this.message1Name = message1Name;
        this.message1 = "";
        this.message2 = "";
        this.message3 = "";
        this.message2Name = "";
        this.message3Name = "";
    }

    public String getMessage1() {
        return message1;
    }

    public String getMessage1Name() {
        return message1Name;
    }
    public String getSemester(){
        return semester;
    }
    public String getMessage2() {
        return message2;
    }

    public String getMessage2Name() {
        return message2Name;
    }

    public String getMessage3() {
        return message3;
    }

    public String getMessage3Name() {
        return message3Name;
    }

    public String getName() {
        return name;
    }
}
