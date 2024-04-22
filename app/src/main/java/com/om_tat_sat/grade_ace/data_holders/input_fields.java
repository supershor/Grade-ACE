package com.om_tat_sat.grade_ace.data_holders;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class input_fields {
    EditText theory_marks;
    EditText practical_marks;
    TextView name;

    public LinearLayout getLayout() {
        return layout;
    }

    LinearLayout layout;

    public input_fields(LinearLayout layout, EditText theory_marks, EditText practical_marks, TextView name) {
        this.theory_marks = theory_marks;
        this.practical_marks = practical_marks;
        this.name = name;
        this.layout=layout;
    }
    public EditText getTheory_marks() {
        return theory_marks;
    }

    public EditText getPractical_marks() {
        return practical_marks;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }
}
