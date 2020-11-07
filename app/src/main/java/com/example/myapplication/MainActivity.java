package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Tasks");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                Task t = new Task();
                for(DataSnapshot datasnap : snapshot.getChildren()) {
                    t.setTitle((datasnap.getValue(Task.class)).getTitle());
                    list.add(t.getTitle());
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