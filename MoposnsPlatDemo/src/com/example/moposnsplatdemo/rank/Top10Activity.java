package com.example.moposnsplatdemo.rank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.moposnsplatdemo.R;

public class Top10Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_top10);
		
		//AccountInfoSupport.Init(this);
		
		Button commit = (Button) findViewById(R.id.btn_commit_score);		
		if(commit != null){
			commit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent(Top10Activity.this, CommitScoreActivity.class));
				}
			});
		}
		
		Button getTop = (Button) findViewById(R.id.btn_get_top10);		
		if(getTop != null){
			getTop.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent(Top10Activity.this, RankActivity.class));
				}
			});
		}
	}
}
