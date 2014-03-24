package edu.uci.movietrivia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;


import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.uci.movietrivia.questions.Question;

import edu.uci.movietrivia.sql.QuestionGenerator;

public class QuizActivity extends Activity {

	private Button A,B,C,D;
	private TextView question;
	private Question q;
	private int questionNum;
	
	private int correctAnswers;
	private static Long timeLeft;
	private Long instanceTime;
	private CountDownTimer timer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		
		questionNum = 0;
		correctAnswers = 0;
	
		timeLeft = Long.valueOf(180000);
		bindTimer(timeLeft);
		
		
		

	}
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putLong("time", instanceTime);
	}
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		this.instanceTime = savedInstanceState.getLong("time");
	}
	@Override
	protected void onResume()
	{	super.onResume();
		if(instanceTime!=null) bindTimer(instanceTime);
		
		setUpQuestion();
		
	
	}
	@Override
	protected void onPause()
	{
		super.onPause();
		instanceTime = timeLeft;
		timer.cancel();
		
		
		
		
	}
	public void bindTimer(long timeLeft)
	{
		
		timer = new CountDownTimer(timeLeft, 1000) {

		     public void onTick(long millisUntilFinished) {
		         TextView tv = (TextView)findViewById(R.id.timer);
		         long minutes = millisUntilFinished/60000;
		         long seconds = (millisUntilFinished-(60000*minutes))/1000;
		         String sec = null;
		         
		         if(seconds<10)
		        	 sec = "0"+Long.toString(seconds);
		         else
		        	 sec = Long.toString(seconds);
		         
		         
		         
		         QuizActivity.timeLeft = millisUntilFinished;
		         tv.setText(minutes+":"+sec);
		         tv.setTextColor(getResources().getColor(R.color.DarkRed));
		         tv.setBackgroundColor(Color.TRANSPARENT);
		     }

		     public void onFinish() {
		    	
		         endQuiz();
		     }
		  }.start();
		
		
	}
	
	
	private void endQuiz()
	{
		Intent i = new Intent(this,EndQuiz.class);
		i.putExtra("numQuestions", questionNum);
		i.putExtra("numCorrect", correctAnswers);
		startActivity(i);
		
	}
	public void setUpQuestion()
	{	questionNum++;
		
			q = QuestionGenerator.getRandomQuestion();
		
			question = (TextView) findViewById(R.id.question_text);
		
			A = (Button) findViewById(R.id.option_A);
		
			B  = (Button) findViewById(R.id.option_B);
		
			C = (Button) findViewById(R.id.option_C);
		
			D = (Button) findViewById(R.id.option_D);
		
			question.setText(questionNum+". "+q.getQuestion());
			A.setText(q.getOptionA());
			B.setText(q.getOptionB());
			C.setText(q.getOptionC());
			D.setText(q.getOptionD());
			A.setBackgroundColor(getResources().getColor(R.color.SlateGray));
			B.setBackgroundColor(getResources().getColor(R.color.SlateGray));
			C.setBackgroundColor(getResources().getColor(R.color.SlateGray));
			D.setBackgroundColor(getResources().getColor(R.color.SlateGray));
		
	}
	
	
	public void checkAnswer(View v)
	{
		
		Button pressedButton = (Button) v;
		
		String answerText = pressedButton.getText().toString();
		
		if(answerText.equals(q.getCorrectAnswer()))
		{
			correctAnswers++;
			Toast toast = Toast.makeText(this, (R.string.correct_toast), Toast.LENGTH_SHORT);
			toast.getView().setBackgroundColor(Color.GREEN);
			toast.show();
			
			
		}
		
		else
		{
			
			Toast toast = Toast.makeText(this, R.string.wrong_toast, Toast.LENGTH_SHORT);
			toast.getView().setBackgroundColor(Color.RED);
			toast.show();

		}
		setUpQuestion();
	}
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
		return true;
	}
	
	
	

}
