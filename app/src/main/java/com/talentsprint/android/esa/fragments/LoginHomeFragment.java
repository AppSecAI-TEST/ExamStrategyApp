package com.talentsprint.android.esa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.interfaces.LoginInterface;
import com.talentsprint.android.esa.utils.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginHomeFragment extends Fragment {

    private View signup;
    private View emailLogin;
    private View fbLogin;
    private View googleLogin;
    private LoginInterface loginInterface;

    public LoginHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loginInterface = (LoginInterface) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_login_home, container, false);
        findViews(fragmentView);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SignInMobileFragment(), AppConstants.DASHBOARD).addToBackStack
                        (null).commit();
            }
        });
        emailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SignInMobileFragment(), AppConstants.DASHBOARD).addToBackStack
                        (null).commit();
            }
        });
        fbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fbLogin.setClickable(false);
                loginInterface.loginFb(fbLogin);
            }
        });
        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleLogin.setClickable(false);
                loginInterface.loginGoogle(googleLogin);
            }
        });
        return fragmentView;
    }

    private void findViews(View fragmentView) {
        signup = fragmentView.findViewById(R.id.signup);
        fbLogin = fragmentView.findViewById(R.id.fbLogin);
        googleLogin = fragmentView.findViewById(R.id.googleLogin);
        emailLogin = fragmentView.findViewById(R.id.emailLogin);
    }

}
