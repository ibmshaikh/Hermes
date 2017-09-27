package com.example.ibm.hermes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.jude.swipbackhelper.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int RC_SIGN_IN = 2;
    // SignInButton button;
    private Button buttonSignup;
    FirebaseAuth.AuthStateListener mAuthlistner;
    GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private TextView textviewreg;
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
    private SignInButton msignin;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //firebase auth object
    private FirebaseAuth firebaseAuth;

    FirebaseAuth.AuthStateListener mAut;
    //progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAut);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();

        mAut=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()!=null){
                    startActivity(new Intent(MainActivity.this,contact.class));
                }
            }
        };

        //if the objects getcurrentuser method is not null
        //means user is already logged in
        if(firebaseAuth.getCurrentUser() != null){
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), contact.class));
        }

        //-------------------End-------------------//
        msignin=(SignInButton)findViewById(R.id.sign_in_button);
        msignin.setOnClickListener(this);
        msignin.setOnClickListener(this);



        editTextEmail = (EditText) findViewById(R.id.input_email);
        editTextPassword = (EditText) findViewById(R.id.input_password);
        buttonSignup = (Button) findViewById(R.id.btn_login);
        progressDialog = new ProgressDialog(this);
        buttonSignup.setOnClickListener(this);
        textviewreg = (TextView) findViewById(R.id.link_signup);




    }

    @Override
    public void onClick(View view) {
        if (view == this.buttonSignup) {
            userLogin();
        }
        if (view==msignin){


        }
    }




    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });



    }

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            // Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            editTextEmail.setError("Enter Email");
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            editTextPassword.setError("Enter Password");
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //if the task is successfull
                        if(task.isSuccessful()){
                            //start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(),contact.class));
                        }
                        else {

                            editTextEmail.setError("Emai/password not correct");

                        }
                    }
                });

    }

    public void register(View v) {
        startActivity(new Intent(this, Register.class));
    }


    public void phone(View view){
        Intent i=new Intent(MainActivity.this,PhoneAuthActivity.class);
        startActivity(i);
    }












}
