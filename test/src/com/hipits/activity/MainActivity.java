package com.hipits.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hipits.R;
import com.hipits.manager.DataManager;
import com.hipits.manager.ScreenManager;
import com.hipits.model.Sign;


public class MainActivity extends Activity {
	
	public static List<Sign> signs = new ArrayList<Sign>();
	
	@Override
	protected void onDestroy() {
		signs.clear();
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		calculateScreen();
		
		DataManager dataManager = DataManager.getInstance();

		if (!isDirectory()) {
			dataManager.getParsingData();
		} else {
			try {
				dataManager.getDataList();
			} catch (Exception e) {
				Log.e("Exception", e.getMessage());
			}
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
