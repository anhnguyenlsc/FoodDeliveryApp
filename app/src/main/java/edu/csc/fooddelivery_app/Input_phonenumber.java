package edu.csc.fooddelivery_app;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Input_phonenumber extends AppCompatActivity{
    private EditText edt_inputphone;
    private TextView tv_error;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String USER_PHONENUMBER_KEY = "USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_phonenumber);

        edt_inputphone = findViewById(R.id.edt_inputphone);
        tv_error = findViewById(R.id.tv_error);

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        edt_inputphone.setText(sharedPreferences.getString(USER_PHONENUMBER_KEY, ""));

        edt_inputphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String phone = edt_inputphone.getText().toString().trim();

                if (charSequence.length() == 0)
                {
                    tv_error.setText("Bạn phải nhập số điện thoại");
                }
                else if (charSequence.length() != 10)
                {
                    tv_error.setText("Số điện thoại không hợp lệ");
                }
                else
                {
                    tv_error.setText(null);
                    if (validateMobile(edt_inputphone.getText().toString())) {
                        editor = sharedPreferences.edit();
                        editor.putString(USER_PHONENUMBER_KEY, phone);
                        editor.commit();
                        Intent intent = new Intent(Input_phonenumber.this, Input_password.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            public boolean validateMobile(String input) {
                Pattern p = Pattern.compile("([038]?[039]?[070]?[077]?[076][079]?)[0-9]{9}");
                Matcher m = p.matcher(input);
                return m.matches();
            }
        });

    }

}
