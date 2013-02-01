package com.hipits.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hipits.R;
import com.hipits.customview.HiddenImageView;

public class HiddenImageActivity extends Activity {
	
	private LinearLayout rootView;
	private ProgressBar countProgressBar;
	private HiddenImageView hiddenImageView;
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hiddenimage);
		
		rootView = (LinearLayout)findViewById(R.id.rootView);
		hiddenImageView = (HiddenImageView)findViewById(R.id.hiddenImageView);

		initImageView();

		countProgressBar = (ProgressBar)findViewById(R.id.countProgressBar);
		countProgressBar.setMax(30);
		
		startTimeCount();
		
		findViewById(R.id.homeImageViewButton).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(HiddenImageActivity.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}
	
	public void startTimeCount() {
		
		AsyncTask<Void, Integer, Void> timeCounterAsyncTask = new AsyncTask<Void, Integer, Void>(){
			@Override
			protected Void doInBackground(Void... params) {
				for (int i = 30; i >= 0; i--) {
					if (!hiddenImageView.isEnd()) {
						publishProgress(i);
						SystemClock.sleep(1000);
					}
				}
				return null;
			}
			
			@Override
			protected void onProgressUpdate(Integer... values) {
				countProgressBar.setProgress(values[0]);
			}

			@Override
			protected void onPostExecute(Void result) {
				if (!HiddenImageActivity.this.isFinishing() && !hiddenImageView.isEnd()) {
					showFailDialog();
				}
			}
			
			@Override
			protected void onCancelled() {
				super.onCancelled();
			}
			
		}.execute();
	}
	
	public void showFailDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("시간이 지났어요~");
		dialog.setMessage("실패!");
		dialog.setPositiveButton("다른게임 하기", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();
			}
		});
		
		dialog.setNegativeButton("다시 하기", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				startTimeCount();
				dialog.dismiss();
				
			}
		});
		dialog.show();
	}
	
	public void checkCorrectImage(int index) {
		(rootView.getChildAt(index)).setBackgroundColor(Color.BLUE);
	}
	
	public void initImageView() {
		for (int i = 0; i < rootView.getChildCount(); i++) {
			View view = rootView.getChildAt(i);
			view.setBackgroundColor(Color.RED);
		}
	}
}
