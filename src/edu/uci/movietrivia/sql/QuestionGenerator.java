package edu.uci.movietrivia.sql;

import java.util.Random;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import edu.uci.movietrivia.questions.Question;

public class QuestionGenerator {

	public static Question getRandomQuestion()
	{
		Random gen = new Random();
		int num = gen.nextInt(8)+1;
		Question toReturn = null;
		
		switch(num){
		
		case 1:
			toReturn = question1();
			break;
		case 2:
			toReturn = question2();
			break;
		case 3:
			toReturn = question3();
			break;
		case 4:
			toReturn = question4();
			break;
		case 5:
			toReturn = question5();
			break;
		case 6:
			toReturn = question6();
			break;
		case 7:
			toReturn = question7();
			break;
		case 8:
			toReturn = question8();
			break;
		
		}
		
		return toReturn;
		
	}
	
	public static Question question1()
	{
		SQLiteDatabase mDB = DatabaseHelper.getReadableDB();
		Cursor results = mDB.query("movies", new String[]{"title","director"},null, null, null, null, "RANDOM()", "1");
		Question q = null;
		String correctAnswer ="";

		while(results.moveToNext())
		{
			String title = (results.getString(results.getColumnIndex("title")));
			q = new Question("Who directed the movie: "+title + "?");
			correctAnswer = results.getString(results.getColumnIndex("director"));
			q.setCorrectAnswer(correctAnswer);
			q.insertAnswer(correctAnswer);
		}

		results = mDB.query("movies", new String[]{"director"}, "director != ?" , new String[]{correctAnswer}, null, null, "RANDOM()","3");
		while(results.moveToNext())
		{
			q.insertAnswer(results.getString(results.getColumnIndex("director")));
		}
		results.close();
		q.shuffleAnswersAndSetCorrectOption();

		return q;

	}

	//When was the movie X released?
	public static Question question2()
	{
		SQLiteDatabase mDB = DatabaseHelper.getReadableDB();
		Cursor results = mDB.query("movies", new String[]{"title","year"},null, null, null, null, "RANDOM()", "1");
		Question q = null;
		String correctAnswer ="";
		Double correctDouble = null;
		while(results.moveToNext())
		{
			String title = (results.getString(results.getColumnIndex("title")));
			q = new Question("When was "+title+" released?");
			correctDouble = results.getDouble(results.getColumnIndex("year"));
			correctAnswer = Integer.toString(((int)(double)results.getDouble(results.getColumnIndex("year"))));
			q.setCorrectAnswer(correctAnswer);
			q.insertAnswer(correctAnswer);
		}

		//results = mDB.query("movies", new String[]{"year"}, "year != ?" , new String[]{Double.toString(correctDouble)}, null, null, "RANDOM()","3");
		//avoid duplicate years 
		results = mDB.rawQuery("select distinct year from movies where year != ? order by random() limit 3", new String[] {Double.toString(correctDouble)});
		while(results.moveToNext())
		{
			q.insertAnswer(results.getString(results.getColumnIndex("year")));
		}
		results.close();
		q.shuffleAnswersAndSetCorrectOption();

		return q;

	}


	//Which star (was/was not) in the movie X?

	public static Question question3()
	{
		SQLiteDatabase mDB = DatabaseHelper.getReadableDB();
		Question q = null;
		String correctAnswer ="";
		String movieID = null;

		Cursor results = mDB.query("movies, stars, stars_in_movies", new String[]{"title","movie_id","first_name","last_name"},"movies.id = movie_id and stars.id = star_id", null, null, null, "RANDOM()", "1");

		while(results.moveToNext())
		{	
			//Log.d("Here", "C");
			movieID = Integer.toString(results.getInt(results.getColumnIndex("movie_id")));

			String title = (results.getString(results.getColumnIndex("title")));
			q = new Question("Which star was in the movie: "+title+"?");
			correctAnswer = results.getString(results.getColumnIndex("first_name"))+" "+results.getString(results.getColumnIndex("last_name"));
			q.setCorrectAnswer(correctAnswer);
			q.insertAnswer(correctAnswer);
		}
		Log.d("Here", "D");
		results = mDB.query("stars, stars_in_movies", new String[]{"first_name","last_name"},"stars.id = star_id and star_id not in (select star_id from stars_in_movies where movie_id = ?)", new String[]{movieID}, null, null, "RANDOM()", "3");
		while(results.moveToNext())
		{
			q.insertAnswer(results.getString(results.getColumnIndex("first_name"))+" "+results.getString(results.getColumnIndex("last_name")));
		}
		results.close();
		q.shuffleAnswersAndSetCorrectOption();


		return q;

	}

	//In which movie the stars X and Y appear together?

