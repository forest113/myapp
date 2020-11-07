package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import static java.util.logging.Logger.global;

public class new_task extends AppCompatActivity {

    EditText txtTitle, textDescrip, txtDate, txtTime;
    Button savebtn, cancelbtn;
    DatabaseReference ref;
    Task task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        txtTitle = (EditText) findViewById(R.id.editTitle);
        textDescrip = (EditText) findViewById(R.id.editDescrip);
        savebtn = (Button) findViewById(R.id.savebtn);
        cancelbtn = (Button) findViewById(R.id.cancelbtn);
        txtDate = (EditText) findViewById(R.id.editDate);
        txtTime = (EditText) findViewById(R.id.editTime);



        ref = FirebaseDatabase.getInstance().getReference().child("Tasks");
        task = new Task();

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check = 0;
                String d = txtDate.getText().toString().trim();
                String date[] = d.split("-");
                int startTime[] = {0,0,0,0,0};
                for(int i=0; i<3; i++) {
                    startTime[i] = Integer.parseInt(date[2-i]);
                }
                String t[] = txtTime.getText().toString().trim().split(":");
                startTime[3] = Integer.parseInt(t[0]);
                startTime[4] = Integer.parseInt(t[1]);
                startTime[2]--;
                int endT[] = {0,0,0,0,0};
                endT = startTime;
                endT[3]++;
                task.setDate(d);
                task.setTime(txtTime.getText().toString().trim());
                task.setTitle(txtTitle.getText().toString().trim());
                task.setDescrip(textDescrip.getText().toString().trim());
                ref.push().setValue(task);
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(startTime[0], startTime[1], startTime[2], startTime[3], startTime[4]);
                Calendar endTime = Calendar.getInstance();
                endTime.set(endT[0], endT[1], endT[2], endT[3], endT[4]);
                Intent intent = new Intent(Intent.ACTION_INSERT);
                        intent.setData(CalendarContract.Events.CONTENT_URI);
                        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
                        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
                        intent.putExtra(CalendarContract.Events.TITLE, task.getTitle().toString());
                        intent.putExtra(CalendarContract.Events.DESCRIPTION, task.getDescrip().toString());
                if(intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                    check = 1;
                }
                else {
                    Toast.makeText(new_task.this, "No app", Toast.LENGTH_LONG).show();
                }
                if(check == 1) {
                    Toast.makeText(new_task.this, "task created", Toast.LENGTH_LONG).show();
                    returnToHome();
                    startActivity(intent);
                }

                    }
        });
        cancelbtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToHome();
                    }
        }));




    }

    private void returnToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}