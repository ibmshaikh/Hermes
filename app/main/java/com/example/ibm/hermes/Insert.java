package com.example.ibm.hermes;

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

import java.util.HashMap;

public class Insert extends AppCompatActivity {

    private EditText minsert,mid;
    private Button msave;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String name,id,chk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        msave=(Button)findViewById(R.id.button2);
        minsert=(EditText)findViewById(R.id.editText) ;
        mid=(EditText)findViewById(R.id.editText3);
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        msave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name =minsert.getText().toString().trim();
                id = mid.getText().toString().trim();
                if (name.isEmpty() && id.isEmpty()) {
                    Toast.makeText(Insert.this.getApplicationContext(), "GroupName And Group Key Should not be Empty",Toast.LENGTH_SHORT).show();
                } else if (name.isEmpty()) {
                    Toast.makeText(Insert.this.getApplicationContext(), "GroupName Should not be Empty", Toast.LENGTH_SHORT).show();
                } else if (id.isEmpty()) {
                    Toast.makeText(Insert.this.getApplicationContext(), "Group Key Should not be Empty",Toast.LENGTH_SHORT).show();
                } else {
                    chk = name+id;
                    FirebaseDatabase.getInstance().getReference().child("Groups").orderByChild("qery").equalTo(Insert.this.chk).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() > 0) {
                                Toast.makeText(Insert.this.getApplicationContext(), "Groupname Already Exist", Toast.LENGTH_SHORT).show();
                            }
                            else {

                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                user us = new user();
                                us.setId(Insert.this.id);
                                us.setName(Insert.this.name);
                                us.setQuery(Insert.this.chk);
                                Maa maa = new Maa();
                                maa.setId(Insert.this.id);
                                maa.setNnmm(Insert.this.name);
                                String que = Insert.this.name + Insert.this.id;
                                maa.setQery(que);
                                databaseReference.child("Groups").push().setValue(maa);
                                databaseReference.child("user").child(userId).push().setValue(us);
                                Toast.makeText(Insert.this.getApplicationContext(), "Group Created", 0).show();
                                String a = Insert.this.name + "mice";
                                Admin admin = new Admin();
                                admin.setAdmin(userId);
                                Insert.this.databaseReference.child("Admin").child(Insert.this.chk).push().setValue(admin);
                                Group_user_Id uid = new Group_user_Id();
                                uid.setUser_id(userId);
                                uid.setAdmin("Admin");
                                databaseReference.child("UserId").child(que + "mice").push().setValue(uid);
                                people p = new people();
                                p.setPop("Annoni");
                                Insert.this.databaseReference.child("grouppeople").child(a).push().setValue(p);
                                HashMap<String, String> token = new HashMap();
                                token.put("token", FirebaseInstanceId.getInstance().getToken());
                                databaseReference.child("Device_Token").child(Insert.this.chk).push().setValue(token);
                                Insert.this.startActivity(new Intent(Insert.this, contact.class));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }



            }
        });



    }
}