	public static Question question4()
	{
		SQLiteDatabase mDB = DatabaseHelper.getReadableDB();
		Cursor results = mDB.query("stars_in_movies", new String[]{"movie_id"},null, null, "movie_id", "count(star_id) >=2", "RANDOM()", "1");
		String movieID = null;
		while(results.moveToNext())
		{
			movieID = Integer.toString(results.getInt(results.getColumnIndex("movie_id")));
		}
		results = mDB.query("stars_in_movies", new String[]{"star_id"},"movie_id = ?", new String[]{movieID}, null, null, "RANDOM()", "2");

		String[] starIDs = new String[2];
		int i = 0;
		while(results.moveToNext())
		{
			starIDs[i] = Integer.toString(results.getInt(results.getColumnIndex("star_id"))); 
			i++;
		}
		//results = mDB.query("stars", new String[]{"first_name","last_name"},"id = ? or id = ?",starIDs , null, null, "RANDOM()", "2");
		results = mDB.rawQuery("select distinct first_name, last_name from stars where first_name != last_name and last_name != first_name and id = ? or id = ? order by random() limit 2", starIDs);

		String firstStar = null;
		String secondStar = null;
		while(results.moveToNext())
		{
			firstStar = results.getString(results.getColumnIndex("first_name"))+ " "+ results.getString(results.getColumnIndex("last_name"));
			results.moveToNext();
			secondStar = results.getString(results.getColumnIndex("first_name"))+ " "+ results.getString(results.getColumnIndex("last_name"));
		}

		Question q = new Question("In which movie did the stars "+firstStar+" and "+secondStar+" appear together?");

		String correctAnswer ="";


		results = mDB.query("movies", new String[]{"title"},"id = ?",new String[]{movieID} , null, null, null, "1");
		while(results.moveToNext())
		{
			correctAnswer = results.getString(results.getColumnIndex("title"));
			q.setCorrectAnswer(correctAnswer);
			q.insertAnswer(correctAnswer);
		}

		//results = mDB.query("movies, stars_in_movies", new String[]{"title","movie_id"}, "id = movie_id and movie_id not in (select movie_id from stars_in_movies where star_id = ?)  ", new String[]{starIDs[0]}, null, null, "RANDOM()","3");
		results = mDB.rawQuery("select title,  movie_id from movies, stars_in_movies where id = movie_id and movie_id not in (select movie_id from stars_in_movies where star_id = ?) order by random() limit 3", new String[]{starIDs[0]});
		while(results.moveToNext())
		{
			q.insertAnswer(results.getString(results.getColumnIndex("title")));
		}
		results.close();
		q.shuffleAnswersAndSetCorrectOption();

		return q;

	}

	//Who directed/did not direct the star X?
	public static Question question5()
	{
		SQLiteDatabase mDB = DatabaseHelper.getReadableDB();
		//get a random star
		Cursor results = mDB.query("stars", new String[]{"first_name", "last_name", "id"}, null, null, null, null, "RANDOM()", "1");
		Question q = null;
		String correctAnswer = "";
		String firstName = "";
		String lastName = "";
		String starID = null;

		while(results.moveToNext())
		{
			firstName = results.getString(0);	
			lastName = results.getString(1);
			starID = Integer.toString(results.getInt(2));		
		}

		//next, get the director
		results = mDB.query("movies, stars, stars_in_movies", new String[]{"director"}, "stars_in_movies.movie_id = movies.id and stars_in_movies.star_id = stars.id and stars.id = ?", new String[]{starID}, null, null, null, "1");

		while(results.moveToNext())
		{
			q = new Question("Who directed the star " + firstName + " " + lastName + "?");
			correctAnswer = results.getString(0);
			q.setCorrectAnswer(correctAnswer);
			q.insertAnswer(correctAnswer);
		}

		//now get the wrong answers
		//results = mDB.query("movies as m", new String[]{"m.director"}, "m.director not in (select movies.director from movies, stars, stars_in_movies where stars_in_movies.movie_id = movies.id and stars_in_movies.star_id = stars.id and stars.id = ?)", new String[]{starID}, null, null, "RANDOM()", "5");

		results = mDB.rawQuery("select distinct m.director from movies as m where m.director not in (select distinct movies.director from movies, stars, stars_in_movies where stars_in_movies.movie_id = movies.id and stars_in_movies.star_id = stars.id and stars.id = ?) order by random() limit 3", new String[]{starID});
		String temp = "";

		while(results.moveToNext())
		{
			temp = results.getString(0);
			if(temp == null)
				break;
			q.insertAnswer(temp);
		}

		//requery if there's a null pointer exception 
		while(temp == null)
		{
			results = mDB.query("stars", new String[]{"first_name", "last_name", "id"}, null, null, null, null, "RANDOM()", "1");

			while(results.moveToNext())
			{
				firstName = results.getString(0);	
				lastName = results.getString(1);
				starID = Integer.toString(results.getInt(2));		
			}

			//next, get the director
			results = mDB.query("movies, stars, stars_in_movies", new String[]{"director"}, "stars_in_movies.movie_id = movies.id and stars_in_movies.star_id = stars.id and stars.id = ?", new String[]{starID}, null, null, null, "1");

			while(results.moveToNext())
			{
				q = new Question("Who directed the star " + firstName + " " + lastName + "?");
				correctAnswer = results.getString(0);
				q.setCorrectAnswer(correctAnswer);
				q.insertAnswer(correctAnswer);
			}

			//now get the wrong answers
			//results = mDB.query("movies as m", new String[]{"m.director"}, "m.director not in (select movies.director from movies, stars, stars_in_movies where stars_in_movies.movie_id = movies.id and stars_in_movies.star_id = stars.id and stars.id = ?)", new String[]{starID}, null, null, "RANDOM()", "5");

			results = mDB.rawQuery("select distinct m.director from movies as m where m.director not in (select distinct movies.director from movies, stars, stars_in_movies where stars_in_movies.movie_id = movies.id and stars_in_movies.star_id = stars.id and stars.id = ?) order by random() limit 3", new String[]{starID});
			while(results.moveToNext())
			{
				temp = results.getString(0);
				q.insertAnswer(temp);
			}
		}

		results.close();
		q.shuffleAnswersAndSetCorrectOption();
		return q;
	}

