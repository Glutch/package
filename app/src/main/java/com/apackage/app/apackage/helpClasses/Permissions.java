package com.apackage.app.apackage.helpClasses;

import android.content.pm.PackageManager;

/**
 * Created by Petteri Tuononen on 2017-11-24.
 */

public abstract class Permissions {

    private static final int PERMISSION_REQUEST_CAMERA = 1;
    private static final int PERMISSION_FAILED = -1;
    private static final int DIALOGCANCELLED = -2;
    private static final int PERMISSION_GRANTED = 0;
    private static final int NO_REQUEST = -5;


    public static int checkPremis(int requestCode, String permissions[], int[] grantResults) {
        // This is because the dialog was cancelled when we recreated the activity.
        if (permissions.length == 0 || grantResults.length == 0)
            return DIALOGCANCELLED;

        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    return PERMISSION_GRANTED;

                else
                    return PERMISSION_FAILED;

            }
// more premissions?

        }
        return NO_REQUEST;
    }
}
