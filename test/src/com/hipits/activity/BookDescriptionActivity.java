package com.hipits.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.hipits.R;

public class BookDescriptionActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookdescription);
		
		Intent intent = getIntent();
		int index = intent.getIntExtra("index", 2);
		
		Log.e("index", ""+ index);
		
		ImageView signsImageView = (ImageView)findViewById(R.id.singsImageView);
		signsImageView.setImageBitmap(BookSelectionActivity.signs.get(index).getBitmap());
		
		TextView descriptionTextView = (TextView)findViewById(R.id.descriptionTextView);
		descriptionTextView.setText(BookSelectionActivity.signs.get(index).getDescription());
		
	}
	
}