	public static Question question6()
	{
		SQLiteDatabase mDB = DatabaseHelper.getReadableDB();
		Cursor results = mDB.rawQuery("select distinct stars.first_name, stars.last_name, m.id, m.title, m2.id, m2.title from stars, movies as m, movies as m2, stars_in_movies as sm1, stars_in_movies as sm2 where stars.id = sm1.star_id and stars.id = sm2.star_id and m.id = sm1.movie_id and m2.id = sm2.movie_id and m.id != m2.id and m.title != m2.title order by random() limit 1", null);
		Question q = null;
		String correctAnswer = "";
		int movieID1 = 0;
		int movieID2 = 0;

		//instead of a while loop here, i decided to go in the order i specified in the query above
		//advance the cursor to the first element
		results.moveToFirst();
		//then for each column, get the result. this approach retains uniqueness among movie titles.
		String firstName = results.getString(0);
		String lastName = results.getString(1);
		movieID1 = results.getInt(2);
		String movieTitle1 = results.getString(3);
		movieID2 = results.getInt(4);	
		String movieTitle2 = results.getString(5);

		q = new Question("Which star appears in both " + movieTitle1 + " and " + movieTitle2 + "?");
		correctAnswer = firstName + " " + lastName;
		q.setCorrectAnswer(correctAnswer);
		q.insertAnswer(correctAnswer);

		String[] movieIDs = new String[]{Integer.toString(movieID1), Integer.toString(movieID2)};

		results = mDB.rawQuery("select distinct s1.first_name, s1.last_name from stars as s1 where (not exists (select distinct s2.first_name, s2.last_name from stars as s2, stars_in_movies as sm1 where s1.id = s2.id and s2.id = sm1.star_id and sm1.movie_id = ?)) or (not exists (select distinct s3.first_name, s3.last_name from stars as s3, stars_in_movies as sm2 where s1.id = s3.id and s3.id = sm2.star_id and sm2.movie_id = ?)) order by random() limit 3", movieIDs);
		while(results.moveToNext())
		{
			q.insertAnswer(results.getString(0) + " " + results.getString(1));
		}

		results.close();
		q.shuffleAnswersAndSetCorrectOption();
		return q;
	}

