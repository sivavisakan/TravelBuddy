package edu.cmu.travelbuddy.image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import android.R.bool;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;


/**
 * 
 * @author amol
 *
 */
public class ClickPicture extends Activity {
	/** Called when the activity is first created. */

	private static final int CAMERA_PIC_REQUEST = 1339;
	
	private String IMAGE_NAME = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		IMAGE_NAME = "tb" + (new Date()).getTime() ;
		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
		//finish();
		
		
		
		/**
		 * For testing purpose:
		 */
		/*
		Button myButton = new Button(this);
		myButton.setText("Click for Launching Camera");
		myButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
			}
		});

		this.setContentView(myButton);
		*/
	}
	
	
	/**
	 * 
	 * @param fileName: The name for the image with file extension.
	 */

	
	
/**
 * Determine FileName using parameters, for now it is set to testImage.jpeg
 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_PIC_REQUEST) {
			
			Bitmap x = (Bitmap) data.getExtras().get("data");
			String fileName = IMAGE_NAME;
			try { 
				FileOutputStream os = null;
	            if (android.os.Environment.getExternalStorageState().equals(
	                    android.os.Environment.MEDIA_MOUNTED)){
	                //File sdCard = Environment.getExternalStorageDirectory();
	                //File dir = new File (sdCard.getAbsolutePath() + dirName);
	                //dir.mkdirs();
	            	String root = Environment.getExternalStorageDirectory().toString();
	            	//File dir = new File(root + dirName);
	            	File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "travelbuddy_data");
	            	boolean check = dir.mkdir();
	                //File file = new File(this.getExternalFilesDir(null), this.dirName+fileName); //this function give null pointer exception so im using other one
	                File file = new File(dir, fileName);
	                os = new FileOutputStream(file);
	                x.compress(Bitmap.CompressFormat.PNG, 90, os);
	                //os = this.openFileOutput(x.toString(), MODE_PRIVATE);
	            }else{
	                //os = context.openFileOutput(fileName, MODE_PRIVATE);
	            	Log.d("TAG", "in else");
	            }
	            //resizedBitmap.compress(CompressFormat.PNG, 100, os);
	            os.flush();
	            os.close();
	            finish();
	        }catch(Exception e){
	        	e.printStackTrace();
	        	e.toString();
	        	Log.d("Chakka line","for debug");
	        }
			finish();
		}
	}

}