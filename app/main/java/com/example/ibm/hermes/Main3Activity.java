package com.example.ibm.hermes;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.path;

public class Main3Activity extends AppCompatActivity {


    private EditText Contain_message;
    private String GroupChat;
    public String GroupTitle;
    private String SenderId;
    private MessageAdapter mAdapter;
    private DatabaseReference mChat;
    private LinearLayoutManager mLinearLayoutmanager;
    private RecyclerView mMessageList;
    private final List<Messages> messagesList = new ArrayList();
    public String mm;
    private ImageButton msend;
    public ArrayList aib=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        SenderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        GroupChat = getIntent().getStringExtra("message");
        GroupTitle = getIntent().getStringExtra("name");
        mm = GroupChat;
        msend = (ImageButton) findViewById(R.id.chat_send_btn);
        mChat = FirebaseDatabase.getInstance().getReference();
        getSupportActionBar().setTitle(GroupTitle);
        msend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contain_message = (EditText) Main3Activity.this.findViewById(R.id.chat_message_view);
                String message = Contain_message.getText().toString();
                if (TextUtils.isEmpty(message.trim())) {
                    Toast.makeText(Main3Activity.this, "Not Send", Toast.LENGTH_SHORT).show();
                } else {

                    HashMap ref = new HashMap();
                    ref.put("Main_message", message.trim());
                    Main3Activity.this.Contain_message.setText("");
                    ref.put("Sender", Main3Activity.this.SenderId);
                    ref.put("Time", ServerValue.TIMESTAMP);
                    mChat.child("groupchat").child(GroupChat).push().setValue(ref);
                    LastMessage lm = new LastMessage();
                    lm.setLastm(message.trim());
                    lm.setTimestampCreated(ServerValue.TIMESTAMP);
                    lm.setLastmessageSender(Main3Activity.this.SenderId);
                    FirebaseDatabase.getInstance().getReference().child("LastMessage").child(GroupChat).setValue(lm);
                    Toast.makeText(Main3Activity.this, "Succees", Toast.LENGTH_SHORT).show();
                    UpdateLastMessage(message.trim(), GroupChat + "mice");


                }


            }
        });

        mAdapter = new MessageAdapter(this.messagesList);
        mMessageList = (RecyclerView) findViewById(R.id.messages_list);
        mLinearLayoutmanager = new LinearLayoutManager(this);
        mMessageList.setHasFixedSize(true);
        mMessageList.setLayoutManager(this.mLinearLayoutmanager);
        mLinearLayoutmanager.setStackFromEnd(true);
        mMessageList.setAdapter(mAdapter);
        loadMessage();


    }

    private void loadMessage() {
        mChat.child("groupchat").child(GroupChat).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages message = dataSnapshot.getValue(Messages.class);
                messagesList.add(message);
                mMessageList.scrollToPosition(Main3Activity.this.mMessageList.getAdapter().getItemCount() - 1);
                mAdapter.notifyItemInserted(mMessageList.getAdapter().getItemCount() - 1);
                // mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.infoo:
                String a = mm + "mice";

                Intent ij = new Intent(this, GroupUser.class);
                ij.putExtra("title", a);
                ij.putExtra("toolbar", GroupTitle);
                ij.putExtra("name", mm);
                startActivity(ij);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //-----------------------Update LAstMessage----------------//
    private void UpdateLastMessage(final String trim, String s) {

        DatabaseReference mgroupp = FirebaseDatabase.getInstance().getReference().child("UserId").child(s);;
        mgroupp.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {

                    HashMap<String, Object> hashmap = (HashMap) dataSnapshot.getValue();
                    String a= (String) hashmap.get("user_id");
                    finallyu(a,trim,s);


                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void finallyu(String a, final String trim, String s) {


        final DatabaseReference userref=FirebaseDatabase.getInstance().getReference().child("user").child(a);
        Query q=userref.orderByChild("query").equalTo(mm);


        Map<String,Object> taskMap = new HashMap<String,Object>();
        taskMap.put("lastmessage", trim);



       /* q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {

                    HashMap<String, Object> result = new HashMap<>();
                    result.put("lastmessage", trim);
                   // childSnapShot.getRef().child("lastmessage").updateChildren(result);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/



    }
}
