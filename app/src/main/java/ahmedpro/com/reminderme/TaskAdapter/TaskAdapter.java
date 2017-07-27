package ahmedpro.com.reminderme.TaskAdapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ahmedpro.com.reminderme.DataBase.TasksDataSource;
import ahmedpro.com.reminderme.Model.Task;
import ahmedpro.com.reminderme.MyReceiverAlarm1;
import ahmedpro.com.reminderme.R;

/**
 * Created by hp on 16/04/2017.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    List<Task> tasks;
    List<Task> tasks2;
    Context context;
    TasksDataSource dataSource;
    String AM_PM = "";
    int iTime_Formate = 0;
    String sTime_Formate = " AM";
    int min = 0;
    int minute;
    String[] timeSplit;
    private static String title;


    public TaskAdapter(List<Task> tasks, Context context, TasksDataSource dataSource) {
        this.tasks = tasks;
        this.context = context;
        this.dataSource = dataSource;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_task_list, parent, false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.tvTaskName.setText("");
        holder.tvTaskName.setText(task.getName());
        holder.checkBox.setChecked(false);

        try {
            sTime_Formate = task.getTime().substring(0, 2);
        } catch (NullPointerException n) {

        }
        try {
            iTime_Formate = Integer.parseInt(sTime_Formate);
            if (iTime_Formate > 12) {
                iTime_Formate -= 12;
                AM_PM = " PM";
                sTime_Formate = String.valueOf(iTime_Formate) + task.getTime().substring(2, task.getTime().length());
                holder.tvDate.setText(sTime_Formate + AM_PM);
            } else if (iTime_Formate == 10 || iTime_Formate == 11) {
                AM_PM = " AM";
                holder.tvDate.setText(task.getTime() + AM_PM);
            } else {
                AM_PM = " PM";
                holder.tvDate.setText(task.getTime() + AM_PM);
            }

        } catch (NumberFormatException e) {
            String t = "";
            int i = 0;
            try {
                i = Integer.parseInt(task.getTime().substring(0, 1));
            } catch (NullPointerException n) {

            }
            if (i == 0) {
                AM_PM = " AM";
                i += 12;
                try {
                    t = String.valueOf(i) + task.getTime().substring(1, task.getTime().length());
                } catch (NullPointerException n) {

                }
                holder.tvDate.setText(t + AM_PM);
            } else {
                AM_PM = " AM";
                holder.tvDate.setText(task.getTime() + AM_PM);
            }
        }
        //time_Formate = Integer.parseInt(task.getTime());

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void update(ArrayList<Task> tasks) {
        if (tasks != null && tasks.size() > 0) {
            this.tasks.clear();
            this.tasks.addAll(tasks);
        }
    }


    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvTaskName, tvDate;
        CheckBox checkBox;

        public TaskViewHolder(View view) {
            super(view);
            tvTaskName = (TextView) view.findViewById(R.id.tvTaskName);
            tvDate = (TextView) view.findViewById(R.id.tvDateAndTime);
            checkBox = (CheckBox) view.findViewById(R.id.cbCheck);
            checkBox.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Task task = tasks.get(getAdapterPosition());
            if (checkBox.isChecked()) {
                dataSource.deleteTask(task.getName());
                tvTaskName.setText(task.getName() + " ( Is done ) ");

                try {
                    tasks2 = dataSource.getAllTasks();

                    timeSplit = tasks2.get(0).getTime().split(":");
                    min = Integer.parseInt(timeSplit[0]);
                    minute = Integer.parseInt(timeSplit[1]);
                    title = tasks.get(0).getName();
                    for (int i = 1; i < tasks2.size(); i++) {
                        timeSplit = tasks2.get(i).getTime().split(":");
                        if (Integer.parseInt(timeSplit[0]) < min) {
                            min = Integer.parseInt(timeSplit[0]);
                            minute = Integer.parseInt(timeSplit[1]);
                            title = tasks2.get(i).getName();
                        } else if (Integer.parseInt(timeSplit[0]) == min) {
                            if (Integer.parseInt(timeSplit[1]) < minute) {
                                min = Integer.parseInt(timeSplit[0]);
                                minute = Integer.parseInt(timeSplit[1]);
                                title = tasks2.get(i).getName();
                            }

                        }
                    }
                    Toast.makeText(context, "Title before" + title, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(context, MyReceiverAlarm1.class);
                    intent.putExtra("Uniqid", "From Adapter");
                    intent.putExtra("title2", title);
                    Toast.makeText(context, "Title after" + title, Toast.LENGTH_SHORT).show();

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, min);
                    cal.set(Calendar.MINUTE, minute);
                    cal.set(Calendar.SECOND, 0);
                    Log.v("Date Full", cal.getTime().toString());
                    Log.v("Hour", String.valueOf(min));
                    Log.v("Minute", String.valueOf(minute));
                    Log.v("Time SQL", tasks2.get(0).getTime());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            context, 2, intent, 0);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                            pendingIntent);
                } catch (Exception e) {

                }


                //tvTaskName.setPaintFlags(tvTaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                Toast.makeText(context, task.getName() + " Is Deleted " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
            if (!checkBox.isChecked()) {
                dataSource.addTask(task);
                tvTaskName.setText(task.getName());

                Toast.makeText(context, task.getName() + " Is UnChecked ", Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        public boolean onLongClick(View v) {
            Toast.makeText(context, getAdapterPosition() + " Long Click ", Toast.LENGTH_SHORT).show();
            // ToDO
            // UpadteFn();
            // ToDO
            return false;
        }
    }

    public static String getTilt2() {
        return title;
    }
}
