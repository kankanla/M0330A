package com.kankanla.e560.m0330a;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kankanla.e560.m0330a.Function.PickFolderDialog;
import com.kankanla.e560.m0330a.Function.Speaker;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            addFragment();
            setpermission();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        System.out.println("-----MainActivity-------------onSaveInstanceState------------1---------");
        outState.putBoolean("restart", false);
        outState.putString("miru", "miru");
        super.onSaveInstanceState(outState);
        System.out.println("-----MainActivity-------------onSaveInstanceState------------2---------");
    }

    public void addFragment() {
        BGMFragment2 bgmFragment;
        SpeakerFragment speakerFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Speaker speaker = new Speaker(this);
        speakerFragment = new SpeakerFragment(this, speaker);
        bgmFragment = new BGMFragment2(this);
        fragmentTransaction.add(R.id.main_bgmplay, bgmFragment, "main_bgmplay");
        fragmentTransaction.add(R.id.main_speaker, speakerFragment, "main_speaker");
        fragmentTransaction.commit();
    }


//    public void addFragment() {
//        BGMFragment2 bgmFragment;
//        SpeakerFragment speakerFragment;
//        FragmentManager fragmentManager = getSupportFragmentManager();
//
//        bgmFragment = (BGMFragment2) fragmentManager.findFragmentByTag("main_bgmplay");
//        speakerFragment = (SpeakerFragment) fragmentManager.findFragmentByTag("main_speaker");
//
//        if (bgmFragment == null && speakerFragment == null) {
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            bgmFragment = new BGMFragment2(this);
//            speakerFragment = new SpeakerFragment(this);
//            fragmentTransaction.add(R.id.main_bgmplay, bgmFragment, "main_bgmplay");
//            fragmentTransaction.add(R.id.main_speaker, speakerFragment, "main_speaker");
//            fragmentTransaction.commit();
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void t2(View view) {
        if (Build.VERSION.SDK_INT > 22) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                PickFolderDialog pickFolderDialog = new PickFolderDialog();
                pickFolderDialog.show(getFragmentManager(), "xx");
            } else {
                setpermission();
            }
        } else {
            PickFolderDialog pickFolderDialog = new PickFolderDialog();
            pickFolderDialog.show(getFragmentManager(), "xx");
        }
    }

    private void setpermission() {
        System.out.println("--------------Build.VERSION.SDK_INT---------" + Build.VERSION.SDK_INT + "--------");
        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 13);
                System.out.println("------------------------requestPermissions----------end-------------------");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println("----------1--------------requestPermissions-------------1----------------");
        System.out.println("requestCode--" + requestCode);
        for (String temp : permissions) {
            System.out.println("permissions--" + temp);
        }
        for (int temp : grantResults) {
            System.out.println("grantResults--" + temp);
        }
        System.out.println("----------2--------------requestPermissions---------------2--------------");
    }
}