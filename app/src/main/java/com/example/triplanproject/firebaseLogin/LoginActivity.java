package com.example.triplanproject.firebaseLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.triplanproject.menu.MainActivity;
import com.example.triplanproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.widget.Toast.*;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Main";
    private FirebaseAuth mAuth;
    EditText mEmail, mPassword;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail=findViewById(R.id.email);
        mPassword=findViewById(R.id.password);
        progressBar=findViewById(R.id.progressBar2);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }



    }


    public void createNewAccount(View view) {
        startActivity(new Intent(getApplicationContext(), Register.class));
        finish();
    }

    public void login(View view) {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            mEmail.setError("Email is required");
            return;
        }

            if(TextUtils.isEmpty(password)){
            mPassword.setError("Password is required");
            return;
        }
            progressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Logged in successfully", LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else{
                        Toast.makeText(LoginActivity.this, "Error :"+ task.getException().getMessage(), LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }

            });

    }
}