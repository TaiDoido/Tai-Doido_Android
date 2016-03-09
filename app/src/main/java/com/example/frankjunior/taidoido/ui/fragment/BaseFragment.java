package com.example.frankjunior.taidoido.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.frankjunior.taidoido.ui.MainActivity;
import com.example.frankjunior.taidoido.util.Util;

public abstract class BaseFragment extends Fragment {

    MainActivity mCallbacks;
    private ConnectionListener listener;
    private ConnectionBroadcast connectionBroadcast;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionBroadcast = new ConnectionBroadcast();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(connectionBroadcast, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(connectionBroadcast);
    }

    /***
     * Set the callback of internet changed
     *
     * @param listener
     */
    public void setConnectionListener(ConnectionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /***
     * Callback of connection changed
     */
    public interface ConnectionListener {
        void onConnectionChanged(boolean hasConnection);
    }

    private class ConnectionBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equalsIgnoreCase(ConnectivityManager.CONNECTIVITY_ACTION) && listener != null) {
                listener.onConnectionChanged(Util.isInternetConnected(getActivity()));
            }
        }
    }
}
