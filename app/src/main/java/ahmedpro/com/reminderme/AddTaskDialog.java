package ahmedpro.com.reminderme;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ahmedpro.com.reminderme.DataBase.TasksDataSource;
import ahmedpro.com.reminderme.Model.Task;
import ahmedpro.com.reminderme.TaskAdapter.TaskAdapter;

/**
 * Created by hp on 16/04/2017.
 */

public class AddTaskDialog {
    Context context;
    View customView;
    String taskName;
    EditText editTextTaskName;
    List<Task> tasks;
    TimePicker timePicker;
    int min = 0;
    int minute;
    String[] timeSplit;
    private static String title = "";


    public AddTaskDialog(final Context context, final TasksDataSource dataSource, final TaskAdapter adapter) {
        this.context = context;

        LayoutInflater li = LayoutInflater.from(context);
        customView = li.inflate(R.layout.task_create_dialog, null);
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle("Add New Task");
        dialog.setMessage("Enter The Name Of Your Task");
        dialog.setView(customView);

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editTextTaskName = (EditText) customView.findViewById(R.id.taskNameInput);
                timePicker = (TimePicker) customView.findViewById(R.id.timePicker);
                taskName = editTextTaskName.getText().toString();

                if (taskName.equals("")) {
                    Toast.makeText(context, "Please Enter Task Name Or Cancel ", Toast.LENGTH_SHORT).show();
                } else {
                    Task task = new Task(taskName, getCurrentTime());
                    dataSource.addTask(task);
                    tasks = dataSource.getAllTasks();

                    timeSplit = tasks.get(0).getTime().split(":");
                    min = Integer.parseInt(timeSplit[0]);
                    minute = Integer.parseInt(timeSplit[1]);
                    //title = tasks.get(0).getName();
                    //Toast.makeText(context, "Title" + tasks.get(0).getName(), Toast.LENGTH_SHORT).show();
                    for (int i = 1; i < tasks.size(); i++) {
                        timeSplit = tasks.get(i).getTime().split(":");
                        if (Integer.parseInt(timeSplit[0]) < min) {
                            min = Integer.parseInt(timeSplit[0]);
                            minute = Integer.parseInt(timeSplit[1]);
                            title = tasks.get(i).getName();


                        } else if (Integer.parseInt(timeSplit[0]) == min) {
                            if (Integer.parseInt(timeSplit[1]) < minute) {
                                min = Integer.parseInt(timeSplit[0]);
                                minute = Integer.parseInt(timeSplit[1]);
                                title = tasks.get(i).getName();
                            }

                        }
                    }
                    Toast.makeText(context, "Title before" + title, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MyReceiverAlarm1.class);
                    intent.putExtra("Uniqid", "From Add");
                    intent.putExtra("title", title);
                    Toast.makeText(context, "Title after" + title, Toast.LENGTH_SHORT).show();

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, min);
                    cal.set(Calendar.MINUTE, minute);
                    cal.set(Calendar.SECOND, 0);
                    Log.v("Date Full", cal.getTime().toString());
                    Log.i("Hour", String.valueOf(min));
                    Log.v("Minute", String.valueOf(minute));
                    Log.v("Time SQL", tasks.get(0).getTime());
                    Log.i("Title", title);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            context, 1, intent, 0);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                            pendingIntent);


                    adapter.update((ArrayList<Task>) tasks);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(context, taskName + " Is Added ", Toast.LENGTH_SHORT).show();

                }
            }
        });

        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Cancel", Toast.LENGTH_LONG).show();
            }
        });

        dialog.show();
    }


    public static String getTilt1() {
        return title;
    }

    public String getCurrentTime() {
        String currentTime = timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
        return currentTime;
    }
}
