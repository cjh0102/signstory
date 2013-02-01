package com.hipits.customview;

import java.util.ArrayList;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.hipits.R;
import com.hipits.activity.HiddenImageActivity;
import com.hipits.manager.PointManager;
import com.hipits.model.Point;

public class HiddenImageView extends View {

	private Point touchPoint;
	
	private Bitmap faileBitmap;
	private Bitmap correctBitmap;
	
	private Boolean isStart = false;
	private Boolean isCorrect = false;
	
	private Paint paint;
	private Paint fadePaint;
	
	private ArrayList<Rect> correctRects;
	private ArrayList<Point> correctPoints;
	
	private Context context;

	public HiddenImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		faileBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		correctBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2);

		paint = new Paint();
		fadePaint = new Paint();
		touchPoint = new Point();
		
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);

		setBackgroundColor(Color.BLACK);

		correctPoints = new ArrayList<Point>();

		setCorrectRects();

	}

	@SuppressLint("DrawAllocation")
	@SuppressWarnings("unchecked")

	@Override
	protected void onDraw(final Canvas canvas) {

		if (isStart == true) {
			if (isCorrect == false) {
				// 정답이 아닌 곳을 눌렀을경우 X를 그림
				float xx = touchPoint.getX() - (faileBitmap.getWidth() / 2);
				float yy = touchPoint.getY() - (faileBitmap.getHeight() / 2);
				canvas.drawBitmap(faileBitmap, xx, yy, fadePaint);
			}

			// 정답인 곳 저장
			for (Point correctPoint : correctPoints) {
				float xX = correctPoint.getX() - (correctBitmap.getWidth() / 2);
				float yY = correctPoint.getY() - (correctBitmap.getHeight() / 2);
				canvas.drawBitmap(correctBitmap, xX, yY, paint);
			}
		}

		for (Rect rect : correctRects) {
			canvas.drawRect(rect, paint);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			isStart = true;

			touchPoint.setX((int)event.getX());
			touchPoint.setY((int)event.getY());
			Log.e("터치", "터치터치터치");

			isCorrect(touchPoint);
			
			if (isEnd()) {
				showDialog();
			}
		}
		return true;
	}

	public void setCorrectRects() {

		PointManager pointManager = PointManager.getInstance();
		correctRects = new ArrayList<Rect>();

		for (Point point : pointManager.getPoints()) {
			Rect rect = new Rect(point.getX(), point.getY(),
					point.getX() + point.getLength(), point.getY() + point.getLength());
			correctRects.add(rect);
		}

	}

	public void isCorrect(Point touchPoint) {

		int i = 0;

		for (Rect rect : correctRects) {
			if (rect.contains(touchPoint.getX(), touchPoint.getY())) {
				isCorrect = true;
				
				correctPoints.add(new Point(rect.centerX(), rect.centerY(), 0));
				((HiddenImageActivity)context).checkCorrectImage(i);
				
				invalidate();
				return;
			}
			i++;
		}

		if (i == 3) {
			isCorrect = false;
			fadeOutImage();
			return;
		}
	}

	private void resetGame() {
		correctPoints.clear();
		((HiddenImageActivity)context).countNumber = 5;
		((HiddenImageActivity)context).initImageView();
		//setBackgroundColor(Color.YELLOW);
		invalidate();
	}

	private void showDialog() {

		AlertDialog.Builder dialog= new AlertDialog.Builder(context);
		dialog.setTitle("축하합니다");
		dialog.setMessage("성공");
		dialog.setPositiveButton("다른 게임 할래요!", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				((HiddenImageActivity)context).finish();
			}
		});

		dialog.setNegativeButton("더 할래요!", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				resetGame();
			}
		});
		dialog.show();
	}

	public Boolean isEnd() {
		if (correctPoints.size() == 3) {
			return true;
		}
		return false;
	}

	public void fadeOutImage() {
		
		AsyncTask<Void, Integer, Void> asyncTask = new AsyncTask<Void, Integer, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				for (int i = 255; i >= 0; i = i - 51) {
					SystemClock.sleep(100);
					publishProgress(i);
				}
				return null;
			}
			
			protected void onProgressUpdate(Integer... values) {
				fadePaint.setAlpha(values[0]);
				invalidate();
			};
		}.execute();
	}
}
