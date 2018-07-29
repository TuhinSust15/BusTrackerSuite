package com.example.iftekharalamtuhin.bustrackersuite;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btRegister;
    private TextView tvAlreadyHaveAnAcc;
    private EditText etEmail,etPassword,etBusRoute,etBusType;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        btRegister=findViewById(R.id.btRegister);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        etBusRoute=findViewById(R.id.etBusRoute);
        etBusType=findViewById(R.id.etBusType);
        tvAlreadyHaveAnAcc=findViewById(R.id.tvAlreadyHaveAnAcc);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btRegister).setOnClickListener(this);

        findViewById(R.id.tvAlreadyHaveAnAcc).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btRegister:
                registerUser();
                break;

            case R.id.tvAlreadyHaveAnAcc:
                startActivity(new Intent(this,LogIn.class));
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser()!=null){
            //handle the already registered user
        }

    }

    private void registerUser() {

        final String email=etEmail.getText().toString().trim();
        String password=etPassword.getText().toString().trim();
        final String busRoute=etBusRoute.getText().toString();
        final String busType=etBusType.getText().toString();



        if (email.isEmpty()){
            etEmail.setError("Email is Required");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please Enter a Valid Email");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            etPassword.setError("Password is Required");
            etPassword.requestFocus();
            return;
        }

        if (password.length()<6){
            etPassword.setError("Minimum length of Password is 6");
            etPassword.requestFocus();
            return;
        }

        if (busRoute.isEmpty()){
            etBusRoute.setError("Bus Route is Required");
            etBusRoute.requestFocus();
            return;
        }

        if (busType.isEmpty()){
            etBusType.setError("Bus Type is Required");
            etBusType.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                      if (task.isSuccessful()){
                          //We will add additional Data to database...

//                          Intent intent=new Intent(RegisterActivity.this,LogIn.class);
//                          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                          startActivity(intent);

                          sendEmailVerification();

                          User user=new User(
                                  email,
                                  busRoute,
                                  busType
                          );

                          FirebaseDatabase.getInstance().getReference("User")
                          .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                          .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                  if (task.isSuccessful()){
                                     Toast.makeText(RegisterActivity.this,
                                            "Registration Successful", Toast.LENGTH_LONG).show();
//                                      Intent intent=new Intent(RegisterActivity.this,GpsServiceMain.class);
//                                      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                      startActivity(intent);
                                  }
                                  else {
//                                      Toast.makeText(RegisterActivity.this,
//                                              "Failed to Register", Toast.LENGTH_LONG).show();
                                      if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                          Toast.makeText(getApplicationContext(),"You are already Registered",Toast.LENGTH_LONG).show();
                                      }
                                      else {
                                          Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                      }
                                  }
                              }
                          });



                      }
                      else {
                          Toast.makeText(RegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                      }
                    }
                });
    }
    private void sendEmailVerification(){
        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        if (firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        //sendUserData();
                        Toast.makeText(RegisterActivity.this, "Successfully Registered, Verification mail sent!", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(RegisterActivity.this, LogIn.class));
                    }else{
                        Toast.makeText(RegisterActivity.this, "Verification mail has'nt been sent!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
