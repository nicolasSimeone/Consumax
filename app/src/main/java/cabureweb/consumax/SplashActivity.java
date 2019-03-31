package cabureweb.consumax;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.R.id.message;

public class SplashActivity extends AppCompatActivity {

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    int User_Id;



    ImageView loading;
    Thread splashTread;

    //NEW LOGIN FIREBASE
    private static final String TAG = "Login_Activity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String first_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);

        facebookSDKInitialize();
        AppEventsLogger.activateApp(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // ...
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };





        StartAnimations();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void StartAnimations() {




        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                        //Go to login
                        Intent intent = new Intent(getBaseContext(), loginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("firstLogin");
                        mDatabase2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String message = dataSnapshot.getValue(String.class);

                                if(message == null){
                                    Intent intent = new Intent(getBaseContext(), loginActivity.class);
                                    startActivity(intent);
                                    finish();

                                }else{
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    // Splash screen pause time
                    while (waited < 5000) {
                        sleep(100);
                        waited += 100;
                    }

                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    //loginMenu.this.finish();
                    //  dialog.dismiss();

                }

            }
        };
        splashTread.start();

    }

    protected void facebookSDKInitialize() {
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
