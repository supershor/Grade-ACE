package com.om_tat_sat.grade_ace.Recycler;

public class Item {
    String name;
    String ogpa;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOgpa() {
        return ogpa;
    }

    public String getSem() {
        return sem;
    }


    public Item(String name, String ogpa, String sem) {
        this.name = name;
        this.ogpa = ogpa;
        this.sem = sem;
    }

    String sem;
}
