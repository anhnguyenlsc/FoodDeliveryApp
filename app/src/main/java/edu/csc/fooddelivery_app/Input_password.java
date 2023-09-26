package edu.csc.fooddelivery_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Input_password extends AppCompatActivity {

    private Button btn_login;
    private EditText edt_inputpass;
    private CheckBox checkRemember;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String PASSWORD_KEY = "PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_password);

        btn_login = findViewById(R.id.btn_login);
        edt_inputpass = findViewById(R.id.edt_inputpass);
        checkRemember = findViewById(R.id.checkRemember);

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        edt_inputpass.setText(sharedPreferences.getString(PASSWORD_KEY, ""));

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = edt_inputpass.getText().toString();
                Intent intent = new Intent(Input_password.this, Homepage.class);

                if (checkRemember.isChecked())
                {
                    editor = sharedPreferences.edit();
                    editor.putString(PASSWORD_KEY, password);
                    editor.commit();
                    startActivity(intent);
                }
                else
                    startActivity(intent);
            }
        });
    }
}

