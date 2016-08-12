package com.tacademy.sampledata;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ContactActivity extends AppCompatActivity {

    EditText inputView;
    ListView listView;
    SimpleCursorAdapter cAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        inputView = (EditText)findViewById(R.id.edit_input);
        listView = (ListView)findViewById(R.id.list_input);

        String[] from = {ContactsContract.Contacts.DISPLAY_NAME };
        int[] to = { android.R.id.text1 };

        cAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to, 0);
        listView.setAdapter(cAdapter);

        if (checkPermission())
            getContacts(null);
    }

    private static final int RC_PERMISSION = 100;
    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                // dialog...
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, RC_PERMISSION);
                return false;
            }
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, RC_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMISSION) {
            if (permissions != null) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String keyword = inputView.getText().toString();
                    getContacts(keyword);
                    return;
                }
            }
            Toast.makeText(this, "need read_contact permission", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    String[] projection = {ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME};
    String selection = "((" + ContactsContract.Contacts.DISPLAY_NAME + " NOT NULL) AND (" +
            ContactsContract.Contacts.DISPLAY_NAME + " != ''))";
    String sort = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

    private void getContacts(String keyword) {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        if (!TextUtils.isEmpty(keyword)) {
            uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(keyword));
        }
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uri, projection, selection, null, sort);
        cAdapter.changeCursor(cursor);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isPermission()) {
            String keyword = inputView.getText().toString();
            getContacts(keyword);
        }
    }

    private boolean isPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }
}
