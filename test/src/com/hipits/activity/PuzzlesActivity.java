package com.hipits.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

import com.hipits.R;
import com.hipits.customview.PuzzleGameView;

public class PuzzlesActivity extends Activity {

	private ProgressBar countProgressBar;
	private PuzzleGameView puzzleGameView;

	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle);
		
		countProgressBar = (ProgressBar)findViewById(R.id.counterProgressBar);
		puzzleGameView = (PuzzleGameView)findViewById(R.id.puzzleGameView);
		
		findViewById(R.id.homeImageViewButton).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(PuzzlesActivity.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		startTimeCount();
	}

	public void startTimeCount() {

		AsyncTask<Void, Integer, Void> timeCounterAsyncTask = new AsyncTask<Void, Integer, Void>(){
			protected void onPreExecute() {
				countProgressBar.setMax(20);
			};

			@Override
			protected Void doInBackground(Void... params) {
				for (int i = 20; i >= 0; i--) {
						if (puzzleGameView.isEnd == false) {
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
				if (!isFinishing()&& puzzleGameView.isEnd == false) {
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
		AlertDialog.Builder dialog= new AlertDialog.Builder(this);
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
				puzzleGameView.shuffle();
				dialog.dismiss();
			}
		});
		dialog.show();
	}
}


