package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class task_info extends AppCompatActivity {

    TextView txt1,txt3,txt5,txt7;
    Button b1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

        Intent intent = getIntent();
        String title = intent.getExtras().getString("task_title");

        txt1 = (TextView) findViewById(R.id.textView1);
        txt3 = (TextView) findViewById(R.id.textView3);
        txt5 = (TextView) findViewById(R.id.textView5);
        txt7 = (TextView) findViewById(R.id.textView7);
        b1 = (Button) findViewById(R.id.button);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Tasks").child(title);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Task task = new Task();
                 task = snapshot.getValue(Task.class);
                txt1.setText(title);
                txt3.setText(task.getDate());
                txt5.setText(task.getTime());
                txt7.setText(task.getDescrip());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.removeValue();
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
                Toast.makeText(task_info.this, "task deleted", Toast.LENGTH_LONG).show();
            }
        });



    }
}