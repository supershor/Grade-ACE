package com.om_tat_sat.grade_ace.Recycler;

public class Item {
    String name;
    String ogpa;
    String sem;
    String ogpa_type;

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
    public Item(String name, String ogpa, String sem,String ogpa_type) {
        this.name = name;
        this.ogpa = ogpa;
        this.sem = sem;
        this.ogpa_type=ogpa_type;
    }
}
