package com.example.radio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import com.example.radio.StationLoader.ParserCallBack;
import com.example.radio.entity.ResponseInfo;
import com.example.radio.entity.Station;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public final class MyRefreshListActivity extends ListActivity  implements ParserCallBack{
		
	private PullToRefreshListView mPullRefreshListView;
	private ArrayAdapter<String> mAdapter;
		
	private ArrayList<String> stationList;
	private ArrayList<Station> stations;
	
	StationLoader stationLoader;
	String url_token = "";
	
	private MediaPlayer mediaPlayer;
	
	boolean playerIsOne = false;
	Player player;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
						
		Intent intent = getIntent();		

		stationList = new ArrayList<String>();
		
		mediaPlayer = new MediaPlayer();
		
		
		stations = intent.getParcelableArrayListExtra("stations");
		url_token = intent.getStringExtra("url_token");
				
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		
		mPullRefreshListView.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				String url = stations.get(position - 1).getUrl();

				if (!playerIsOne) {
					player = new Player(MyRefreshListActivity.this, mediaPlayer);
					player.execute(url);
					playerIsOne = true;
				} else {
					// check if you have selected another radioStation
					mediaPlayer.stop();
					mediaPlayer.reset();
					player = new Player(MyRefreshListActivity.this, mediaPlayer);
					player.execute(url);
				}
			}
		});
		
		
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
		    @Override
		    public void onRefresh(PullToRefreshBase<ListView> refreshView) {

		    	String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		        // Do work to refresh the list here.
		       new GetDataTask().execute();
		    	
		    }
		});
						
		setList();
		
	}
	
	
	
	private void onRefresh(){
		
		//create new asyncTask StationLoader
		stationLoader = new StationLoader();
  	  	stationLoader.setParserCallBack(this);
    	stationLoader.execute(url_token);
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
	   
		@Override
		protected String[] doInBackground(Void... arg0) {

			return null;
		}
		
		@Override
	    protected void onPostExecute(String[] result) {
	        // Call onRefreshComplete when the list has been refreshed.
			
			onRefresh();
	    	mPullRefreshListView.onRefreshComplete();
	        super.onPostExecute(result);
	    }
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

	@Override
	public void setInfo(ArrayList<Station> array) {
		stations = array;
		setList();
	}

	@Override
	public void setResponse(ResponseInfo response) {
		// TODO Auto-generated method stub
		
	}	

}
