package edu.uci.movietrivia;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import edu.uci.movietrivia.sql.Stats;

public class EndQuiz extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end_quiz);
		TextView results = (TextView)findViewById(R.id.total_Correct);
		Bundle extras = getIntent().getExtras();
		
		
		
		int numCorrect = extras.getInt("numCorrect");
		int numQuestions = extras.getInt("numQuestions");
		DecimalFormat df = new DecimalFormat("##.##");
		
		double time = 180/numQuestions;
		String avTime = df.format(time);
		String summary = "Number Correct:\n"+numCorrect+" out of "+extras.getInt("numQuestions")+" questions\n\n Average time of:\n "+ Double.parseDouble(avTime)+" seconds spend per question.";
		Stats.updateStats(numQuestions, numCorrect, numQuestions-numCorrect, Double.parseDouble(avTime));
		results.setText(summary);
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.end_quiz, menu);
		return true;
	}
	
	public void goToMainScreen(View v)
	{
		Intent i = new Intent(this,MainActivity.class);
		startActivity(i);
	}

}
