package com.devsameer.assignmentbuddy2;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import org.jetbrains.annotations.NotNull;
import nl.joery.animatedbottombar.AnimatedBottomBar;

public class BottomHandler extends AppCompatActivity {
    AnimatedBottomBar animatedBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_handler);

        animatedBottomBar = findViewById(R.id.bottomNavigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new TakeNotes()).commit();
        animatedBottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int lastIndex, @Nullable AnimatedBottomBar.Tab lastTab, int newIndex, @NotNull AnimatedBottomBar.Tab newTab) {
                Fragment fragment = null;
                switch (newTab.getId()) {
                    case R.id.takenotes:
                        vibrateDevice();
                        fragment = new TakeNotes();
                        break;
                    case R.id.ocr:
                        vibrateDevice();
                        fragment = new OCR_Bottom();
                        break;
                    case R.id.history:
                        vibrateDevice();
                        fragment = new History();
                        break;
                    case R.id.more:
                        vibrateDevice();
                        fragment = new More();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
            }

            @Override
            public void onTabReselected(int i, @NotNull AnimatedBottomBar.Tab tab) {
               
            }
        });
    }
    private void vibrateDevice() {
        Vibrator v3 = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v3.vibrate(VibrationEffect.createOneShot(28, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v3.vibrate(20);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
