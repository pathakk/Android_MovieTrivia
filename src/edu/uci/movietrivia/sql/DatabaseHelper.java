package edu.uci.movietrivia.sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String STARS_TABLE = "stars";
	private static final String MOVIES_TABLE = "movies";
	private static final String STARS_IN_MOVIES_TABLE = "stars_in_movies";

	// stars columns:
	private static final String STARS_KEY = "id";
	private static final String STARS_FIRSTNAME = "first_name";
	private static final String STARS_LASTNAME = "last_name";

	// Movie columns:
	private static final String MOVIE_DIRECTOR = "director";
	private static final String MOVIE_YEAR = "year";
	private static final String MOVIE_TITLE = "title";
	private static final String MOVIE_KEY = "id";

	// Stars_in_movies columns:
	private static final String STAR_ID = "star_id";
	private static final String MOVIE_ID = "movie_id";

	// Create table statements:

	private static final String CREATE_STARS = "CREATE TABLE " + STARS_TABLE
			+ " ( " + STARS_KEY + " INTEGER NOT NULL, " + STARS_FIRSTNAME
			+ " VARCHAR(50) NOT NULL, " + STARS_LASTNAME
			+ " VARCHAR(50) NOT NULL, PRIMARY KEY(" + STARS_KEY + "));";

	private static final String CREATE_MOVIES = "CREATE TABLE " + MOVIES_TABLE
			+ " ( " + MOVIE_KEY + " INTEGER NOT NULL, " + MOVIE_TITLE
			+ " VARCHAR(100) NOT NULL, " + MOVIE_YEAR + " INTEGER NOT NULL, "
			+ MOVIE_DIRECTOR + " VARCHAR(100) NOT NULL, PRIMARY KEY("
			+ MOVIE_KEY + "));";

	private static final String CREATE_STARS_IN_MOVIES = "CREATE TABLE "
			+ STARS_IN_MOVIES_TABLE + " ( " + STAR_ID + " INTEGER NOT NULL, "
			+ MOVIE_ID + " INTEGER NOT NULL, " + "FOREIGN KEY(" + STAR_ID
			+ ") REFERENCES " + STARS_TABLE + "(" + STARS_KEY
			+ "), FOREIGN KEY(" + MOVIE_ID + ") REFERENCES " + MOVIES_TABLE
			+ "(" + MOVIE_KEY + "));";

	private static SQLiteDatabase mWriteableDB;
	private static SQLiteDatabase mReadableDB;
	private static final String DATABASE_NAME = "DB";
	private static final int DB_VERSION = 1;
	private Context mContext;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DB_VERSION);
		mContext = context;
		DatabaseHelper.mWriteableDB = getWritableDatabase();
		DatabaseHelper.mReadableDB = getReadableDatabase();
	}

	static SQLiteDatabase getWriteableDB()
	{
		return mWriteableDB;
	}
	static SQLiteDatabase getReadableDB()
	{
		return mReadableDB;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_STARS);
		db.execSQL(CREATE_MOVIES);
		db.execSQL(CREATE_STARS_IN_MOVIES);
		db.execSQL("CREATE TABLE stats (id INTEGER ,taken INTEGER, totalQuestions INTEGER, totalCorrect INTEGER, totalWrong INTEGER, averageTime DOUBLE, PRIMARY KEY(id));");
		try {
			loadStars(db);
			loadMovies(db);
			loadStarsInMovies(db);
			loadStats(db);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.getStackTraceString(e);
		}

		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + STARS_IN_MOVIES_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + STARS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + MOVIES_TABLE);
		db.execSQL("DROP TABLE IS EXISTS stats");
		onCreate(db);
	}

	public void loadStats(SQLiteDatabase db)
	{
		db.execSQL("INSERT INTO stats(id, taken, totalQuestions,totalCorrect,totalWrong, averageTime) VALUES(0,0,0,0,0,0)");
		
	}
	public void loadMovies(SQLiteDatabase db) throws IOException {

		AssetManager assets = mContext.getAssets();

		InputStream istream = assets.open("movies.csv");

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				istream));

		String line;
		db.beginTransaction();
		while ((line = reader.readLine()) != null) {

			SQLiteStatement stmt = db
					.compileStatement("INSERT INTO movies(id,title,year,director) VALUES(?,?,?,?);");
			String[] str = line.split(",");
			
			stmt.bindDouble(1, Double.parseDouble(str[0]));
			stmt.bindString(2, str[1]);
			stmt.bindDouble(3, Double.parseDouble(str[2]));
			stmt.bindString(4, str[3]);
			stmt.execute();

		}
		db.setTransactionSuccessful();
		db.endTransaction();

	}

	public void loadStars(SQLiteDatabase db) throws IOException {

		AssetManager assets = mContext.getAssets();

		InputStream istream = assets.open("stars.csv");

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				istream));

		String line;
		db.beginTransaction();
		while ((line = reader.readLine()) != null) {

			SQLiteStatement stmt = db
					.compileStatement("INSERT INTO stars(id,first_name,last_name) VALUES(?,?,?);");
			String[] str = line.split(",");
			stmt.bindDouble(1, Double.parseDouble(str[0]));
			stmt.bindString(2, str[1]);
			stmt.bindString(3, str[2]);
			stmt.execute();

		}
		db.setTransactionSuccessful();
		db.endTransaction();

	}

	public void loadStarsInMovies(SQLiteDatabase db) throws IOException {

		AssetManager assets = mContext.getAssets();

		InputStream istream = assets.open("stars_in_movies.csv");

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				istream));

		String line;
		db.beginTransaction();
		while ((line = reader.readLine()) != null) {

			SQLiteStatement stmt = db
					.compileStatement("INSERT INTO stars_in_movies(star_id,movie_id) VALUES(?,?);");
			String[] str = line.split(",");
			stmt.bindDouble(1, Double.parseDouble(str[0]));
			stmt.bindDouble(2, Double.parseDouble(str[1]));
			stmt.execute();

		}
		db.setTransactionSuccessful();
		db.endTransaction();

	}

	
	
	
}
