package com.example.matchit;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.matchit.helper.SQLiteHandler;
import com.example.matchit.helper.SessionManager;

import java.util.HashMap;

/**
 * Created by josephtyx on 4/6/17.
 */

public class OrganizationInfo extends Fragment {
    private static final String TAG = OrganizationInfo.class.getSimpleName();

    private TextView orgName;
    private TextView orgAddress;
    private TextView orgContact;
    private TextView orgWho;
    private TextView orgHow;
    private TextView orgYou;

    private ProgressDialog pDialog;

    private SQLiteHandler db;
    public HashMap<String,String> user;
    private SessionManager session;
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.organizationinfo, container, false);

        orgName = (TextView) myView.findViewById(R.id.textOrgName);
        orgAddress = (TextView) myView.findViewById(R.id.textAddress);
        orgContact = (TextView) myView.findViewById(R.id.textContactNumber);
        orgWho = (TextView) myView.findViewById(R.id.textWhoTheyHelp);
        orgHow = (TextView) myView.findViewById(R.id.textHowTheyHelp);
        orgYou = (TextView) myView.findViewById(R.id.textHowYouCanBeInvolved);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getActivity().getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());

        user = db.getOrgDetails();

        String name = user.get("org_name");
        String address = user.get("org_address");
        String contact = user.get("org_contact");
        String who = user.get("org_who");
        String how = user.get("org_how_they");
        String howyou = user.get("org_how_you");




        orgName.setText(name);
        orgAddress.setText(address);
        orgContact.setText(contact);
        orgWho.setText(who);
        orgHow.setText(how);
        orgYou.setText(howyou);

        return myView;
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
