package com.example.matchit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Joel on 17-Jun-17.
 */

public class OrganizationInfo extends Fragment {
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.organizationinfo, container, false);

        //return super.onCreateView(inflater, container, savedInstanceState);
        return myView;
    }
}
