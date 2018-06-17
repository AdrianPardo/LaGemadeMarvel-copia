package com.example.adrian.lagemademarvel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPanel extends AppCompatActivity {
    FirebaseAuth mAuth;
    private String email, password;
    private EditText usernameET, apodoET, emailET, passwordET, checkPasswordET;
    boolean inserted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        setContentView(R.layout.activity_registro);
        emailET = (EditText) findViewById(R.id.user_email);
        apodoET = (EditText) findViewById(R.id.user_nick_name);
        passwordET = (EditText) findViewById(R.id.user_password);
        usernameET = (EditText) findViewById(R.id.user_username);
        checkPasswordET = (EditText) findViewById(R.id.user_password_comp);

    }

    private void Register(){
        ShowLoadingAnimation();
        email = emailET.getText().toString().toLowerCase();
        password = passwordET.getText().toString();
        if(!email.equals("") && !password.equals("")) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        private static final String TAG = "";

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");

                                Login(email, password);

                                SetInfo();
                                addToDatabase();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                HideLoadingAnimation();
                                Toast.makeText(RegisterPanel.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }else{
            Toast.makeText(RegisterPanel.this, R.string.empty_login_field, Toast.LENGTH_SHORT).show();
        }
    }

    private void Login(String mail, String pass){
        mAuth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    private static final String TAG = "";
                    FirebaseUser user = mAuth.getCurrentUser();
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            user.sendEmailVerification();
                            Toast.makeText(RegisterPanel.this, "Email de verificacion enviado", Toast.LENGTH_SHORT).show();
                            HideLoadingAnimation();
                            Intent intent = new Intent(RegisterPanel.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            HideLoadingAnimation();
                            Toast.makeText(RegisterPanel.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void SetInfo(){
        String username = usernameET.getText().toString();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("profile: ", "User profile updated.");
                            }
                        }
                    });

        } else {
            // No user is signed in
            Toast.makeText(RegisterPanel.this, "No user Logged in.", Toast.LENGTH_LONG).show();
        }
    }

    public void Register_User(View view) {
        if(!passwordET.getText().toString().equals("")) {
            if (passwordET.getText().toString().equals(checkPasswordET.getText().toString())) {
                Register();
            } else {
                Toast.makeText(RegisterPanel.this, R.string.password_inc, Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(RegisterPanel.this, R.string.empty_login_field, Toast.LENGTH_LONG).show();
        }
    }

    public void Login_User(View view) {
        /*email = emailET.getText().toString();
        password = passwordET.getText().toString();
        Login(email, password);*/
        Intent intent = new Intent(this, LoginPanel.class);
        startActivity(intent);
    }

    //show
    private void ShowLoadingAnimation()
    {
        RelativeLayout pageLoading = (RelativeLayout) findViewById(R.id.main_layoutPageLoading);
        pageLoading.setVisibility(View.VISIBLE);
    }


    //hide
    private void HideLoadingAnimation()
    {
        RelativeLayout pageLoading = (RelativeLayout) findViewById(R.id.main_layoutPageLoading);
        pageLoading.setVisibility(View.GONE);
    }

    private void addToDatabase(){
        String mail = emailET.getText().toString().replace("."," ");
        String nickname = apodoET.getText().toString();
        String username = usernameET.getText().toString();
        //Toast.makeText(RegisterScreen.this, mail, Toast.LENGTH_LONG).show();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("usuarios");

        //Toast.makeText(RegisterScreen.this, "in ondatachange", Toast.LENGTH_LONG).show();
        Log.e("database: ", "start database fill");

        myRef.child(mail).child("Datos").child("Name").setValue(username);
        myRef.child(mail).child("Datos").child("NickName").setValue(nickname);
        Log.e("database: ", "finish database fill");

    }
}
