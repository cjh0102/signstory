package com.hipits.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.hipits.R;
import com.hipits.customview.HiddenImageView;

public class HiddenImageActivity extends Activity {

	private LinearLayout rootView;
	private ProgressBar countProgressBar;
	private HiddenImageView hiddenImageView;
	public int countNumber = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hiddenimage);

		rootView = (LinearLayout)findViewById(R.id.rootView);
		hiddenImageView = (HiddenImageView)findViewById(R.id.hiddenImageView);

		initImageView();

		countProgressBar = (ProgressBar)findViewById(R.id.countProgressBar);
		countProgressBar.setMax(countNumber);

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
				if (!hiddenImageView.isEnd()) {
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
