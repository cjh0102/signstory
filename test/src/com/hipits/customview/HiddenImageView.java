package com.hipits.customview;

import java.util.ArrayList;
import java.util.Iterator;

import com.hipits.R;
import com.hipits.activity.HiddenImageActivity;
import com.hipits.activity.PuzzlesActivity;
import com.hipits.manager.PointManager;
import com.hipits.model.Point;

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
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class HiddenImageView extends View {

	private float x;
	private float y;
	private Bitmap faileBitmap;
	private Bitmap correctBitmap; 
	private Boolean isStart = false;
	private Boolean isCorrect = false;
	private Paint paint;
	private ArrayList<Rect> correctRects;
	private ArrayList<Point> correctPoints;
	private Context context;

	public HiddenImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		faileBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		correctBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher2);

		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);

		setBackgroundColor(Color.BLACK);

		correctPoints = new ArrayList<Point>();

		setCorrectRects();

	}

	@SuppressLint("DrawAllocation")
	@SuppressWarnings("unchecked")

	@Override
	protected void onDraw(Canvas canvas) {
	
		if (isStart == true) {
			if (isCorrect == false) {
				// 정답이 아닌 곳을 눌렀을경우 X를 그림
				float xx = x - (faileBitmap.getWidth() / 2);
				float yy = y - (faileBitmap.getHeight() / 2);
				canvas.drawBitmap(faileBitmap, xx, yy, null);
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
		
		if (isEnd()) {
			showDialog();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			isStart = true;

			x = event.getX();
			y = event.getY();
			Log.e("터치", "터치터치터치");

			Point touchPoint = new Point((int)x, (int)y, 0);
			isCorrect(touchPoint);
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
				break;
			}
			i++;
		}

		if (i == 3) {
			isCorrect = false;
		}
		
		invalidate();
	}
	
	private void resetGame() {
		correctPoints.clear();
		((HiddenImageActivity)context).startTimeCount();
		((HiddenImageActivity)context).initImageView();
		setBackgroundColor(Color.YELLOW);
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
}
