package edu.uci.movietrivia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import edu.uci.movietrivia.sql.Stats;

public class GameStats extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_stats);
		
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_stats, menu);
		return true;
	}
	
	public void onResume()
	{
		super.onResume();
		generateResults();
	}
	private void generateResults()
	{
		TextView totalQuestions,totalTaken,totalCorrect,totalWrong,averageTime;
	
		totalQuestions = (TextView) findViewById(R.id.totalQuestions);
	
		totalTaken = (TextView) findViewById(R.id.totalTaken);
		
		totalCorrect = (TextView) findViewById(R.id.totalCorrect);
		
		totalWrong = (TextView) findViewById(R.id.totalWrong);
	
		averageTime = (TextView) findViewById(R.id.averageTime);
		
		String[] stats = Stats.getStats();
	

		
		totalTaken.setText("Quizzes Played: "+stats[0]);
		
		totalQuestions.setText("Questions answered: "+stats[1]);
	
		totalCorrect.setText("Number correct: "+stats[2]);
		totalWrong.setText("Number incorrect: "+stats[3]);
			
		averageTime.setText("Average time per question: "+stats[4]+" seconds");
		
	}

	public void goToMain(View v)
	{
		Intent i = new Intent(this,MainActivity.class);
		startActivity(i);
	}
	
}
