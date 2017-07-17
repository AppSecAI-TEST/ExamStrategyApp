package com.talentsprint.android.esa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.checkmobi.sdk.AsyncResponse;
import com.checkmobi.sdk.CheckMobiService;
import com.checkmobi.sdk.ValidationType;
import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.interfaces.LoginInterface;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.AppUtils;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInMobileFragment extends Fragment {

    private View singUp;
    private EditText mobileEdtTxt;
    private LoginInterface loginInterface;

    public SignInMobileFragment() {
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
        View fragmentView = inflater.inflate(R.layout.fragment_sign_in_mobile, container, false);
        singUp = fragmentView.findViewById(R.id.singUp);
        mobileEdtTxt = fragmentView.findViewById(R.id.mobileEdtTxt);
        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobileEdtTxt.getText().toString().trim().length() == 10) {
                    AppUtils.closeKeyboard(singUp, getActivity());
                    sendCheckMobiOtp();
                } else {
                    Toast.makeText(getActivity(), "Enter a valid number", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return fragmentView;
    }

    private void sendCheckMobiOtp() {
        loginInterface.showProgress(true);
        CheckMobiService.getInstance().SetSecretKey(getActivity().getResources().getString(R.string.check_mobi_api_key));
        CheckMobiService.getInstance().RequestValidation(ValidationType.SMS, "+91" + mobileEdtTxt.getText().toString().trim(), new
                AsyncResponse() {
                    @Override
                    public void OnRequestCompleted(int httpStatus, Map<String, Object> result, String error) {
                        loginInterface.showProgress(false);
                        if (httpStatus == CheckMobiService.STATUS_SUCCESS && result != null) {
                            String key = String.valueOf(result.get("id"));
                            String type = String.valueOf(result.get("type"));
                            OtpVerifyFragment otpVerifyFragment = new OtpVerifyFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString(AppConstants.OTP, key);
                            bundle.putString(AppConstants.MOBILE_NUMBER, mobileEdtTxt.getText().toString().trim());
                            otpVerifyFragment.setArguments(bundle);
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, otpVerifyFragment, AppConstants.DASHBOARD).addToBackStack
                                    (null)
                                    .commit();
                            Toast.makeText(getActivity(), "OTP sent to your mobile number", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Failed to send OTP", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
