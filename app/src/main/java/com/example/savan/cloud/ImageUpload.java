package com.example.savan.cloud;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;


public class ImageUpload extends Activity {

    ProgressDialog prgDialog;
    String encodedString;
    RequestParams params = new RequestParams();
    Bitmap bitmap;
	private String filePath = null;
	private ImageView imgPreview;
	private Button back;
	long totalSize = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imageupload);

        prgDialog = new ProgressDialog(this);
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        back = (Button) findViewById(R.id.button2);
		imgPreview = (ImageView) findViewById(R.id.imgPreview);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ImageUpload.this, Menu.class);
                startActivity(i);
            }
        });

		// Receiving the data from previous activity
		Intent i = getIntent();
		// image or video path that is captured in previous activity
		// boolean flag to identify the media type, image or video
		//boolean isImage = i.getBooleanExtra("isImage", true);
        if(Menu.flag==1) {
            filePath = i.getStringExtra("filePath");
        }else if(Menu.flag==2) {
            filePath = i.getStringExtra("picturePath");
        }else{
            Toast.makeText(this,"Flag isn't getting into ImageUpload.java!!!",Toast.LENGTH_LONG).show();
        }

		if (filePath != null) {
			// Displaying the image or video on the screen
            Toast.makeText(this,"File path is : "+filePath,Toast.LENGTH_LONG).show();
			previewMedia();
		} else {
			Toast.makeText(getApplicationContext(),
					"Sorry, file path is missing!", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Displaying captured image/video on the screen
	 * */
	private void previewMedia() {
		// Checking whether captured media is image or video
			//imgPreview.setVisibility(View.VISIBLE);
			BitmapFactory.Options options = new BitmapFactory.Options();
			// down sizing image as it throws OutOfMemory Exception for larger
			// images
			options.inSampleSize = 8;

			final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
            Toast.makeText(this,filePath,Toast.LENGTH_LONG).show();
			imgPreview.setImageBitmap(bitmap);
	}

    // When Upload button is clicked
    public void uploadImage(View v) {
        // When Image is selected from Gallery
        if (filePath != null && !filePath.isEmpty()) {
            Toast.makeText(this,"Inside upload image",Toast.LENGTH_LONG).show();
            prgDialog.setMessage("Converting Image to Binary Data");
            prgDialog.show();
            String fileNameSegments[] = filePath.split("/");
            String fileName = fileNameSegments[fileNameSegments.length - 1];
            // Put file name in Async Http Post Param which will used in Php web app
            params.put("filename", fileName);

            // Convert image to String using Base64
            encodeImagetoString();
            // When Image is not selected from Gallery
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "You must select image from gallery before you try to upload",
                    Toast.LENGTH_LONG).show();
        }
    }

    // AsyncTask - To convert Image to String
    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            };

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(filePath,options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                prgDialog.setMessage("Calling Upload");
                // Put converted Image string into Async Http Post param
                params.put("image", encodedString);
                // Trigger Image upload
                triggerImageUpload();
            }
        }.execute(null, null, null);
    }

    public void triggerImageUpload() {
        makeHTTPCall();
    }

    // http://192.168.2.4:9000/imgupload/upload_image.php
    // http://192.168.2.4:9999/ImageUploadWebApp/uploadimg.jsp
    // Make Http call to upload Image to Php server
    public void makeHTTPCall() {
        prgDialog.setMessage("Invoking Php");
        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post("http://192.168.0.4:80/imgupload/upload_image.php",
                params, new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        prgDialog.hide();
                        Toast.makeText(getApplicationContext(), response,
                                Toast.LENGTH_LONG).show();
                    }

                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // Hide Progress Dialog
                        prgDialog.hide();
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
                                            + statusCode, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Dismiss the progress bar when application is closed
        if (prgDialog != null) {
            prgDialog.dismiss();
        }
    }
}
