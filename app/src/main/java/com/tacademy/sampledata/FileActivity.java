package com.tacademy.sampledata;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class FileActivity extends AppCompatActivity {

    EditText dataView;
    private static final String FILE_NAME = "note.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        dataView = (EditText)findViewById(R.id.edit_data);

        Button btn = (Button)findViewById(R.id.btn_save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BufferedWriter bw = null;
                try {
                    String data = dataView.getText().toString();
                    OutputStream os = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                    bw = new BufferedWriter(new OutputStreamWriter(os));
                    bw.write(data, 0, data.length());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bw != null)
                        try {
                            bw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }
        });

        btn = (Button)findViewById(R.id.btn_load);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputStream is = openFileInput(FILE_NAME);
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n\r");
                    }

                    dataView.setText(sb.toString());
                    br.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
