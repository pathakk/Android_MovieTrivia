package edu.uci.movietrivia.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class Stats {
//taken INTEGER, totalQuestions INTEGER, totalCorrect INTEGER, totalWrong INTEGER,averageTime DOUBLE
	
	
	public static void updateStats( int totalQuestions, int totalCorrect, int totalWrong, double averageTime)
	{
		
		SQLiteDatabase mDB = DatabaseHelper.getWriteableDB();
		Cursor results = mDB.rawQuery("select * from stats where id = 0;", null);
		int totalQuizzes = 1;
		while (results.moveToNext())
		{
			totalQuestions += results.getInt(results.getColumnIndex("totalQuestions"));
			totalCorrect += results.getInt(results.getColumnIndex("totalCorrect"));
			totalWrong += results.getInt(results.getColumnIndex("totalWrong"));
			totalQuizzes += results.getInt(results.getColumnIndex("taken"));
			averageTime += results.getDouble(results.getColumnIndex("averageTime"));
			averageTime/=2;
			
		}
		mDB.execSQL("UPDATE stats SET taken = ?, totalQuestions = ?, totalCorrect = ?, totalWrong = ?, averageTime =? where id = 0;"
				, new String[]{Integer.toString(totalQuizzes),Integer.toString(totalQuestions),Integer.toString(totalCorrect),Integer.toString(totalWrong),Double.toString(averageTime)});
		
	}
	
	
	
	

	public static String[] getStats()
	{
		
		String[] stats = new String[5];

		SQLiteDatabase mDB = DatabaseHelper.getReadableDB();
		
		
		Cursor results = mDB.rawQuery("select * from stats where id = 0;",null);
		
		while(results.moveToNext())
		{	
			stats[0] = Integer.toString(results.getInt(results.getColumnIndex("taken")));
			stats[1] = Integer.toString(results.getInt(results.getColumnIndex("totalQuestions")));
			stats[2] = Integer.toString(results.getInt(results.getColumnIndex("totalCorrect")));
			stats[3] = Integer.toString(results.getInt(results.getColumnIndex("totalWrong")));
			stats[4] = Double.toString(results.getInt(results.getColumnIndex("averageTime")));
			
		}
		return stats;
	}
	
}
