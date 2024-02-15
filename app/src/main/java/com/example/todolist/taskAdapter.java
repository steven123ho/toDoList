package com.example.todolist;

import android.content.Context;
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

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public TextView textSubject;
        public TextView textPriority;
        public Button deleteButton;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textSubject = (TextView) itemView.findViewById(R.id.textSubject);
            textPriority = (TextView) itemView.findViewById(R.id.prio);
            deleteButton = itemView.findViewById(R.id.buttonDeleteTask);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnClickListener);
        }

        public TextView getContactTextView() {
            return textViewContact;
        }
        public TextView getPhoneTextView() {return textPhone;}
        public Button getDeleteButton() {return deleteButton;}
    }

    public ContactAdapter (ArrayList<Contact> arrayList, Context context) {
        contactData = arrayList;
        parentContext = context;
    }

    public void setmOnClickListener(View.OnClickListener itemClickListener) {
        mOnClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull RecyclerView.ViewHolder holder, final int position) {
        ContactViewHolder cvh = (ContactViewHolder) holder;
        cvh.getContactTextView().setText((contactData.get(position)).getContactName());
        cvh.getPhoneTextView().setText(contactData.get(position).getPhoneNumber());

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
        Contact contact = contactData.get(position);
        ContactDataSource ds = new ContactDataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteContact(contact.getContactID());
            ds.close();
            if (didDelete) {
                contactData.remove(position);
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
        return contactData.size();
    }
}