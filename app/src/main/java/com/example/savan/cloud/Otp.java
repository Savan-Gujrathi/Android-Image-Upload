package com.example.savan.cloud;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Otp extends ActionBarActivity {

    TextView mTimeLabel;
    Intent intent1;

    long countdownTime, blinkTime, alertTime;
    int i;
    boolean blink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.savan.cloud.R.layout.activity_otp);

        mTimeLabel = (TextView) findViewById(com.example.savan.cloud.R.id.textView2);
        intent1 = new Intent(this,Login.class);

        countdownTime = 5 * 60 * 1000;
        alertTime = 60 * 1000;
        blinkTime = 10 * 1000;

        CountDownTimer timer1 = new CountDownTimer(countdownTime, 500){

            @Override
            public void onFinish() {
                mTimeLabel.setVisibility(getCurrentFocus().VISIBLE);
                Toast toast = Toast.makeText(getApplicationContext(), "Operation has timed out!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                startActivityForResult(intent1, 0);
            }

            @Override
            public void onTick(long millisUntilFinished) {

                long seconds = (int) (millisUntilFinished / 1000);

                if (millisUntilFinished < alertTime)
                {
                    mTimeLabel.setTextAppearance(getApplicationContext(), com.example.savan.cloud.R.style.timerAlertText);
                }

                if (millisUntilFinished < blinkTime)
                {
                    if (blink)
                        mTimeLabel.setVisibility(getCurrentFocus().VISIBLE);

                    else
                        mTimeLabel.setVisibility(getCurrentFocus().INVISIBLE);

                    blink = !blink;
                }

                mTimeLabel.setText(String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60));

            }
        }.start();

        Button button3 = (Button) findViewById(com.example.savan.cloud.R.id.button1);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Menu.class);
                startActivityForResult(intent,0);
            }
        });
    }
}