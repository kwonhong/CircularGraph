package com.example.circulargraph;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {
	
	private int colors[] = { Color.parseColor("#F2C249"),
			Color.parseColor("#E6772E"), Color.parseColor("#4DB3B3"),
			Color.parseColor("#E64A45"), Color.parseColor("#3D4C53"),
			Color.parseColor("#FFFFFF") };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        CircularGraph graph = (CircularGraph)findViewById(R.id.circularGraph);
        graph.setGraphPortions(setDefaultPortions());
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.man);
        graph.setImage(bm);
        
    }

	public ArrayList<Portion> setDefaultPortions() {
		ArrayList<Portion> mPortions = new ArrayList<Portion>();
		mPortions.add(new Portion(0.2f, colors[0], "text1"));
		mPortions.add(new Portion(0.2f, colors[1], "text1"));
		mPortions.add(new Portion(0.3f, colors[2], "text1"));
		mPortions.add(new Portion(0.25f, colors[3], "text1"));
//		mPortions.add(new Portion(0.05f,colors[4], "expense"));
		return mPortions;
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
