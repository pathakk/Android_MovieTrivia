package edu.uci.movietrivia;

import edu.uci.movietrivia.sql.DatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button mQuizMe;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DatabaseHelper(this);
        
        mQuizMe = (Button) findViewById(R.id.start_quiz);
      
        
        mQuizMe.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this,QuizActivity.class);
				startActivity(i);
			}
        	
        	
        });
        
        
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void goToStats(View v)
    {
    	Intent i = new Intent(this, GameStats.class);
    	startActivity(i);
    	
    }
    
}
