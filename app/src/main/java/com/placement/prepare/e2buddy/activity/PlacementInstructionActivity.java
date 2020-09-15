package com.placement.prepare.e2buddy.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
import com.placement.prepare.e2buddy.R;
import com.placement.prepare.e2buddy.network.API;
import com.placement.prepare.e2buddy.network.ApiClient;
import com.placement.prepare.e2buddy.object.User;
import com.placement.prepare.e2buddy.preference.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacementInstructionActivity extends AppCompatActivity {

    private Button btNext;

    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 234;

    private static final String TAG = PlacementInstructionActivity.class.getSimpleName();

    GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth.AuthStateListener listener;

    FirebaseAuth mAuth;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placement_instruction);

        btNext = findViewById(R.id.btNext);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).
                        requestEmail().build();


        mGoogleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SessionManager.getInstance(PlacementInstructionActivity.this).isLoggedIn()) {
                    startActivity(new Intent(PlacementInstructionActivity.this, QuestionActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
                else {
                    signIn();

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(listener);

    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                fireBaseAuthwithGoogle(account);

                if (!isFinishing()) {
                    progressDialog = new ProgressDialog(PlacementInstructionActivity.this);
                    progressDialog.setCancelable(true);
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setProgress(0);
                    progressDialog.setMax(100);
                    progressDialog.show();
                }
            } catch (ApiException e) {
            }
        }
    }

    private void fireBaseAuthwithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                                addData();

                        } else {

                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(PlacementInstructionActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    private void addData(){

        final String username, useremail,imageUri, date, time;
        final SimpleDateFormat sdfDate,sdfTime;

        final FirebaseUser user = mAuth.getCurrentUser();
        useremail = user.getEmail();
        username = user.getDisplayName();
        imageUri = user.getPhotoUrl().toString();

        sdfDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        sdfTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        date = sdfDate.format(new Date());
        time = sdfTime.format(new Date());

        API api = ApiClient.getClient().create(API.class);
        Call call = api.user_login(username, useremail, imageUri, date, time);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()){

                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                        if (jsonObject.getString("status").equals("true")){

                            JSONObject object = jsonObject.getJSONObject("data");

                                int userId = object.getInt("userId");
                                String userName = object.getString("userName");
                                String emailId = object.getString("emailId");

                                String userImage = object.getString("userImage");

                                User user = new User(
                                        userId, userName, emailId, userImage
                                );

                                SessionManager.getInstance(getApplicationContext()).userLogin(user);
                                finish();
                                startActivity(new Intent(PlacementInstructionActivity.this, QuestionActivity.class));
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }


}
