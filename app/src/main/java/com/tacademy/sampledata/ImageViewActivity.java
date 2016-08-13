package com.tacademy.sampledata;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageViewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    GridView gridView;
    SimpleCursorAdapter cAdapter;

    String[] projection = { MediaStore.Images.Media._ID,
                            MediaStore.Images.Media.DISPLAY_NAME,
                            MediaStore.Images.Media.DATA };
    String sort = MediaStore.Images.Media.DATE_ADDED + " DESC";

    int dataColumnIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        checkPermission();

        gridView = (GridView)findViewById(R.id.gridView);

        String[] from = { MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA };
        int[] to = { R.id.text_name, R.id.image_icon };

        cAdapter = new SimpleCursorAdapter(this, R.layout.view_image_check, null, from, to, 0);
        cAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex == dataColumnIndex) {
                    String path = cursor.getString(columnIndex);
                    ImageView iv = (ImageView)view;
                    Glide.with(ImageViewActivity.this)
                            .load(new File(path))
                            .into(iv);
                    return true;
                }

                return false;
            }
        });

        gridView.setAdapter(cAdapter);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_image_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_finish) {
            SparseBooleanArray array = gridView.getCheckedItemPositions();
            List<String> files = new ArrayList<>();

            for (int i = 0; i < array.size(); i++) {
                int position = array.keyAt(i);

                if (array.get(position)) {
                    Cursor c = (Cursor)gridView.getItemAtPosition(position);
                    String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                    files.add(path);
                }
            }

            Intent intent = new Intent();
            intent.putExtra("files", files.toArray(new String[files.size()]));
            setResult(Activity.RESULT_OK, intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, sort);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        dataColumnIndex = data.getColumnIndex(MediaStore.Images.Media.DATA);
        cAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cAdapter.swapCursor(null);
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
