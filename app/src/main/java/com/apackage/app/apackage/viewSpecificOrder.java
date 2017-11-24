package com.apackage.app.apackage;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apackage.app.apackage.database.DBHelper;
import com.apackage.app.apackage.database.Order;
import com.apackage.app.apackage.settings.SettingsActivity;

public class viewSpecificOrder extends AppCompatActivity {

    private TextView clientID;
    private TextView ordernumber;
    private TextView firstname;
    private TextView lastname;
    private TextView address;
    private TextView postcode;
    private TextView city;
    private TextView weight;
    private TextView price;
    private TextView deliverydate;
    private TextView deliverystatus;
    private Order order;
    private DBHelper dbHelper;
    private Button deliveredBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_specific_order);

        Intent intent = getIntent();
        long id = intent.getLongExtra("ID", -1);

        dbHelper = new DBHelper(this);
        order = dbHelper.getByOrderId((int) id);

        clientID = findViewById(R.id.clientID);
        clientID.setText("" + order.prize);
        ordernumber = findViewById(R.id.ordernumber);
        ordernumber.setText("" + order.orderId);
        firstname = findViewById(R.id.firstname);
        firstname.setText(order.clientName);
        lastname = findViewById(R.id.lastname);
        lastname.setText("?????"); // todo fix last name?
        address = findViewById(R.id.address);
        address.setText(order.address);
        postcode = findViewById(R.id.postcode);
        postcode.setText("" + order.postalCode);
        city = findViewById(R.id.city);
        city.setText(order.postalTown);
        weight = findViewById(R.id.weight);
        weight.setText("" + order.weight);
        price = findViewById(R.id.price);
        price.setText("" + order.prize);
        deliverydate = findViewById(R.id.deliverydate);
        deliverydate.setText(order.deliveryTime);
        deliverystatus = findViewById(R.id.deliverystatus);
        deliverystatus.setText("" + order.delivered);
        deliveredBtn = findViewById(R.id.button2);
        if (order.delivered)
            deliveredBtn.setVisibility(View.GONE);
    }

    public void onClickDelivered(View view) {
        sendSMS();

        dbHelper.markAsDelivered(order);
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void sendSMS() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) ==
                    PackageManager.PERMISSION_DENIED)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        } else if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE}, 1);
        }

        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String nr = sharedPreferences.getString(SettingsActivity.KEY_PHONENUMBER, "");
            String message = getString(R.string.order) + " " + order.orderId + " " + getString(R.string.delivered) + ".";
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(nr, "null", message, null, null);

        } catch (IllegalArgumentException e) {
            Toast toast = new Toast(this);
            toast.makeText(this, "Sms not sent! Check phonenumber.", Toast.LENGTH_SHORT).show();
        }catch (SecurityException e){
            Toast toast = new Toast(this);
            toast.makeText(this, "Sms not sent! Needs permissions!", Toast.LENGTH_SHORT).show();
        }
    }


}
