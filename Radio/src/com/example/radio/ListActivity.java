package com.example.radio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import com.example.radio.StationLoader.ParserCallBack;
import com.example.radio.entity.ResponseInfo;
import com.example.radio.entity.Station;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class ListActivity extends Activity{
		
	private PullToRefreshListView mPullRefreshListView;
	private ArrayAdapter<String> mAdapter;
		
	private ArrayList<String> stationList;
	private ArrayList<Station> stations;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
				
		Intent intent = getIntent();		

		stationList = new ArrayList<String>();
		
		stations = intent.getParcelableArrayListExtra("stations");
				
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		
		setList();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}
	
	public void setList(){
		
		for (Station station: stations){
			//Log.i("DE:", station.getName());
			stationList.add(station.getName());
		}
		
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stationList);
		mPullRefreshListView.setAdapter(mAdapter);
	}

}
