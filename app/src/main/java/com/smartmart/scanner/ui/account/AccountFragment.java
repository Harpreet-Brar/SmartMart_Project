package com.smartmart.scanner.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.annotation.NonNull;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.smartmart.scanner.BaseActivity;
import com.smartmart.scanner.Home;
import com.smartmart.scanner.R;


 public class AccountFragment extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
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
     public View onCreateView(@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.activity_login, container, false);


         navBar = getActivity().findViewById(R.id.nav_view);
         profSection = root.findViewById(R.id.profileSection);
         signOut = root.findViewById(R.id.btn);
         Name = root.findViewById(R.id.tv1);
         Email = root.findViewById(R.id.tv2);
         profilePic = root.findViewById(R.id.imageView);
         signOut.setOnClickListener(this);
         profSection.setVisibility(View.GONE);




         GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                 .requestIdToken(getString(R.string.default_web_client_id))
                 .requestEmail()
                 .build();
         mGoogleSignInClient = GoogleSignIn.getClient(this.getActivity (),signInOptions);
         mAuth = FirebaseAuth.getInstance();


        return root;
    }



     @Override
     public void onStart() {
         super.onStart();
         //hideSystemUI ();
         Log.d ("aa", "onStart: ");
         // Check if user is signed in (non-null) and update UI accordingly.
         //updateUI(currentUser);
         setSignIn ();

     }



     @Override
    public void onClick(View v) {
        switch (v.getId())
        {
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
        mGoogleSignInClient.signOut().addOnCompleteListener(this.getActivity (),
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);

                        Intent intent = new Intent (getContext (), Home.class);
                        startActivity (intent);

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
        if(user!=null){
            profSection.setVisibility(View.VISIBLE);
            navBar.setVisibility(View.VISIBLE);


        }
    }



     private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
         // [START_EXCLUDE silent]
        // showProgressDialog();
         // [END_EXCLUDE]

         AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
         mAuth.signInWithCredential(credential)
                 .addOnCompleteListener(this.getActivity (), new OnCompleteListener<AuthResult>() {
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



}

