package com.hipits.activity;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hipits.R;
import com.hipits.manager.DataManager;
import com.hipits.model.Sign;

public class QuizActivity extends Activity {
	
	private ImageView signsImageView;
	private int questionNumber = 1;
	private List<Sign> signs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz); 
		
		signsImageView = (ImageView) findViewById(R.id.signsImageView);
	
		signs = DataManager.getInstance().signs;
		
		signsImageView.setImageBitmap(signs.get(questionNumber - 1).getBitmap());
		
	}
	
	public void getRandomNumbers() {
		Random random = new Random();
		int number = random.nextInt() - 1;
		
	}

	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.homeImageViewButton:
			finish();
			break;
		case R.id.correctAnswer1:
			break;
		case R.id.correctAnswer2:
			break;
		case R.id.correctAnswer3:
			break;
		case R.id.hintTextView:
			
			break;
		default:
			break;
		} 
	}
	
	public void showHintDialog() {
		
	}
}
