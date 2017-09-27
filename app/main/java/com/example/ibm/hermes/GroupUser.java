package com.example.ibm.hermes;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.ibm.hermes.R.id.collapsingToolbar;

public class GroupUser extends AppCompatActivity {

    private static final int GALARY_PICK = 1;
    private ImageView Proflie;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    private Button change;
    private String data;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private TextView groupnametext;
    private ListView list;
    private StorageReference mImageStorage;
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mref, mimage;
    private DatabaseReference mremove;
    private DatabaseReference muserdata;
    public String nime;
    private String prop;
    private ProgressDialog mProgressdial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_user);
        SwipeBackHelper.onCreate(this);
        String toolbartitle = getIntent().getExtras().getString("toolbar");
        getSupportActionBar().setTitle(toolbartitle);
        prop = getIntent().getExtras().getString("title");
        nime = getIntent().getExtras().getString("name");
        // Toast.makeText(GroupUser.this,nime,Toast.LENGTH_SHORT).show();

        mimage = FirebaseDatabase.getInstance().getReference();

        mImageStorage = FirebaseStorage.getInstance().getReference();

        Proflie=(ImageView)findViewById(R.id.imageView);

        DatabaseReference imageShowref=FirebaseDatabase.getInstance().getReference().child("Group_Profile_Image").child(nime);
        imageShowref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HashMap<String, Object> hashmap = (HashMap) dataSnapshot.getValue();
                    final String vala = (String) hashmap.get("image");
                    Picasso.with(GroupUser.this).load(vala).placeholder(R.drawable.default_avatar).into(Proflie);
                }
                else {

                    Proflie.setImageResource(R.drawable.avatar_group_large);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        list = (ListView) findViewById(R.id.list);
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, arrayList);
        list.setAdapter(adapter);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("UserId").child(prop);
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {

                    data = (childSnapShot.child("user_id").getValue().toString());

                    arrayList.add(data);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                CharSequence[] items = new CharSequence[]{"Remove User", "Report User"};
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupUser.this);
                builder.setTitle((CharSequence) "Select The Action");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            String a = arrayList.get(i);
                            Toast.makeText(GroupUser.this, a, Toast.LENGTH_SHORT).show();
                            cheackadmin(a);


                        }
                        if (item == 1) {
                            Toast.makeText(GroupUser.this, "Report User Clicked", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                builder.show();
                return true;
            }
        });


    }

    private void cheackadmin(final String a) {
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mref = FirebaseDatabase.getInstance().getReference().child("Admin").child(nime);
        mref.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                    if (userId.equals(childSnapShot.child("admin").getValue().toString())) {
                        removeUser(a);
                    } else {
                        Toast.makeText(GroupUser.this.getApplicationContext(), "You are Not admin", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void removeUser(final String a) {
        // Toast.makeText(GroupUser.this,a,Toast.LENGTH_SHORT).show();

        mremove = FirebaseDatabase.getInstance().getReference().child("user").child(a);
        Query q = mremove.orderByChild("query").equalTo(nime);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Toast.makeText(GroupUser.this, "REMOVES", Toast.LENGTH_SHORT).show();
                    data.getRef().setValue(null);
                    arrayList.remove(a);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(GroupUser.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_proflie, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile:

                Intent gallaryIntent = new Intent();
                gallaryIntent.setType("image/*");
                gallaryIntent.setAction("android.intent.action.GET_CONTENT");
                startActivityForResult(Intent.createChooser(gallaryIntent, "SELECT IMAGE"), GALARY_PICK);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALARY_PICK && resultCode == RESULT_OK) {

            Uri imageUri=data.getData();

            CropImage.activity(data.getData()).setAspectRatio(1, 1).start(this);
        }
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if (resultCode==RESULT_OK){

                mProgressdial=new ProgressDialog(GroupUser.this);
                mProgressdial.setTitle("Uploading Image.....");
                mProgressdial.setMessage("Please wait while we upload and the process the image.");
                mProgressdial.setCanceledOnTouchOutside(false);
                mProgressdial.show();

                Uri resulturi=result.getUri();

                StorageReference filepath=mImageStorage.child("Group_Profile").child(nime+".jpg");
                filepath.putFile(resulturi).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){

                            String downloadUrl=task.getResult().getDownloadUrl().toString();
                            HashMap h=new HashMap();
                            h.put("image",downloadUrl);
                            mimage.child("Group_Profile_Image").child(nime).setValue(h).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                        mProgressdial.dismiss();

                                        Toast.makeText(GroupUser.this,"Success Uploading",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        }
                        else {
                            Toast.makeText(GroupUser.this,"NOT Stored Successfully",Toast.LENGTH_SHORT).show();
                            mProgressdial.dismiss();
                        }
                    }
                });


            }
            else if (resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error=result.getError();

            }



        }


    }


    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }
}