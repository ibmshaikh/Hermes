package com.example.ibm.hermes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.widget.Toast.LENGTH_SHORT;

public class contact extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView mUserlist;
    private DatabaseReference muserref, mlastmessage, mimage, mref;
    private FirebaseRecyclerAdapter madapter, cadapter;
    private ArrayList<String> arrau;
    private String val, timea, uid;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);


        //-----------------------------Navigation---------------------------//

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Hermes");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //--------------------------------End--------------------------------//


        //-------------------------------Main Content------------------------//


        // long i = 1503326402;
        //String x = "1503326402";
        //long milliSeconds = Long.parseLong(x);
        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        //String dateAsString = sdf.format(milliSeconds);
        //Toast.makeText(this, dateAsString, LENGTH_SHORT).show();
        arrau = new ArrayList<String>();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        muserref = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
        progressDialog = new ProgressDialog(this);
        mUserlist = (RecyclerView) findViewById(R.id.recyler);
        mUserlist.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        mUserlist.setLayoutManager(lm);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        muserref.keepSynced(true);
        madapter = new FirebaseRecyclerAdapter<user, contact.UserViewHolder>(user.class, R.layout.activity_dialogs_list, contact.UserViewHolder.class, muserref) {
            @Override
            protected void populateViewHolder(final UserViewHolder viewHolder, user model, final int position) {
                final String ais = model.getName();
                viewHolder.setName(model.getName());
                final String b = model.getQuery();
                //Toast.makeText(contact.this,viewHolder.getLayoutPosition(),LENGTH_SHORT).show();
                mref = FirebaseDatabase.getInstance().getReference().child("LastMessage").child(b);
                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {

                            HashMap<String, Object> hashmap = (HashMap) dataSnapshot.getValue();
                            val = (String) hashmap.get("lastm");
                            Long role = (Long) hashmap.get("timestampCreated");
                            String lastsender = (String) hashmap.get("LastmessageSender");
                            getTimeago getTimeAgo = new getTimeago();
                            String lastSeenTime = getTimeAgo.getTimeAgo(role);
                            viewHolder.setLastTime(lastSeenTime);
                            //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                            //String dateAsString = sdf.format (role);
                            //change(position);
                            swape(viewHolder.getAdapterPosition());

                            if (val.length() > 40) {
                                String nawa = val.substring(0, 40);
                                viewHolder.setLastmessage((nawa + "..."));
                               // viewHolder.setLastTime(lastSeenTime);
                            } else {

                                viewHolder.setLastmessage(val);
                                //viewHolder.setLastTime(lastSeenTime);
                            }
                            //viewHolder.setLastmessage(val);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mimage = FirebaseDatabase.getInstance().getReference().child("Group_Profile_Image").child(b);
                mimage.keepSynced(true);
                mimage.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {

                            HashMap<String, Object> hashmap = (HashMap) dataSnapshot.getValue();
                            final String vala = (String) hashmap.get("image");

                            viewHolder.setImage(vala, getApplicationContext());
                            /// //viewHolder.setImage(val,getApplicationContext());
                            //viewHolder.setImage(vala);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(contact.this,"Something Went wrong",LENGTH_SHORT).show();
                    }
                });

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(contact.this, Main3Activity.class);
                        i.putExtra("message", b);
                        i.putExtra("name", ais);
                        startActivity(i);
                    }
                });
            }

        };
        mUserlist.setAdapter(madapter);
        madapter.notifyDataSetChanged();
        //------------------------Main End---------------------------//


    }

    private void swape(int adapterPosition) {

        madapter.notifyItemMoved(adapterPosition,0);


    }


    //--------------------------Navigation Drawer-----------------//

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(contact.this,MainActivity.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.craetegroup) {

            Intent i=new Intent(contact.this,Insert.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.entergroup) {

            Intent j=new Intent(contact.this,Enter_Group.class);

            startActivity(j);

        } else if (id == R.id.action_settings) {

           // Intent k= new Intent(contact.this, Setting.class);
           // startActivity(k);

        }  else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //----------------------Navigation End------------------------//




    //----------------------RecyclerView Holder----------------//


    public static class UserViewHolder extends RecyclerView.ViewHolder {

        View mview;
        ImageLoader imageLoader;

        public UserViewHolder(View itemView) {
            super(itemView);
            mview = itemView;

        }

        public void setName(String name) {
            TextView username = (TextView) mview.findViewById(R.id.dialogName);
            username.setText(name);
        }

        public void setLastmessage(String lastmessage) {

            TextView set = (TextView) mview.findViewById(R.id.dialogLastMessage);
            set.setText(lastmessage);


        }

        public void setLastTime(String lastTime) {
            TextView time = (TextView) mview.findViewById(R.id.dialogDate);
            time.setText(lastTime);
        }


        public void setImage(final String vala, final Context applicationContext) {
            final CircleImageView cm = (CircleImageView) mview.findViewById(R.id.dialogAvatar);
            Picasso.with(applicationContext).load(vala).networkPolicy(NetworkPolicy.OFFLINE).into(cm, new Callback() {
                @Override
                public void onSuccess() {
                    Picasso.with(applicationContext).load(vala).into(cm);
                }

                @Override
                public void onError() {
                    Picasso.with(applicationContext).load(vala).into(cm);
                }
            });


        }

        public void setUnread(int unread){

            TextView tx=(TextView)mview.findViewById(R.id.dialogUnreadBubble);



        }

    }


    //----------------------Unread Count----------------------------------//




}
