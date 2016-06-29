package com.abs.samih.fcm_ex01.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.abs.samih.fcm_ex01.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ort on 27/06/2016.
 */
//1                         //2
public class MyAdapterTask extends ArrayAdapter<MyTask> {
    //resurece is the xmlFile with the layout
    private  DatabaseReference reference;

    //context this is the activity that we are using
    //3
    public MyAdapterTask(Context context, int resource)
    {
        super(context, resource);
        //10
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","_");
        //11
        reference = FirebaseDatabase.getInstance().getReference(email).child("MyTasks");
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       //5
        final MyTask myTask= getItem(position);
//        if (convertView ==  null)
//        {
//            //make sure convert view is build
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_my_task,parent,false);
//        }

        //6.
        CheckBox chbxIsComplated = (CheckBox) convertView.findViewById(R.id.chbxIsComplated);
        TextView tvText = (TextView) convertView.findViewById(R.id.tvText);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        ImageButton btnCall = (ImageButton) convertView.findViewById(R.id.btnCall);
        ImageButton btnLoction = (ImageButton) convertView.findViewById(R.id.btnLocation);

        //7
        chbxIsComplated.setChecked(myTask.isComplated());
        tvText.setText(myTask.getText());
        tvDate.setText(myTask.getText());
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make call
            }
        });

        btnLoction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to map
            }
        });

        chbxIsComplated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                myTask.setComplated(isChecked);
                //12 this updates the check box in the firebase
                reference.child(myTask.getTaskKey()).setValue(myTask);
            }
        });

        //convertView shows a line in List View
        return convertView;
    }

}
