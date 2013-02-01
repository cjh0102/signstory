package com.hipits.activity;


import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

import com.hipits.R;
import com.hipits.customview.PuzzleGameView;

public class PuzzlesActivity extends Activity {

	private ProgressBar countProgressBar;
	private PuzzleGameView puzzleGameView;
	public int countNumber = 5;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle);
		
		countProgressBar = (ProgressBar)findViewById(R.id.counterProgressBar);
		countProgressBar.setMax(5);
		
		startTimeCount();

		puzzleGameView = (PuzzleGameView)findViewById(R.id.puzzleGameView);
		
		findViewById(R.id.homeImageViewButton).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(PuzzlesActivity.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		
	}

	public void startTimeCount() {
		countNumber = 5;
		
		final Timer timer = new Timer();
		
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					showFailDialog();
				}
			}
		};
		
		final TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				if (puzzleGameView.isEnd == false) {
					if (countNumber <= 0) {
						timer.cancel();
						handler.sendEmptyMessage(0);
					}
					else if (isFinishing()) {
						timer.cancel();
					}
					countProgressBar.setProgress(countNumber);
					countNumber--;
				}
			}
		};
		
		timer.schedule(timerTask, 0, 1000);

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