	public static Question question7()
	{
		SQLiteDatabase mDB = DatabaseHelper.getReadableDB();
		//query for one star
		Cursor results = mDB.rawQuery("select distinct s2.first_name, s2.last_name, s2.id from movies, stars as s1, stars as s2, stars_in_movies as sm1, stars_in_movies as sm2 where movies.id = sm1.movie_id and movies.id = sm2.movie_id and sm1.star_id = s1.id and sm2.star_id = s2.id and s1.id != s2.id order by random() limit 1", null);
		Question q = null;
		int starID = 0;
		String starFirstName = "";
		String starLastName = "";

		results.moveToFirst();
		starFirstName = results.getString(0);
		starLastName = results.getString(1);
		starID = results.getInt(2);

		//query for a star that isn't in the same movie as the star from above
		results = mDB.rawQuery("select distinct stars.first_name, stars.last_name from stars where stars.id not in (select distinct s2.id from movies, stars as s1, stars as s2, stars_in_movies as sm1, stars_in_movies as sm2 where movies.id = sm1.movie_id and movies.id = sm2.movie_id and sm1.star_id = s1.id and sm2.star_id = s2.id and s1.id != s2.id and s1.id = ? and s2.id = stars.id) order by random() limit 1", new String[]{Integer.toString(starID)});

		results.moveToFirst();
		String correctFirstName = results.getString(0);
		String correctLastName = results.getString(1);

		q = new Question("Which star did not appear in the same movie with " + starFirstName + " " + starLastName + "?");
		String correctAnswer = correctFirstName + " " + correctLastName;
		q.setCorrectAnswer(correctAnswer);
		q.insertAnswer(correctAnswer);

		//lastly, generate the wrong answers
		results = mDB.rawQuery("select distinct s2.first_name, s2.last_name from movies, stars as s1, stars as s2, stars_in_movies as sm1, stars_in_movies as sm2 where movies.id = sm1.movie_id and movies.id = sm2.movie_id and sm1.star_id = s1.id and sm2.star_id = s2.id and s1.id != s2.id and s1.id = ?", new String[]{Integer.toString(starID)});

		while(results.moveToNext())
		{	
			q.insertAnswer(results.getString(0) + " " + results.getString(1));
		}

		//requery to avoid arrayindexoutofbounds exception. some answer sets used to have 3 or 4 of
		//the same star, so in that case i just requery from the very beginning to have
		//every element be unique
		while(q.answersSize() <= 4)
		{
			results = mDB.rawQuery("select distinct s2.first_name, s2.last_name, s2.id from movies, stars as s1, stars as s2, stars_in_movies as sm1, stars_in_movies as sm2 where movies.id = sm1.movie_id and movies.id = sm2.movie_id and sm1.star_id = s1.id and sm2.star_id = s2.id and s1.id != s2.id order by random() limit 1", null);

			results.moveToFirst();
			starFirstName = results.getString(0);
			starLastName = results.getString(1);
			starID = results.getInt(2);

			//query for a star that isn't in the same movie as the star from above
			results = mDB.rawQuery("select distinct stars.first_name, stars.last_name from stars where stars.id not in (select distinct s2.id from movies, stars as s1, stars as s2, stars_in_movies as sm1, stars_in_movies as sm2 where movies.id = sm1.movie_id and movies.id = sm2.movie_id and sm1.star_id = s1.id and sm2.star_id = s2.id and s1.id != s2.id and s1.id = ? and s2.id = stars.id) order by random() limit 1", new String[]{Integer.toString(starID)});

			results.moveToFirst();
			correctFirstName = results.getString(0);
			correctLastName = results.getString(1);

			q = new Question("Which star did not appear in the same movie with " + starFirstName + " " + starLastName + "?");
			correctAnswer = correctFirstName + " " + correctLastName;
			q.setCorrectAnswer(correctAnswer);
			q.insertAnswer(correctAnswer);

			//lastly, generate the wrong answers
			results = mDB.rawQuery("select distinct s2.first_name, s2.last_name from movies, stars as s1, stars as s2, stars_in_movies as sm1, stars_in_movies as sm2 where movies.id = sm1.movie_id and movies.id = sm2.movie_id and sm1.star_id = s1.id and sm2.star_id = s2.id and s1.id != s2.id and s1.id = ?", new String[]{Integer.toString(starID)});

			while(results.moveToNext())
			{	
				q.insertAnswer(results.getString(0) + " " + results.getString(1));
			}
		}

		results.close();
		q.shuffleAnswersAndSetCorrectOption();
		return q;
	}

	//Who directed the star X in the year Y?
	public static Question question8()
	{
		SQLiteDatabase mDB = DatabaseHelper.getReadableDB();
		Cursor results = mDB.query("movies, stars, stars_in_movies", new String[]{"movies.director", "movies.year", "stars.id", "stars.first_name", "stars.last_name"}, "stars_in_movies.movie_id = movies.id and stars_in_movies.star_id = stars.id", null, null, null, "RANDOM()", "1");
		Question q = null;
		String correctAnswer = "";
		String firstName = "";
		String lastName = "";
		String starID = null;
		String year = null;

		while(results.moveToNext())
		{
			correctAnswer = results.getString(0);
			year = Integer.toString(results.getInt(1));
			starID = Integer.toString(results.getInt(2));
			firstName = results.getString(3);
			lastName = results.getString(4);

			q = new Question("Who directed the star " + firstName + " " + lastName + " in the year " + year + "?");
			q.setCorrectAnswer(correctAnswer);
			q.insertAnswer(correctAnswer);
		}

		results = mDB.query("movies as m", new String[]{"m.director"}, "m.director not in (select director from movies, stars, stars_in_movies where stars_in_movies.movie_id = movies.id and stars_in_movies.star_id = stars.id and year = " + year + " and stars.id = " + starID + ")", null, null, null, "RANDOM()", "3");
		while(results.moveToNext())
		{
			q.insertAnswer(results.getString(0));
		}
		results.close();
		q.shuffleAnswersAndSetCorrectOption();
		return q;
	}
	
	
	
	
	
}
