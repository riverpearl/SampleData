package com.tacademy.sampledata;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.List;

public class AddressActivity extends AppCompatActivity {

    ListView listView;
    SimpleCursorAdapter cAdapter;

    int phoneIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        listView = (ListView)findViewById(R.id.list_person);

        String[] from = { AddressContract.Address.COLUMN_NAME,
                AddressContract.Address.COLUMN_AGE,
                AddressContract.Address.COLUMN_PHONE,
                AddressContract.Address.COLUMN_ADDRESS };
        int[] to = { R.id.text_name,
                R.id.text_age,
                R.id.text_phone,
                R.id.text_address };

        cAdapter = new SimpleCursorAdapter(this, R.layout.view_person_detail, null, from, to, 0);
        cAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if (i == phoneIndex) {
                    String phone = cursor.getString(i);
                    TextView tv = (TextView)view;
                    StringBuilder sb = new StringBuilder();

                    if (phone.length() > 0)
                        sb.append(phone.charAt(0));

                    for (int j = 0; j < phone.length(); j++)
                        sb.append("*");

                    tv.setText(sb.toString());
                    return true;
                }
                return false;
            }
        });

        listView.setAdapter(cAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Cursor c =  PersonDBManager.getInstance().getPersonCursor(null);
        phoneIndex = c.getColumnIndex(AddressContract.Address.COLUMN_PHONE);
        cAdapter.changeCursor(c);
    }

    @Override
    protected void onStop() {
        super.onStop();

        cAdapter.changeCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_address, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            Intent intent = new Intent(this, AddAddressActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
