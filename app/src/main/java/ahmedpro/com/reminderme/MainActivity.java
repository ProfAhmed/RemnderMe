package ahmedpro.com.reminderme;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ahmedpro.com.reminderme.DataBase.TasksDataSource;
import ahmedpro.com.reminderme.Model.Task;
import ahmedpro.com.reminderme.TaskAdapter.TaskAdapter;

public class MainActivity extends AppCompatActivity {

    TaskAdapter adapter;
    Context context = this;
    RecyclerView recyclerView;
    List<Task> tasks = new ArrayList<>();
    TasksDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new TasksDataSource(this);
        dataSource.open();
        tasks = dataSource.getAllTasks();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        adapter = new TaskAdapter(tasks, this, dataSource);
        recyclerView.setAdapter(adapter);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddTaskDialog(context, dataSource, adapter);
            }
        });
    }

    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }
}
