package com.hipits.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.hipits.R;

public class QuizActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		
	}
	
	public void onClick(View view){
		switch (view.getId()) {
		case R.id.homeImageViewButton:
			finish();
			break;
			
		default:
			break;
		}
	}

}
