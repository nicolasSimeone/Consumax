package cabureweb.consumax;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginActivity extends AppCompatActivity {
    private Button buttonLogin;
    private FirebaseAuth.AuthStateListener mAuthListerner;
    private FirebaseAuth mAuth;
    LoginButton loginButton;
    CallbackManager mCallbackManager;
    String TAG = "Hey";
    String name;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogin = (Button)findViewById(R.id.login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginUser.class);
                startActivity(intent);
            }
        });

        loginButton = (LoginButton) findViewById(R.id.bLoginWithFacebook);
        mAuth = FirebaseAuth.getInstance();

        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG,"Hello" + loginResult.getAccessToken().getToken());
                //Toast.makeText(loginActivity.this, "Token:" + loginResult.getAccessToken(),Toast.LENGTH_SHORT);

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        mAuthListerner = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user!=null){
                    name = user.getDisplayName();
                    Toast.makeText(loginActivity.this,"" + user.getDisplayName(), Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(loginActivity.this, "Algo fue mal", Toast.LENGTH_LONG).show();
                }
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken (AccessToken token){
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("firstLogin");
                            mDatabase2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String message = dataSnapshot.getValue(String.class);

                                    if(message == null){
                                        RegisterFacebookUserInFireBaseDatabase();

                                    }else{
                                        Intent intent = new Intent(loginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                        else{
                            Toast.makeText(loginActivity.this, "Authentication error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void RegisterFacebookUserInFireBaseDatabase() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        DatabaseReference currentUserDB = mDatabase.child(mAuth.getCurrentUser().getUid());


        currentUserDB.child("name").setValue(mAuth.getCurrentUser().getDisplayName());
        currentUserDB.child("registration_id").setValue("0");
        currentUserDB.child("firstLogin").setValue("0");
        currentUserDB.child("nickname").setValue(mAuth.getCurrentUser().getDisplayName());
        //currentUserDB.child("birthday").setValue("ToBeDefined");
        Intent intent = new Intent(loginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();


    }

}