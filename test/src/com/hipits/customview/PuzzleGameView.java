package com.hipits.customview;

import java.util.Random;

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
import android.view.MotionEvent;
import android.view.View;

import com.hipits.R;
import com.hipits.activity.PuzzlesActivity;
import com.hipits.model.Box;

public class PuzzleGameView extends View {
	Context context;
	
	Bitmap puzzle_background_pororo;
	
	int gLT;//gridLineT
	int bW, bH;
	int COLS = 4, ROWS = 4;
	
	Box box[] = new Box[COLS*ROWS];
	
	int blankBoxIndex=box.length-1;
	public Boolean isEnd = false;
	
	public PuzzleGameView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		this.context = context;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		puzzle_background_pororo = BitmapFactory.decodeResource(getResources(), R.drawable.puzzle_background_pororo);
		puzzle_background_pororo = Bitmap.createScaledBitmap(puzzle_background_pororo, w, h, false);
		
		gLT = getWidth()/60;
		bW = (getWidth() - (COLS + 1) * gLT) / COLS;
		bH=(getHeight() - (ROWS + 1) * gLT) / ROWS;
		
		int i = 0;
		
		for(int r = 0; r< ROWS;r++) {
			for (int c = 0; c < COLS; c++){
				int left = (gLT + bW)* c + gLT;
				int top = (gLT + bH)* r + gLT;
				int right = left + bW;
				int bottom = top+bH;
				box[i]= new Box();
				box[i].rect = new Rect(left, top, right, bottom);
				box[i].bitmap=Bitmap.createBitmap(puzzle_background_pororo, left, top, bW, bH);
				i++;
			}
		}
		shuffle();
	}
	
	public void shuffle() {
		Random random = new Random();
		do{
			for(int i = 0; i<50; i++){
				swap(box[random.nextInt(box.length-1)], box[blankBoxIndex]);
			}
		}
		while(isCompleted()==true);
		invalidate();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		
		paint.setColor(Color.CYAN);
		canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
		paint.setColor(Color.BLACK);
		
		for (int i=0;i<box.length;i++){
			if(i==blankBoxIndex) {
				canvas.drawRect(box[i].rect.left, box[i].rect.top, box[i].rect.right, box[i].rect.bottom, paint);
			} else {
				canvas.drawBitmap(box[i].bitmap, box[i].rect.left, box[i].rect.top, null);
			}
		}
		if (isCompleted()==false) return;
		showDialog();
	}
	
	private void showDialog() {
		AlertDialog.Builder dialog= new AlertDialog.Builder(context);
		dialog.setTitle("다맞췄어요");
		dialog.setMessage("성공");
		dialog.setPositiveButton("다른게임 할래요", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				((PuzzlesActivity)context).finish();
			}
		});
		
		dialog.setNegativeButton("더 할래요", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				shuffle();
				((PuzzlesActivity)context).startTimeCount();
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	public Boolean isCompleted() {
		for (int r = 0; r < ROWS-1; r++){ 
			for (int c = 0 ; c<COLS; c++){
				if(box[r*COLS+c].rect.top >= box[(r+1)*COLS+c].rect.top){
					isEnd = false;
					return false;
				}
			}
		}
		for (int c = 0; c<COLS-1;c++){
			for (int r=0;r<ROWS; r++){
				if(box[r*COLS+c].rect.left>= box[r*COLS+(c+1)].rect.left){
					isEnd = false;
					return false;
				}
			}
		}
		isEnd = true;
		return true;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			for(int i=0 ; i<box.length;i++){
				if(box[i].rect.contains((int)event.getX(), (int)event.getY())){
					swap(box[i], box[blankBoxIndex]);
					break;
				}
			}
		invalidate();
		}
		return true;
	}
	private void swap(Box box1, Box box2) {
		if(box1.rect.left==box2.rect.left && Math.abs(box1.rect.top-box2.rect.top)<1.5*bH || box1.rect.top==box2.rect.top && Math.abs(box1.rect.left-box2.rect.left)<1.5*bW){
			Rect temp = new Rect(box1.rect);
			box1.rect=box2.rect;
			box2.rect=temp;
		}
	}
}
