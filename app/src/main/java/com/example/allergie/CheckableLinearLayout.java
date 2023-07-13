package com.example.allergie;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable {

    public CheckableLinearLayout(Context context) {
        super(context);
    }

    public CheckableLinearLayout(Context context, AttributeSet attr){
        super(context, attr);
    }

    @Override
    public void setChecked(boolean b) {
        CheckBox cb = (CheckBox) findViewById(R.id.checkedNum);

        if (cb.isChecked() != b) {
            cb.setChecked(b);
        }
    }

    @Override
    public boolean isChecked() {
        CheckBox cb = (CheckBox) findViewById(R.id.checkedNum);

        return cb.isChecked();
    }

    @Override
    public void toggle() {
        CheckBox cb = (CheckBox) findViewById(R.id.checkedNum);

        setChecked(cb.isChecked() ? false : true);
    }
}
