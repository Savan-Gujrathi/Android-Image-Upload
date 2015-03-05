package com.example.savan.cloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Login extends Activity {

    Button butt1,butt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        butt1 = (Button) findViewById(R.id.button1);
        butt2 = (Button) findViewById(R.id.button2);
        TextView textView = (TextView) findViewById(R.id.textView2);

        textView.setText(Html.fromHtml(getString(R.string.txtViewNoAcc)));

        butt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Otp.class);
                startActivity(i);
            }
        });

        butt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, SignUp.class);
                startActivity(i);
            }
        });

    }
}
