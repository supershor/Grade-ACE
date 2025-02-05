package com.om_tat_sat.grade_ace.data_holders;

public class ogpa_holder {
    public String name;
    public String ogpa;
    public String sem;
    public String ogpa_type;

    public ogpa_holder(String name, String ogpa, String sem) {
        this.name = name;
        this.ogpa = ogpa;
        this.sem = sem;
    }
    public ogpa_holder(String name, String ogpa, String sem, String ogpa_type) {
        this.name = name;
        this.ogpa = ogpa;
        this.sem = sem;
        this.ogpa_type = ogpa_type;
    }
}
