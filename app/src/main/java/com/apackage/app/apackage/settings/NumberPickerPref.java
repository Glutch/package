package com.apackage.app.apackage.settings;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Custom made NumberPicker for preference view.
 */

public class NumberPickerPref extends DialogPreference {

    private NumberPicker numberPicker = null;

    /**
     * Constructor that sets the buttons in dialog.
     * @param context
     * @param attrs
     */
    public NumberPickerPref(Context context, AttributeSet attrs) {
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
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setMaxValue(20);
        numberPicker.setMinValue(1);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            persistInt(numberPicker.getValue());
        }

    }
}
