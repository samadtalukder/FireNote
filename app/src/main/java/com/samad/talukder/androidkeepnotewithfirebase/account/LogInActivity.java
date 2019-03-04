package com.samad.talukder.androidkeepnotewithfirebase.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.samad.talukder.androidkeepnotewithfirebase.MainActivity;
import com.samad.talukder.androidkeepnotewithfirebase.R;

public class LogInActivity extends AppCompatActivity {
    private EditText logInEmail, logInPassword;
    private Button logIn;
    private TextView signUp;
    private ImageView fbLogIn, twitterLogIn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get Firebase auth instance
        firebaseAuth = FirebaseAuth.getInstance();
        // Checking User Session
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LogInActivity.this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_log_in);
        initViews();
        // Get Firebase auth instance
        firebaseAuth = FirebaseAuth.getInstance();
        onClickActions();
    }

    private void initViews() {
        logInEmail = findViewById(R.id.logInEmailET);
        logInPassword = findViewById(R.id.logInPasswordET);
        logIn = findViewById(R.id.logInBtn);
        signUp = findViewById(R.id.signUpTV);
        fbLogIn = findViewById(R.id.logInFB);
        twitterLogIn = findViewById(R.id.logInTwitter);
    }

    private void onClickActions() {
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = logInEmail.getText().toString().trim();
                final String password = logInPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    logInEmail.setError("Please Enter Valid Email Address");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    logInPassword.setError("Please Enter Password");
                    return;
                }
                /*if (password.length() < 6) {
                    logInPassword.setError("Password too short, enter minimum 6 characters!");
                    //Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                progressDialog = new ProgressDialog(LogInActivity.this);
                progressDialog.setMessage("Processing...");
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (!task.isSuccessful()) {
                            if (password.length() < 6) {
                                logInPassword.setError("Password too short, enter minimum 6 characters!");
                            } else {
                                Toast.makeText(LogInActivity.this, "Log In UnSuccessful", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LogInActivity.this, "Log In Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LogInActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
            }
        });
    }
}
