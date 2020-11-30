package com.example.triplanproject.firebaseLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triplanproject.menu.MainActivity;
import com.example.triplanproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText mName,mEmail,mPassword;
    Button mRegister;
    TextView mLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName=findViewById(R.id.name);
        mEmail=findViewById(R.id.email);
        mPassword=findViewById(R.id.password);
        mRegister=findViewById(R.id.register);
        mLogin=findViewById(R.id.login);
        progressBar=findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

    }

    public void register(View view) {
        final String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        final String name = mName.getText().toString();

        if(TextUtils.isEmpty(email)){
             Toast.makeText(this,"Email is required",Toast.LENGTH_SHORT).show();
        return;
    }

        if(TextUtils.isEmpty(password)){
        Toast.makeText(this,"Password is required",Toast.LENGTH_SHORT).show();
        return;
    }
        if(password.length() < 6){
            Toast.makeText(this,"Password must be >= 6 characters",Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Register.this," User Created successfully", Toast.LENGTH_SHORT).show();

                    //creation of the user database
                   userID = mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference =fStore.collection("users").document(userID);
                    Map<String, Object> user =new HashMap<>();
                    user.put("Name",name);
                    user.put("Email",email);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("tag","user Profile is created for :"+ userID);
                        }
                    });

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else{
                    Toast.makeText(Register.this," Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });


    }

    public void login(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}