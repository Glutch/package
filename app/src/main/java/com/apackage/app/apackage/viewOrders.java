package com.apackage.app.apackage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.apackage.app.apackage.database.DBHelper;
import com.apackage.app.apackage.database.Order;
import com.apackage.app.apackage.settings.SettingsActivity;

import java.util.List;
import java.util.zip.Inflater;

public class viewOrders extends AppCompatActivity {

  private DBHelper dbHelper;
  private ListView listView;
  private OrderAdapter orderAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_orders);

    Toolbar toolbar = findViewById(R.id.toolbar2);
    setSupportActionBar(toolbar);

    listView = findViewById(R.id.listView);
    dbHelper = new DBHelper(this);

    orderAdapter = new OrderAdapter(this, dbHelper.getOrdersDelivered(0));
    listView.setAdapter(orderAdapter);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(view.getContext(), viewSpecificOrder.class);
        intent.putExtra("ID", orderAdapter.getItemId(position));
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
    switch (item.getItemId()){
      case R.id.generateOrders:
        dbHelper.resetTheMF();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int amount = sharedPreferences.getInt(SettingsActivity.KEY_GENERATEORDERS, 1);
        dbHelper.addMultipleOrders(RandFakeOrder.randomOrd(amount));
        break;
      case R.id.preferences:
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    return super.onOptionsItemSelected(item);
  }

  public void onClickOrders(View view){
    orderAdapter.setOrderList(dbHelper.getOrdersDelivered(0));
    orderAdapter.notifyDataSetChanged();
  }

  public void onClickHistory(View view){
    orderAdapter.setOrderList(dbHelper.getOrdersDelivered(1));
    orderAdapter.notifyDataSetChanged();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 1 && resultCode == RESULT_OK){
      orderAdapter.removeOrder((Order) orderAdapter.getItem(data.getIntExtra("POSITION", -1)));
    }

  }
}
