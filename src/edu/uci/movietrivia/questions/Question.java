package edu.uci.movietrivia.questions;

import java.util.ArrayList;
import java.util.Collections;

public class Question {

	private String question;
	private String correctAnswer;
	private ArrayList<String> answers;
	private int correctOption;
	
	public Question(String question)
	{
		this.question = question;
		answers = new ArrayList<String>();
		
	}
	public int answersSize()
	{
		return answers.size();
	}
	public void setCorrectAnswer(String answer)	
	{
		this.correctAnswer = answer;
	}
	
	public void shuffleAnswersAndSetCorrectOption()
	{
		Collections.shuffle(answers);
		for(int i = 0; i<answers.size();i++)
		{
			if(answers.get(i).equals(correctAnswer))
				{
				correctOption = i;
				break;
				}
		}
		
		
	}
	public String getCorrectAnswer()
	{
		return correctAnswer;
	}
	public String getQuestion()
	{
		return question;			
	}
	
	public void insertAnswer(String answer)
	{
		answers.add(answer);
	}

	public int getCorrectOption() {
		return correctOption;
	}
	
	public String getOptionA()
	{
		return answers.get(0);
	}
	
	public String getOptionB()
	{
		return answers.get(1);
	}
	
	public String getOptionC()
	{
		return answers.get(2);
	}
	
	public String getOptionD()
	{
		return answers.get(3);
	}
	
	
	
	
}
