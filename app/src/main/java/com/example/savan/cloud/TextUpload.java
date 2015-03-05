package com.example.savan.cloud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;


public class TextUpload extends Activity {

    Button b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.savan.cloud.R.layout.activity_textupload);

        b3 = (Button) findViewById(com.example.savan.cloud.R.id.button7);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TextUpload.this, Menu.class);
                startActivity(intent);
              //  startActivityForResult(intent,0);
            }
        });
    }
}
