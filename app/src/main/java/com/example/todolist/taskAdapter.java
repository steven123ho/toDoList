package com.example.todolist;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class taskAdapter extends RecyclerView.Adapter {

    private ArrayList<Task> taskData;
    private View.OnClickListener mOnClickListener;
    private boolean isDeleting;
    private Context parentContext;

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        public TextView textSubject;
        public TextView textPriority;
        public TextView textDueDate;
        public Button deleteButton;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textSubject = (TextView) itemView.findViewById(R.id.textSubject);
            textPriority = (TextView) itemView.findViewById(R.id.textViewPriority);
            textDueDate = (TextView) itemView.findViewById(R.id.textDueDate);
            deleteButton = itemView.findViewById(R.id.buttonDeleteTask);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnClickListener);
        }

        public TextView getTextSubject() {
            return textSubject;
        }
        public TextView getTextPriority() {return textPriority;}
        public TextView getTextDueDate() {return textDueDate;}
        public Button getDeleteButton() {return deleteButton;}
    }

    public taskAdapter (ArrayList<Task> arrayList, Context context) {
        taskData = arrayList;
        parentContext = context;
    }

    public void setmOnClickListener(View.OnClickListener itemClickListener) {
        mOnClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull RecyclerView.ViewHolder holder, final int position) {
        TaskViewHolder cvh = (TaskViewHolder) holder;
        cvh.getTextSubject().setText((taskData.get(position)).getSubject());
        cvh.getTextPriority().setText(taskData.get(position).getPriority());
        cvh.getTextDueDate().setText(DateFormat.format("MM/dd/yyyy", taskData.get(position).getDueDate()));

        if (isDeleting) {
            cvh.getDeleteButton().setVisibility(View.VISIBLE);
            cvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                    deleteItem(holder.getAdapterPosition());
                }
            });
        } else {
            cvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }

    public void setDelete(boolean b) {
        isDeleting = b;
    }

    private void deleteItem(int position) {
        Task task = taskData.get(position);
        taskDataSource ds = new taskDataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteTask(task.getTaskId());
            ds.close();
            if (didDelete) {
                taskData.remove(position);
                notifyDataSetChanged();
            } else {
                Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return taskData.size();
    }
}