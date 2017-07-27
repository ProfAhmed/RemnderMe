package ahmedpro.com.reminderme.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

import ahmedpro.com.reminderme.Model.Task;

import static android.R.attr.id;

/**
 * Created by hp on 16/04/2017.
 */

public class TasksDataSource {
    private SQLiteDatabase database;
    private DbHelper dbHelper;
    private String[] allColumns = {dbHelper.COLUMN_ID,
            dbHelper.COLUMN_TASK, dbHelper.COLUMN_TIME};

    public TasksDataSource(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addTask(String taskName) {
        ContentValues values = new ContentValues();
        values.put(dbHelper.COLUMN_TASK, taskName);
        database.insert(dbHelper.TABLE_TASKS, null, values);
    }

    public void addTask(Task task) {
        ContentValues values = new ContentValues();
        values.put(dbHelper.COLUMN_TASK, task.getName());
        values.put(dbHelper.COLUMN_TIME, task.getTime());
        database.insert(dbHelper.TABLE_TASKS, null, values);
    }

    public void deleteTask(String taskName) {
        System.out.println("Comment deleted with id: " + id);

        database.delete(dbHelper.TABLE_TASKS, dbHelper.COLUMN_TASK + " = ?",
                new String[]{taskName});
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<Task>();

        Cursor cursor = database.query(dbHelper.TABLE_TASKS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Task task = cursorToComment(cursor);
            tasks.add(task);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return tasks;
    }

    private Task cursorToComment(Cursor cursor) {
        Task task = new Task();
        task.setId(cursor.getInt(0));
        task.setName(cursor.getString(1));
        task.setTime(cursor.getString(2));
        return task;
    }
}
