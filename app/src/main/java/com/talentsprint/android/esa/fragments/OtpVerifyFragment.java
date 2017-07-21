package com.talentsprint.android.esa.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.google.gson.JsonObject;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.activities.SignUpActivity;
import com.talentsprint.android.esa.interfaces.LoginInterface;
import com.talentsprint.android.esa.utils.ApiClient;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.PreferenceManager;
import com.talentsprint.android.esa.utils.TalentSprintApi;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.talentsprint.android.esa.utils.AppConstants.LOGIN_RESULT;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtpVerifyFragment extends Fragment {

    private View verifyOtp;
    private EditText mobileEdtTxt;
    private LoginInterface loginInterface;

    public OtpVerifyFragment() {
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
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_otp_verify, container, false);
        verifyOtp = fragmentView.findViewById(R.id.verifyOtp);
        mobileEdtTxt = fragmentView.findViewById(R.id.mobileEdtTxt);
        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobileEdtTxt.getText().toString().trim().length() == 4) {
                    validateOTP();
                } else {
                    Toast.makeText(getActivity(), "Enter a valid OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return fragmentView;
    }

    private void validateOTP() {
        loginInterface.showProgress(true);
        CheckMobiService.getInstance().VerifyPin(ValidationType.SMS, getArguments().getString(AppConstants.OTP), mobileEdtTxt
                .getText().toString(), new AsyncResponse() {
            @Override
            public void OnRequestCompleted(int httpStatus, Map<String, Object> result, String error) {
                loginInterface.showProgress(false);
                if (httpStatus == CheckMobiService.STATUS_SUCCESS && result != null) {
                    Boolean validated = (Boolean) result.get("validated");
                    if (!validated) {
                        Toast.makeText(getActivity(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    PreferenceManager.saveString(getActivity(), AppConstants.MOBILE_NUMBER, getArguments().getString
                            (AppConstants.MOBILE_NUMBER));
                    loginUser();
                } else {
                    Toast.makeText(getActivity(), "Error validationg OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser() {
        loginInterface.showProgress(true);
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        String oneSignalId = status.getSubscriptionStatus().getUserId();
        TalentSprintApi apiService =
                ApiClient.getCacheClient(false).create(TalentSprintApi.class);
        Call<JsonObject> getHomeDetails;
        RequestBody mobileBody = null;
        RequestBody oneSignalIdBody = null;
        mobileBody = RequestBody.create(MediaType.parse("text/plain"), getArguments().getString(AppConstants.MOBILE_NUMBER));
        if (oneSignalId != null)
            oneSignalIdBody = RequestBody.create(MediaType.parse("text/plain"), oneSignalId);
        getHomeDetails = apiService.loginMobile(mobileBody, oneSignalIdBody);
        getHomeDetails.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loginInterface.showProgress(false);
                if (response.isSuccessful()) {
                    try {
                        JsonObject responseBody = response.body();
                        if (responseBody.get("status").getAsString().equalsIgnoreCase("Ok")) {
                            PreferenceManager.saveString(getActivity(), AppConstants.USER_TYPE, responseBody.get
                                    ("accessType").getAsString());
                            PreferenceManager.saveBoolean(getActivity(), AppConstants.MOBILE_USER, true);
                            PreferenceManager.saveBoolean(getActivity(), AppConstants.IS_LOGGEDIN, true);
                            Intent returnIntent = new Intent();
                            getActivity().setResult(Activity.RESULT_OK, returnIntent);
                            getActivity().finish();
                        } else if (responseBody.get("status").getAsString().equalsIgnoreCase(AppConstants.INVALID)) {
                            Intent navigate = new Intent(getActivity(), SignUpActivity.class);
                            startActivityForResult(navigate, LOGIN_RESULT);
                        } else {
                            Toast.makeText(getActivity(), responseBody.get("status").getAsString(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (loginInterface != null)
                    loginInterface.showProgress(false);
                if (getActivity() != null)
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                Intent returnIntent = new Intent();
                getActivity().setResult(Activity.RESULT_OK, returnIntent);
                getActivity().finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                getActivity().onBackPressed();
            }
        }
    }
}
