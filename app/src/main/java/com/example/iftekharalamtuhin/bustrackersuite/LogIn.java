package com.example.iftekharalamtuhin.bustrackersuite;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Iftekhar Alam Tuhin on 14-Jul-18.
 */

public class LogIn extends Activity implements View.OnClickListener {

    FirebaseAuth mAuth;

    private Button btLogIn;
    private EditText etEmailL,etPasswordL;
    private TextView tvForgotPass;
    private ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        mAuth=FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        findViewById(R.id.tvRegisterHere).setOnClickListener(this);
        findViewById(R.id.btLogIn).setOnClickListener(this);



        tvForgotPass=findViewById(R.id.tvForgotPass);
        btLogIn=findViewById(R.id.btLogIn);
        etEmailL=findViewById(R.id.etEmailL);
        etPasswordL=findViewById(R.id.etPasswordL);

        FirebaseUser user=mAuth.getCurrentUser();
        if (user!=null){
            finish();
            startActivity(new Intent(LogIn.this,GpsServiceMain.class));
        }

//        btLogIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                validate(etEmailL.getText().toString(), etPasswordL.getText().toString());
//            }
//        });


        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogIn.this,PasswordActivity.class));
            }
        });


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.tvRegisterHere:
                startActivity(new Intent(this,RegisterActivity.class));
                break;

            case R.id.btLogIn:
                userLogIn();
                break;
        }
    }

    private void userLogIn() {


        final String email=etEmailL.getText().toString().trim();
        String password=etPasswordL.getText().toString().trim();

        if (email.isEmpty()){
            etEmailL.setError("Email is Required");
            etEmailL.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmailL.setError("Please Enter a Valid Email");
            etEmailL.requestFocus();
            return;
        }

        if (password.isEmpty()){
            etPasswordL.setError("Password is Required");
            etPasswordL.requestFocus();
            return;
        }

        if (password.length()<6){
            etPasswordL.setError("Minimum length of Password is 6");
            etPasswordL.requestFocus();
            return;
        }
        btLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(etEmailL.getText().toString(), etPasswordL.getText().toString());
            }
        });
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent=new Intent(LogIn.this,GpsServiceMain.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }



    private void validate(String email, String password){
        progressDialog.setMessage("Logging in...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {



                if (task.isSuccessful()){
                    progressDialog.dismiss();

                    //Toast.makeText(LogIn.this,"Log In Successful",Toast.LENGTH_SHORT).show();
//                    Intent intent=new Intent(LogIn.this,GpsServiceMain.class);
//                    startActivity(intent);
                    checkEmailVerification();
                }
                else {
                    Toast.makeText(LogIn.this,"Log In Failed",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    ///Email Verification Code

    private void checkEmailVerification(){
        FirebaseUser firebaseUser=mAuth.getInstance().getCurrentUser();
        Boolean emailflag=firebaseUser.isEmailVerified();

        if (emailflag){
            finish();
            startActivity(new Intent(LogIn.this,GpsServiceMain.class));
        }
        else{
            Toast.makeText(this,"Please Verify your Email",Toast.LENGTH_LONG).show();
            mAuth.signOut();
        }
    }
}
