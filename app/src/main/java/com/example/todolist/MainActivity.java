package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.SaveDateListener {

    private Task currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            initTask(extras.getInt("taskID"));
        } else {
            currentTask = new Task();
        }

        initTextChangedEvents();
        initSaveButton();
        initChangeDateButton();
        initListButton();
    }

    @Override
    public void didFinishDatePickerDialog(Calendar selectedTime) {
        TextView dateLabel = findViewById(R.id.dateLabel);
        dateLabel.setText(DateFormat.format("MM/dd/yyyy", selectedTime));
        currentTask.setDueDate(selectedTime);
    }

    //Change Birthday Button
    private void initChangeDateButton() {
        Button changeDate = findViewById(R.id.changeDateButton);
        changeDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                DatePickerDialog datePickerDialog = new DatePickerDialog();
                datePickerDialog.show(fm, "DatePick");
            }
        });
    }

    private void initListButton() {
        ImageButton listButton = findViewById(R.id.listButton);
        ImageButton settingButton = findViewById(R.id.settingsButton);

        settingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, settings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clears the stack trace
                startActivity(intent);
            }
        });

        listButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, taskList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clears the stack trace
                startActivity(intent);
            }
        });
    }


    private void initTextChangedEvents() {

        final EditText etSubject = findViewById(R.id.subjectInput);
        etSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                currentTask.setSubject(etSubject.getText().toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                // autogenerated method for Text Watcher
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // autogenerated method for Text Watcher
            }
        });

        final EditText etDescription = findViewById(R.id.descriptionInput);
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                currentTask.setDescription(etDescription.getText().toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                // autogenerated method for Text Watcher
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // autogenerated method for Text Watcher
            }
        });

        //for the radio buttons to add on click
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioHigh = findViewById(R.id.radioButtonHigh);
                RadioButton radioMedium = findViewById(R.id.radioButtonMedium);

                if (radioHigh.isChecked()) {
                    currentTask.setPriority("High");
                } else if (radioMedium.isChecked()) {
                    currentTask.setPriority("Medium");
                } else {
                    currentTask.setPriority("Low");
                }
            }
        });

    }

    private void initSaveButton () {

        Button saveButton = findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wasSuccessful;
                taskDataSource ds = new taskDataSource(MainActivity.this);
                try {

                    ds.open();

                    if(currentTask.getTaskId() == -1) {
                        wasSuccessful = ds.insertTask(currentTask);
                        if (wasSuccessful) {
                            int newId = ds.getLastTaskId();
                            currentTask.setTaskId(newId);
                        }
                    } else {
                        wasSuccessful = ds.updateTask(currentTask);
                    }
                    ds.close();
                } catch (Exception e) {
                    wasSuccessful = false;
                }
            }
        });
        Toast.makeText(MainActivity.this, "This Task has been saved", Toast.LENGTH_LONG).show();
    }

    //Gets specific contact from ContactDataSource and uses it to populate the fields
    private void initTask (int id) {

        taskDataSource ds = new taskDataSource(MainActivity.this);
        try {
            ds.open();
            currentTask = ds.getSpecificTask(id);
            ds.close();
        } catch (Exception e) {
            Toast.makeText(this,"Load Task Failed", Toast.LENGTH_LONG).show();
        }

        EditText editSubject = findViewById(R.id.subjectInput);
        EditText editDescription = findViewById(R.id.descriptionInput);
        TextView dueDate = findViewById(R.id.dateLabel);

        editSubject.setText(currentTask.getSubject());
        editDescription.setText(currentTask.getDescription());
        dueDate.setText(DateFormat.format("MM/dd/yyyy", currentTask.getDueDate().getTimeInMillis()).toString());

        //setting radio button
        RadioButton high = findViewById(R.id.radioButtonHigh);
        RadioButton medium = findViewById(R.id.radioButtonMedium);
        RadioButton low = findViewById(R.id.radioButtonLow);

        if ("High".equals(currentTask.getPriority())) {
            high.setChecked(true);
        } else if ("Medium".equals(currentTask.getPriority())) {
            medium.setChecked(true);
        } else {
            low.setChecked(true);
        }
    }

}