package com.example.newsapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;
import java.util.Map;
public class SignUp extends AppCompatActivity {
    private FirebaseAuth auth=FirebaseAuth.getInstance();
    private EditText signupEmail, signupPassword;
    private Button signupButton;
    private TextView loginRedirectText;
    private EditText Confirm_password;
    private EditText UserName;

    String emailpattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        UserName = findViewById(R.id.userName);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        Confirm_password=findViewById(R.id.Confirm_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = UserName.getText().toString();
                String Emailuser = signupEmail.getText().toString();
                String pass = signupPassword.getText().toString();
                String Confpass = Confirm_password.getText().toString();

                if (!Emailuser.matches(emailpattern))
                {
                    signupEmail.setError("Enter a Valid Email");
                }
                else if (pass.isEmpty() || pass.length() < 6)
                {
                    signupPassword.setError("Enter a Valid Password");
                }
                else if (!pass.equals(Confpass))
                {
                    Confirm_password.setError("Confirm Password Not match Password ");
                }
                else
                {
                    auth.createUserWithEmailAndPassword(Emailuser,pass).addOnCompleteListener(SignUp.this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                                {
                                    // Toast.makeText(SignUp.this,"Successfully Registered !!",Toast.LENGTH_SHORT).show();
                                    FirebaseUser firebaseUser=auth.getCurrentUser();
                                    HashMap<String, Object> user = new HashMap<>();
                                    user.put("Name", Username);
                                    user.put("email", Emailuser);
                                    user.put("password", pass);
                                    user.put("confirm_pass", Confpass);
                                    myRef.child("Users").child(Username).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(SignUp.this, "Successfully Registered !", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignUp.this, Login.class));

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(SignUp.this, "Failed To Register !", Toast.LENGTH_SHORT).show();

                                                }
                                            });



                                }
                        }
                    });
                }
            }
        });
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, Login.class));
                    }
        });

    }

    }

