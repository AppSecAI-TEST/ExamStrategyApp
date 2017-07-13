package com.talentsprint.android.esa.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.activities.SignUpActivity;
import com.talentsprint.android.esa.utils.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginHomeFragment extends Fragment {

    private View signup;
    private View emailLogin;

    public LoginHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_login_home, container, false);
        signup = fragmentView.findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navigate = new Intent(getActivity(), SignUpActivity.class);
                startActivity(navigate);
            }
        });
        emailLogin = fragmentView.findViewById(R.id.emailLogin);
        emailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SignInMobileFragment(), AppConstants.DASHBOARD).addToBackStack
                        (null).commit();
            }
        });
        return fragmentView;
    }

}
