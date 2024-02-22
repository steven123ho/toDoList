package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class taskList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        initListButton();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Getting the sort data before creating the contacts ArrayList
        String sortBy = getSharedPreferences("MyTaskListPreferences", Context.MODE_PRIVATE).getString("sortfield", "subject");
        String sortOrder = getSharedPreferences("MyTaskListPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");
        taskDataSource ds = new taskDataSource(this);
        ArrayList<Task> tasks;

        try {
            ds.open();
            tasks = ds.getTasks(sortBy, sortOrder);
            ds.close();
            RecyclerView taskList = findViewById(R.id.rvTask);
            taskAdapter taskAdapter = new taskAdapter(tasks, taskList.this);


            //If there are no contacts, open the Main Activity rather than contact list first
            if (tasks.size() > 0) {
                taskList = findViewById(R.id.rvTask);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                taskList.setLayoutManager(layoutManager);
                taskList.setAdapter(taskAdapter);
            } else {
                Intent intent = new Intent(taskList.this, MainActivity.class);
                startActivity(intent);
            }


            // Added the ItemClickListener here for the class contact to have been initialized
            //Book said to add it before the onCreate method
            View.OnClickListener onItemClickListener = new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                    RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                    int position = viewHolder.getAdapterPosition();
                    int taskID = tasks.get(position).getTaskId();
                    Intent intent = new Intent(taskList.this, MainActivity.class);
                    intent.putExtra("taskID", taskID);
                    startActivity(intent);
                }
            };
            taskAdapter.setmOnClickListener(onItemClickListener);

            //Switch turns delete buttons on and off
            //Book did not say to put here but the contactAdapter is here
            Switch s = findViewById(R.id.switchDelete);
            s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    Boolean status = compoundButton.isChecked();
                    taskAdapter.setDelete(status);
                    taskAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e){
            Toast.makeText(this, "Error retrieving tasks", Toast.LENGTH_LONG).show();
        }
    }



    //Navigation to new task and to settings
    private void initListButton() {
        Button addButton = findViewById(R.id.addBtn);
        ImageButton settingButton = findViewById(R.id.settingBtn);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(taskList.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clears the stack trace
                startActivity(intent);
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(taskList.this, settings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clears the stack trace
                startActivity(intent);
            }
        });
    }


}