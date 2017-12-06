package com.apackage.app.apackage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.apackage.app.apackage.database.DBHelper;
import com.apackage.app.apackage.database.Order;
import com.apackage.app.apackage.settings.SettingsActivity;

/**
 * Activity that shows orders in a ListView.
 */
public class ViewOrders extends AppCompatActivity {

    private DBHelper dbHelper;
    private ListView listView;
    private OrderAdapter orderAdapter;
    private boolean onOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.READ_PHONE_STATE}, 1);

        onOrder = true;

        listView = findViewById(R.id.listView);
        dbHelper = new DBHelper(this);
        //dbHelper.resetTheMF();

        orderAdapter = new OrderAdapter(this, dbHelper.getOrdersDelivered(0));
        listView.setAdapter(orderAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), ViewSpecificOrder.class);
                intent.putExtra("ORDERID", id);
                intent.putExtra("POSITION", position);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.generateOrders:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                int amount = sharedPreferences.getInt(SettingsActivity.KEY_GENERATEORDERS, 1);
                dbHelper.addMultipleOrders(RandFakeOrder.randomOrd(amount));
                if (onOrder)
                    onClickOrders(findViewById(R.id.ordersBtn));
                break;
            case R.id.preferences:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.barCodeScanner:
                Intent intent1 = new Intent(this, BarCodeScanner.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * When button orders clicked a list of orders, that is not delivered, is
     * fetched from database and inserted in the adapter.
     * @param view
     */
    public void onClickOrders(View view) {
        orderAdapter.setOrderList(dbHelper.getOrdersDelivered(0));
        onOrder = true;
    }

    /**
     * When button history clicked a list of orders, that is delivered, is
     * fetched from database and inserted in the adapter.
     * @param view
     */
    public void onClickHistory(View view) {
        orderAdapter.setOrderList(dbHelper.getOrdersDelivered(1));
        onOrder = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            orderAdapter.removeOrder((Order) orderAdapter.getItem(data.getIntExtra("POSITION", -1)));
        }

    }
}
