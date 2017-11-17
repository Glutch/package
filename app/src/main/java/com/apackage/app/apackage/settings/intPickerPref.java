package com.apackage.app.apackage.settings;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by mrx on 2017-11-17.
 */

public class intPickerPref extends DialogPreference {

    private NumberPicker numberPicker = null;

    public intPickerPref(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    @Override
    protected View onCreateDialogView() {
        numberPicker = new NumberPicker(getContext());
        return numberPicker;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        numberPicker.setMaxValue(20);
        numberPicker.setMinValue(1);
        numberPicker.setValue(1);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult){
            persistInt(numberPicker.getValue());
        }

    }
}
