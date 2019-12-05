package com.smartmart.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class Home extends Basehome implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private ConstraintLayout profSection;
    private ConstraintLayout signinsection;
    private Button signOut;
    private Button signIn;
    private TextView Name,Email;
    private ImageView profilePic;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private BottomNavigationView navBar;
    private static final int REQ_CODE = 9005;
    public static Boolean user = false;
    public static GoogleSignInResult result =null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_home);
        profSection = findViewById(R.id.profileSection);
        signinsection = findViewById (R.id.signInSection);
        signOut = findViewById(R.id.btn);
        signIn = findViewById(R.id.signIn);
        Name = findViewById(R.id.tv1);
        Email = findViewById(R.id.tv2);
        profilePic = findViewById(R.id.imageView);
        signIn.setOnClickListener(this);
        signOut.setOnClickListener(this);
        profSection.setVisibility(View.GONE);




        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,signInOptions);
        mAuth = FirebaseAuth.getInstance();


        hideSystemUI ();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.signIn:
                setSignIn();
                break;
            case R.id.btn: {
                setSignOut();
                break;
            }
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    private void setSignIn(){
        Intent intent = mGoogleSignInClient.getSignInIntent();

        startActivityForResult(intent, REQ_CODE);

    }
    private void setSignOut(){
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }
    private void handleResult(GoogleSignInResult result){
        if (result.isSuccess()){
            Log.d ("aa", "handleResult: "+result.getSignInAccount());
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            Glide.with(this).load(account.getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform()).into(profilePic);
            Name.setText(name);
            Email.setText(email);
        }
        else {
            updateUI(null);
        }
    }
    private void updateUI (FirebaseUser user){

        hideSystemUI ();
        if(user!=null){
            Intent intent = new Intent (getApplicationContext (),StoreSearchActivity.class);
            startActivity (intent);
        }
        else {
            profSection.setVisibility(View.GONE);
            signinsection.setVisibility(View.VISIBLE);
        }
    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        // [START_EXCLUDE silent]
         showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            updateUI(null);
                        }
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQ_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                firebaseAuthWithGoogle(account);
                handleResult (result);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("aa", "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }


    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = this.getWindow ().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = this.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}


