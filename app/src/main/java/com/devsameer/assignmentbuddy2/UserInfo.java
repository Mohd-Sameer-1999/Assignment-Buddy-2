package com.devsameer.assignmentbuddy2;

import static com.devsameer.assignmentbuddy2.MyName.SHARED_PREFS;
import static com.devsameer.assignmentbuddy2.MyName.TEXT;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;

public class UserInfo extends AppCompatActivity {
   CircularImageView photo;
   TextView username;
   TextView email;
   Button signOut;
   FirebaseAuth mAuth;
   SharedPreferences hasSignedIn = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        hasSignedIn = getSharedPreferences("hasSignedIn", 0);
        boolean hasSigned = hasSignedIn.getBoolean("hasSignedIn", false);

        if (hasSigned) {
            ImageView accphoto = findViewById(R.id.accphoto);
            accphoto.setOnClickListener(v -> {
                vibrateDevice();
//                Intent toGame = new Intent(UserInfo.this, Game.class);
//                startActivity(toGame);
//                Toast.makeText(UserInfo.this, R.string.developer,Toast.LENGTH_LONG).show();
            });

            TextView timesUsed = findViewById(R.id.timesUsed);
            SharedPreferences usedTakeNotes = this.getSharedPreferences("TimeUsedTakeNotes", 0);
            int takeNotesOpening = usedTakeNotes.getInt("TimeUsedTakeNotes", 0);
            if(takeNotesOpening>=1){
                timesUsed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor editor = usedTakeNotes.edit();
                        editor.putInt("TimeUsedTakeNotes",0);
                        editor.apply();
                        Toast.makeText(UserInfo.this,"Counter Reset",Toast.LENGTH_SHORT).show();
                        recreate();
                    }
                });
                if(takeNotesOpening==1) {
                    timesUsed.setVisibility(View.VISIBLE);
                    timesUsed.setText("Used TakeNotes " + takeNotesOpening + " time");
                }
                else {
                    timesUsed.setVisibility(View.VISIBLE);
                    timesUsed.setText("Used TakeNotes " + takeNotesOpening + " times");
                }
            }

            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
            photo = findViewById(R.id.accphoto);
            username = findViewById(R.id.username);
            email = findViewById(R.id.email);
            signOut = findViewById(R.id.sign_out);
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser mUser = mAuth.getCurrentUser();

            if (account !=null){
                //Google
                String personName = account.getDisplayName();
                username.setText(personName);
                String personEmail = account.getEmail();
                email.setText(personEmail);
                Uri photoUrl = account.getPhotoUrl(); Glide.with(this).load(photoUrl).into(photo);
                signOut.setOnClickListener(v -> {
                    vibrateDevice();
                    Toast.makeText(this, R.string.sign_out_greeting,Toast.LENGTH_SHORT).show();
                    Toast.makeText(this,"Goodbye "+personName,Toast.LENGTH_SHORT).show();
                    int death_text = 2800;
                    new Handler().postDelayed(() -> {
                        ((ActivityManager)this.getSystemService(ACTIVITY_SERVICE))
                                .clearApplicationUserData();
                    }, death_text);
                });
            }
            else {
                //Facebook
                String name = mUser.getDisplayName();
                String fbmail = mUser.getEmail();
                String photoURL = mUser.getPhotoUrl().toString();
                Glide.with(this).load(photoURL).into(photo);
                username.setText(name);
                email.setText(fbmail);
                signOut.setOnClickListener(v -> {
                    vibrateDevice();
                    Toast.makeText(this, R.string.sign_out_greeting,Toast.LENGTH_SHORT).show();
                    Toast.makeText(this,"Goodbye "+name,Toast.LENGTH_SHORT).show();
                    int death_text = 2800;
                    new Handler().postDelayed(() -> {
                        ((ActivityManager)this.getSystemService(ACTIVITY_SERVICE))
                                .clearApplicationUserData();
                    }, death_text);
                });
            }
        }
        else {
            Intent toSignUp = new Intent(UserInfo.this,SignUp.class);
            startActivity(toSignUp);
            vibrateDevice();
            Toast.makeText(this,"Sign In To Fully Experience TakeNotes",Toast.LENGTH_SHORT).show();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#19112E"));

        TextView user_save_name = findViewById(R.id.saving_name);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String saving_as = sharedPreferences.getString(TEXT, "");
        if (!saving_as.isEmpty()) {
            user_save_name.setVisibility(View.VISIBLE);
            user_save_name.setText(saving_as);
        }
    }

    private void vibrateDevice() {
        Vibrator v3 = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v3.vibrate(VibrationEffect.createOneShot(32, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v3.vibrate(30);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
