package com.example.frankjunior.taidoido.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.frankjunior.taidoido.R;

public abstract class BaseActivity extends AppCompatActivity {

    /***
     * Change the given fragment on current Activity
     *
     * @param fragment
     */
    public void changeFragment(@NonNull Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragmentManager.findFragmentByTag(fragment.getClass().toString()) != null) {
            fragmentTransaction.add(R.id.place_holder, fragment, fragment.getClass().toString());
        } else {
            fragmentTransaction.replace(R.id.place_holder, fragment, fragment.getClass().toString());
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

}
