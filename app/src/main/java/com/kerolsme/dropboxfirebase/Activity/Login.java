package com.kerolsme.dropboxfirebase.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kerolsme.dropboxfirebase.R;
import com.kerolsme.dropboxfirebase.Utils.CloudDatabase;

import java.util.Objects;

public class Login extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;
    private View view_login;
    private GoogleSignInClient mGoogleSignInClient;
    private String TAG = Login.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        MaterialButton GoogleSign = findViewById(R.id.GoogleSign);
        view_login = findViewById(R.id.view_login_loading);
        view_login.setVisibility(View.GONE);
        CreateAuth();
        GoogleSign.setOnClickListener(this);

    }

    public void CreateAuth () {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        view_login.setVisibility(View.VISIBLE);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            CloudDatabase cloudDatabase  = new CloudDatabase(getApplicationContext());
                            Log.d(TAG, "signInWithCredential:success");
                            boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                            cloudDatabase.CheckAndRegister(isNewUser,new CloudDatabase.CloudResult() {
                                @Override
                                public void onError(Throwable throwable) {
                                    Log.e(TAG, "onError: ",throwable );
                                    Toast.makeText(Login.this, "Try Again", Toast.LENGTH_SHORT).show();
                                    view_login.setVisibility(View.GONE);

                                }

                                @Override
                                public void onSucceed() {
                                    view_login.setVisibility(View.GONE);
                                    Intent intent = new Intent(Login.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            });

                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Try Again", Toast.LENGTH_SHORT).show();
                            view_login.setVisibility(View.GONE);

                        }
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1);
    }

    @Override
    public void onClick(View view) {
        signIn();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
