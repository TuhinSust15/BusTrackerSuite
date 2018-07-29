package com.example.iftekharalamtuhin.bustrackersuite;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordActivity extends AppCompatActivity {

    private EditText etEmailReset;
    private Button btResetPass;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        etEmailReset=findViewById(R.id.etEmailReset);
        btResetPass=findViewById(R.id.btResetPass);
        mAuth=FirebaseAuth.getInstance();

        btResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail=etEmailReset.getText().toString().trim();

                if (userEmail.equals("")){
                    Toast.makeText(PasswordActivity.this,"Enter Your Email",Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(PasswordActivity.this, "Password reset sent to Email", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(PasswordActivity.this, LogIn.class));
                            }
                            else {
                                Toast.makeText(PasswordActivity.this, "Password reset Email Sending Failed", Toast.LENGTH_LONG).show();
                            }
                        }

                    });
                }

            }
        });
    }
}
