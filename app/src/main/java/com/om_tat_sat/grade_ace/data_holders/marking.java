package com.om_tat_sat.grade_ace.data_holders;

public class marking {
    String name;
    Integer theory_marks;
    Integer practical_marks;

    public marking(String name, Integer theory_marks, Integer practical_marks) {
        this.name = name;
        this.theory_marks = theory_marks;
        this.practical_marks = practical_marks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTheory_marks() {
        return theory_marks;
    }

    public Integer getPractical_marks() {
        return practical_marks;
    }

}
