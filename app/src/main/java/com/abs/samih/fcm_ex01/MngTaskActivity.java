package com.abs.samih.fcm_ex01;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.abs.samih.fcm_ex01.data.MyAdapterTask;
import com.abs.samih.fcm_ex01.data.MyTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MngTaskActivity extends AppCompatActivity {
    //private MyTaskAdapter myTaskAdapter;
    //5
    private MyAdapterTask adapterTask;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mng_task_activity);
        //6
        listView = (ListView) findViewById(R.id.listView);
        adapterTask = new MyAdapterTask(this,R.layout.item_my_task);
        //7
        listView.setAdapter(adapterTask);

//1.    get fixed email
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","_");

        //2 this is the head of the database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(email);

        //3
        // value event listner for all changes
        reference.child("MyTasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //8
                adapterTask.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    //4
                    MyTask myTask= ds.getValue(MyTask.class);// tells it to convert my task
                    myTask.setTaskKey(ds.getKey());
                    //Add MyTask to adapter works on firbase only
                    //9
                    adapterTask.add(myTask);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(MngTaskActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}
