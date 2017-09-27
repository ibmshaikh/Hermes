package com.example.ibm.hermes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;

public class Enter_Group extends AppCompatActivity {


    private String chk;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private Integer flag;
    private String id;
    ProgressDialog mProgressDialog;
    private EditText minsert;
    private EditText minsert2;
    private Button msave;
    private String name;
    private ArrayList stock_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter__group);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference =firebaseDatabase.getReference();

         minsert = (EditText) findViewById(R.id.editText);
         minsert2 = (EditText) findViewById(R.id.editText3);
         msave = (Button) findViewById(R.id.button2);
         msave.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 name = minsert.getText().toString();
                 id = minsert2.getText().toString();
                 chk = name + id;
                 if (name.isEmpty() && id.isEmpty()) {
                     Toast.makeText(Enter_Group.this.getApplicationContext(), "GroupName And Key Is Empty", Toast.LENGTH_SHORT).show();
                 } else if (name.isEmpty()) {
                     Toast.makeText(Enter_Group.this.getApplicationContext(), "GroupName is Empty", Toast.LENGTH_SHORT).show();
                 } else if (id.isEmpty()) {
                     Toast.makeText(Enter_Group.this.getApplicationContext(), "Group is Empty", Toast.LENGTH_SHORT).show();
                 } else {
                     Enter_Group.this.showProgressDialog();
                     Enter_Group.this.chk = Enter_Group.this.name + Enter_Group.this.id;
                     FirebaseDatabase.getInstance().getReference().child("Groups").orderByChild("qery").equalTo(Enter_Group.this.chk).addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(DataSnapshot dataSnapshot) {
                             if (dataSnapshot.getChildrenCount() > 0) {
                                 String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                 user us = new user();
                                 us.setId(Enter_Group.this.id);
                                 us.setName(Enter_Group.this.name);
                                 us.setQuery(Enter_Group.this.chk);
                                 Maa maa = new Maa();
                                 maa.setId(Enter_Group.this.id);
                                 maa.setNnmm(Enter_Group.this.name);
                                 String que = Enter_Group.this.name + Enter_Group.this.id;
                                 maa.setQery(que);
                                 Enter_Group.this.databaseReference.child("Groups").push().setValue(maa);
                                 Enter_Group.this.databaseReference.child("user").child(userId).push().setValue(us);
                                 Toast.makeText(Enter_Group.this.getApplicationContext(), "Successfully Enterd in Group", 0).show();
                                 String a = Enter_Group.this.name + "mice";
                                 Group_user_Id uid = new Group_user_Id();
                                 uid.setUser_id(userId);
                                 uid.setAdmin("Not");
                                 Enter_Group.this.databaseReference.child("UserId").child(que + "mice").push().setValue(uid);
                                 people p = new people();
                                 p.setPop("Annoni");
                                 HashMap<String, String> token = new HashMap();
                                 token.put("token", FirebaseInstanceId.getInstance().getToken());
                                 Enter_Group.this.databaseReference.child("Device_Token").child(Enter_Group.this.chk).push().setValue(token);
                                 Enter_Group.this.databaseReference.child("grouppeople").child(a).push().setValue(p);
                                 Intent i = new Intent(Enter_Group.this, Main3Activity.class);
                                 // i.putExtra(ShareConstants.WEB_DIALOG_PARAM_MESSAGE, que);
                                 // i.putExtra("name", Enter_Group.this.name);
                                 //Enter_Group.this.startActivity(i);
                             } else {
                                 Toast.makeText(Enter_Group.this.getApplicationContext(), "Groupname Doesn't Exist", 0).show();
                             }
                             Enter_Group.this.hideProgressDialog();
                         }

                         public void onCancelled(DatabaseError databaseError) {
                         }


                     });
                 }
             }
         });
    }



    private void showProgressDialog() {
        if (this.mProgressDialog == null) {
            this.mProgressDialog = new ProgressDialog(this);
            this.mProgressDialog.setMessage("Loading...");
            this.mProgressDialog.setIndeterminate(true);
        }
        this.mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
        }
    }

}