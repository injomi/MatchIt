package com.example.matchit;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by josephtyx on 4/6/17.
 */

public class Statistics extends Fragment {
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.statistics, container, false);

        //return super.onCreateView(inflater, container, savedInstanceState);
        return myView;
    }
}
