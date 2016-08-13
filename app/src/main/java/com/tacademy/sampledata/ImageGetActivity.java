package com.tacademy.sampledata;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ImageGetActivity extends AppCompatActivity {

    private static final int RC_GET_IMAGE = 1;
    private static final int RC_SINGLE_IMAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_get);

        checkPermission();

        Button btn = (Button)findViewById(R.id.btn_get_image);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImageGetActivity.this, ImageViewActivity.class);
                startActivityForResult(intent, RC_GET_IMAGE);
            }
        });

        btn = (Button)findViewById(R.id.btn_get_single_image);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");
                startActivityForResult(intent, RC_SINGLE_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GET_IMAGE && resultCode == RESULT_OK) {
            String[] files = data.getStringArrayExtra("files");

            for (String f : files)
                Log.d("ImageFiles", "files : " + f);
        }

        if (requestCode == RC_SINGLE_IMAGE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Cursor c = getContentResolver().query(uri, new String[] {MediaStore.Images.Media.DATA}, null, null, null);

            if (c.moveToNext()) {
                String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                Log.d("Single", "path : " + path);
            }
        }
    }

    private static final int RC_PERMISSION = 100;
    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // dialog...
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION);
                return false;
            }
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RC_PERMISSION && permissions != null) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
