package com.example.iftekharalamtuhin.bustrackersuite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private ImageView profilePic;
    private TextView profieBusType, profileBusRoute, profileBusEmail;

    private Button btLogout,btEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        profilePic=(ImageView)findViewById(R.id.ivProPic);
        profieBusType=(TextView)findViewById(R.id.tvProBusType);
        profileBusRoute=(TextView) findViewById(R.id.tvProBusRoute);
        profileBusEmail=(TextView)findViewById(R.id.tvProEmail);
        btLogout=(Button) findViewById(R.id.btLogout);
        btEditProfile=(Button)findViewById(R.id.btEditProfile);
        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();


        //Confusing code::: @getCurrentUser().getUid() method...

        DatabaseReference databaseReference;
        databaseReference = firebaseDatabase.getReference().child("User").child(mAuth.getCurrentUser().getUid());
        

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user=dataSnapshot.getValue(User.class);
                profieBusType.setText("Bus Type: "+user.getBusType());
                profileBusRoute.setText("Bus Route: "+user.getBusRoute());
                profileBusEmail.setText("Email: "+user.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, databaseError.getCode(),Toast.LENGTH_LONG).show();

            }
        });
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(ProfileActivity.this,LogIn.class));
            }
        });

    }
}
