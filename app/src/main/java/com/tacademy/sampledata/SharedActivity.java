package com.tacademy.sampledata;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SharedActivity extends AppCompatActivity {

    SharedPreferences sPrefs;
    SharedPreferences.Editor sEditor;

    EditText emailView, passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);

        emailView = (EditText)findViewById(R.id.edit_email);
        passwordView = (EditText)findViewById(R.id.edit_password);

        Button btn = (Button)findViewById(R.id.btn_save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailView.getText().toString();
                String password = passwordView.getText().toString();

                SharedPreferenceManager.getInstance().setEmail(email);
                SharedPreferenceManager.getInstance().setPassword(password);
            }
        });

        sPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sEditor = sPrefs.edit();

        emailView.setText(SharedPreferenceManager.getInstance().getEmail());
        passwordView.setText(SharedPreferenceManager.getInstance().getPassword());
    }
}
