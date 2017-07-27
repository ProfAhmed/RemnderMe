package ahmedpro.com.reminderme.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hp on 16/04/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String TABLE_TASKS = "TASKS";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TASK = "task";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DATE = "date";

    private static final String DATABASE_NAME = "REMINDER.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_TASKS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TASK
            + " VARCHAR(255), " + COLUMN_TIME + " VARCHAR(255), " + COLUMN_DATE + " VARCHAR(255));";


    public DbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}
