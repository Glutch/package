package com.apackage.app.apackage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.apackage.app.apackage.database.DBHelper;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarCodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    public static final int PERMISSION_REQUEST_CAMERA = 1;
    private static final int PREMISSION_FAILED = -1;
    private static final int DIALOGCANCELLED = -2;
    private static final int PREMISSION_GRANTED = 0;
    private static final int NO_REQUEST = -5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

        haveCameraPermission();
    }

    private void haveCameraPermission()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (!(checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED))
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {

        switch (Premissions.checkPremis(requestCode,permissions,grantResults))
        {
            case DIALOGCANCELLED:return;
            case PREMISSION_GRANTED:mScannerView.startCamera(); break;
            case PREMISSION_FAILED:finish();break;
        }


    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        CharSequence text;



        long result = 0;
        if (rawResult.getText().matches("[0-9]+")) {
            result = Long.parseLong(rawResult.getText());

            try {
                DBHelper db = new DBHelper(this);
                db.getByOrderId(result);
            } catch (Exception e){
                text = "Order NOT in Database!";
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                mScannerView.startCamera();
                return;
            }
            mScannerView.stopCamera();
            Intent intent = new Intent(this, viewSpecificOrder.class);
            intent.putExtra("ORDERID", result);
            startActivity(intent);
        }
        else {

            text = "BarCode Contains other then numbers";

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            mScannerView.startCamera();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
