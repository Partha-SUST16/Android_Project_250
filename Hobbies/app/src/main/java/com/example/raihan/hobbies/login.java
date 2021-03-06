package com.example.raihan.hobbies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity implements View.OnClickListener {

    private EditText inputEmail, inputPassword;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    private FirebaseAuth mAuth;
    int total;
    //private DatabaseReference databaseReference,fine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_login){
            final String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            final ProgressDialog Dialog = new ProgressDialog(login.this);
            Dialog.setMessage("Processing...");
            Dialog.show();

            mAuth.signInWithEmailAndPassword(email+"@gmail.com", password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(login.this, MainActivity.class);  ////////////////// Set it //////////////
                                //i.putExtra("Type", "Driver");
                                i.putExtra("user", email);
                                Dialog.dismiss();
                                Toast.makeText(login.this, "Logged in!",

                                        Toast.LENGTH_SHORT).show();
                                startActivity(i);
                            }
                            else
                            {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(login.this, "Authentication failed.",

                                        Toast.LENGTH_SHORT).show();
                                Dialog.dismiss();

                            }


                        }
                    });
        }
        if(view.getId() == R.id.btn_reset_password){
            Toast.makeText(login.this,"Feature Not Added",Toast.LENGTH_LONG).show();
        }
        if(view.getId() == R.id.btn_signup){
            Intent i = new Intent(login.this,register.class);
            startActivity(i);
        }
    }
}
