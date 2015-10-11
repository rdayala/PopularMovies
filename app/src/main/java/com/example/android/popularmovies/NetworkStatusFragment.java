package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by rdayala on 10/11/2015.
 */
public class NetworkStatusFragment extends Fragment {

    public NetworkStatusFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.network_status_layout, container, false);

        rootView.findViewById(R.id.network_connection_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).checkNetworkAction();
            }
        });

        return rootView;
    }

}
