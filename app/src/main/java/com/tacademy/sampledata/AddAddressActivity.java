package com.tacademy.sampledata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddAddressActivity extends AppCompatActivity {

    EditText nameView, ageView, phoneView, addressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        nameView = (EditText)findViewById(R.id.edit_name);
        ageView = (EditText)findViewById(R.id.edit_age);
        ageView.setText("0");
        phoneView = (EditText)findViewById(R.id.edit_phone);
        addressView = (EditText)findViewById(R.id.edit_address);

        Button btn = (Button)findViewById(R.id.btn_add);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameView.getText().toString();
                int age = Integer.parseInt(ageView.getText().toString());
                String phone = phoneView.getText().toString();
                String address = addressView.getText().toString();

                if (!TextUtils.isEmpty(name) && age > -1 && !TextUtils.isEmpty(phone)&& !TextUtils.isEmpty(address)) {
                    Person p = new Person();
                    p.setName(name);
                    p.setAge(age);
                    p.setPhone(phone);
                    p.setAddress(address);

                    PersonDBManager.getInstance().insert(p);

                    nameView.setText("");
                    ageView.setText("0");
                    phoneView.setText("");
                    addressView.setText("");
                }
            }
        });
    }
}
