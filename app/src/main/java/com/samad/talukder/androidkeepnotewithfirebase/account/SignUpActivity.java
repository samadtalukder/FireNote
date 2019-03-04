package com.samad.talukder.androidkeepnotewithfirebase.account;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.samad.talukder.androidkeepnotewithfirebase.MainActivity;
import com.samad.talukder.androidkeepnotewithfirebase.R;

public class SignUpActivity extends AppCompatActivity {
    private EditText signUpEmail, signUpPassword;
    private Button signUp;
    private TextView logIn;
    private ImageView fbSignUp, twitterSignUp;
    private FirebaseAuth firebaseAuth;
    // private ProgressDialog progressDialog;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Get Firebase auth instance
        firebaseAuth = FirebaseAuth.getInstance();
        initViews();
        onClickActions();
    }

    private void onClickActions() {
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = signUpEmail.getText().toString().trim();
                String password = signUpPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    signUpEmail.setError("Please Enter Valid Email Address");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    signUpPassword.setError("Please Enter Password");
                    return;
                }
                if (password.length() < 6) {
                    signUpPassword.setError("Password too short, enter minimum 6 characters!");
                    //Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                /*
                progressDialog = new ProgressDialog(SignUpActivity.this);
                progressDialog.setMessage("Processing...");
                progressDialog.show();
                */
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finish();
                            progressBar.setVisibility(View.GONE);
                            // progressDialog.dismiss();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Sign Up UnSuccessful"+task.getException(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            //progressDialog.dismiss();
                        }
                    }
                });
            }
        });
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
                //finish();
            }
        });
    }

    private void initViews() {
        signUpEmail = findViewById(R.id.signUpEmailET);
        signUpPassword = findViewById(R.id.signUpPasswordET);
        signUp = findViewById(R.id.signUpBtn);
        progressBar = findViewById(R.id.progressBar);
        logIn = findViewById(R.id.logInTV);
        fbSignUp = findViewById(R.id.signUpFB);
        twitterSignUp = findViewById(R.id.signUpTwitter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
