package com.talentsprint.android.esa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.utils.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInMobileFragment extends Fragment {

    private View singUp;

    public SignInMobileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_sign_in_mobile, container, false);
        singUp = fragmentView.findViewById(R.id.singUp);
        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new OtpVerifyFragment(), AppConstants.DASHBOARD).addToBackStack(null)
                        .commit();
            }
        });
        return fragmentView;
    }

}
