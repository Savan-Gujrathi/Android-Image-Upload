package com.example.savan.cloud;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SignUp extends Activity {

     TextView text;
     Button fetch;
     EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        text = (TextView) findViewById(R.id.textView3);
        et = (EditText) findViewById(R.id.editText1);
        fetch = (Button) findViewById(R.id.button1);

        fetch.setOnClickListener((View.OnClickListener) this);

    }

    class task extends AsyncTask<String, String, Void> {
        private ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
        InputStream is = null;
        String result = "";

        protected void onPreExecute() {
            progressDialog.setMessage(("Fetching data..."));
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    task.this.cancel(true);
                }
            });
        }

        protected Void doInBackground(String... params) {
            String url = "http://127.0.0.1/imgupload/database.php";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url); //YOUR PHP SCRIPT ADDRESS

            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(param));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                //read content
                is = httpEntity.getContent();
            } catch (Exception e) {

                Log.e("log_tag", "Error in http Connection " + e.toString());
                Toast.makeText(SignUp.this, "Please Try Again", Toast.LENGTH_LONG).show();
            }
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {

                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
            } catch (Exception e) {
                Log.e("log_tag", "Error  converting result " + e.toString());
            }
            return null;
        }

        protected void onPostExecute(Void v) {
            //parse json data
            try {
                JSONArray Jarray = new JSONArray(result);
                for (int i = 0; i < Jarray.length(); i++) {
                    JSONObject Jasonobject = null;
                    Jasonobject = Jarray.getJSONObject(i);

                    //Get Output to the screen
                    String rid = Jasonobject.getString("Reg Id");
                    String db_detail = "";
                    String db_detail1 = "";

                    if (et.getText().toString().equalsIgnoreCase(rid)) {
                        db_detail = Jasonobject.getString("Username");
                        db_detail1 = Jasonobject.getString("Password");
                        text.setText(db_detail);
                        text.setText(db_detail1);
                        break;
                    }
                }
                this.progressDialog.dismiss();

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error Parsing Data " + e.toString());
            }
        }
    }
}
