package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton button;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (FloatingActionButton) findViewById(R.id.newtaskbutton);
        listview = (ListView) findViewById(R.id.listview);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewtask();
            }
        });

        ArrayList<String> list = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_item,
                R.id.txt, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(getBaseContext(), task_info.class );
                intent.putExtra("task_title", selectedItem);
                startActivity(intent);
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Tasks");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                Task t = new Task();
                for(DataSnapshot datasnap : snapshot.getChildren()) {
                    t = (Objects.requireNonNull(datasnap.getValue(Task.class)));
                    String date[] = t.getDate().split("-");
                    int startTime[] = {0,0,0,0,0};
                    for(int i=0; i<3; i++) {
                        startTime[i] = Integer.parseInt(date[2-i]);
                    }
                    String time[] = t.getTime().split(":");
                    startTime[3] = Integer.parseInt(time[0])-1;
                    startTime[4] = Integer.parseInt(time[1]);
                    startTime[1]--;
                    Calendar beginTime = Calendar.getInstance();
                    beginTime.set(startTime[0], startTime[1], startTime[2], startTime[3], startTime[4]);
                    Date currentTime = Calendar.getInstance().getTime();
                    if(beginTime.getTimeInMillis() > currentTime.getTime())
                        list.add(t.getTitle());
                    else
                        ref.child("Tasks").child(t.getTitle()).removeValue();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }



        });
    }

    private void openNewtask() {
        Intent intent = new Intent(this, new_task.class);
        startActivity(intent);
    }


}