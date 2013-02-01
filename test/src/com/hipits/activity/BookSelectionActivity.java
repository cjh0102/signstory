package com.hipits.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.hipits.R;

public class BookSelectionActivity extends Activity implements OnClickListener{

	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookselection);

		intent = new Intent(BookSelectionActivity.this, BookDescriptionActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		
		findViewById(R.id.view0).setOnClickListener(this);
		findViewById(R.id.view1).setOnClickListener(this);
		findViewById(R.id.view2).setOnClickListener(this);
		findViewById(R.id.view3).setOnClickListener(this);
		findViewById(R.id.view4).setOnClickListener(this);
		findViewById(R.id.view5).setOnClickListener(this);
		findViewById(R.id.view6).setOnClickListener(this);
		findViewById(R.id.view7).setOnClickListener(this);
		findViewById(R.id.view8).setOnClickListener(this);
		findViewById(R.id.view9).setOnClickListener(this);
	}
	
  

	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		if (id == R.id.view0) {
			intent.putExtra("index", 0);
		} else if (id == R.id.view1) {
			intent.putExtra("index", 1);
		} else if (id == R.id.view2) {
			intent.putExtra("index", 2);
		} else if (id == R.id.view3) {
			intent.putExtra("index", 3);
		} else if (id == R.id.view4) {
			intent.putExtra("index", 4);
		} else if (id == R.id.view5) {
			intent.putExtra("index", 5);
		} else if (id == R.id.view6) {
			intent.putExtra("index", 6);
		} else if (id == R.id.view7) {
			intent.putExtra("index", 7);
		} else if (id == R.id.view8) {
			intent.putExtra("index", 8);
		} else if (id == R.id.view9) {
			intent.putExtra("index", 9);
		}
		startActivity(intent);
	}
}
