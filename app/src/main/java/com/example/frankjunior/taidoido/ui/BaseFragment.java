package com.example.frankjunior.taidoido.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseFragment extends Fragment {

    MainActivity mCallbacks;

    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        try {
            mCallbacks = (MainActivity) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(ctx.toString() + " must be " + MainActivity.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

}
