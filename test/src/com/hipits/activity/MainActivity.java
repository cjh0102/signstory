package com.hipits.activity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hipits.R;
import com.hipits.manager.ScreenManager;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		calculateScreen();

		if (!isDirectory()) {
			getParsingData();
		}


		Button button1 = (Button)findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, BookSelectionActivity.class));
			}
		});

		Button button2 = (Button)findViewById(R.id.button2);
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, QuizActivity.class));

			}
		});

		Button button3 = (Button)findViewById(R.id.button3);
		button3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, PuzzlesActivity.class));
			}
		});

		Button button4 = (Button)findViewById(R.id.button4);
		button4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, HiddenImageActivity.class));
			}
		});

		Button button5 = (Button)findViewById(R.id.button5);
		button5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, CardGameActivity.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


	public void getParsingData() {

		final String[] codeNumbers = new String[]{"1.1056", "1.2001", "1.3016", "1.4012", "1.5018", "2.1001", "2.2002", "2.3002", "2.4004", "2.5004"};
		final Integer[] indexs = new Integer[]{0, 2};
		final HttpClient client = new DefaultHttpClient();
		final ResponseHandler<String> responseHandler = new BasicResponseHandler();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for (int i = 0; i < codeNumbers.length; i++) {
						HttpGet httpGet = new HttpGet("http://api.ibtk.kr/openapi/publicAssistanceFigurecover_api.do?code=" + codeNumbers[i]);
						String response = client.execute(httpGet, responseHandler);

						JSONObject source = new JSONObject(response).getJSONArray("ksp_list").getJSONObject(0);
						String description = source.getString("description");
						String imageUrl = source.getString("fileName");

						Bitmap bitmap = loadImageFromWeb(imageUrl);

						File path = new File(Environment.getExternalStorageDirectory() + "/signs");

						if (!path.exists()) {
							path.mkdir();
						}

						savePngFile(bitmap, "" + i, path);
						saveDescription(description, path);

					}

				} catch (Exception e) {
					Log.e("Exception", e.getMessage());
				} 
			}
		}).start();
	}

	public void saveDescription(String description, File path) throws IOException {


		File file = new File(path, "description.txt");

		if (!file.exists()) {
			file.createNewFile();
		} 

		FileWriter fileWriter = new FileWriter(file, true);
		fileWriter.write(description + "\n");
		fileWriter.flush();
		fileWriter.close();

	}

	private Bitmap loadImageFromWeb(String url) {
		try {
			InputStream inputStream = (InputStream)new URL(url).getContent();
			BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);			
			Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
			return bitmap;

		} catch (Exception e) {
			Log.e("LoadImageException", e.getMessage());
			return null;
		}
	}

	private void savePngFile(Bitmap bmp, String fileName, File path) {

		File file = new File(path, fileName + ".png");

		OutputStream out = null;

		try {
			file.createNewFile();
			out = new FileOutputStream(file);
			// Bitmap.CompressFormat.JPEG 도 있음
			// 저장 품질 선택 가능 (현재 100%)
			bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private Boolean isDirectory() {
		File path = new File(Environment.getExternalStorageDirectory() + "/signs");
		File file = new File(path, "description.txt");

		if (path.exists() && file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	public void calculateScreen() {

		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		ScreenManager screenManager = ScreenManager.getInstance();

		screenManager.setWidth(displayMetrics.widthPixels);
		screenManager.setHeight(displayMetrics.heightPixels);	
	}
}
