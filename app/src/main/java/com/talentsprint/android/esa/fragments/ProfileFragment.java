package com.talentsprint.android.esa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.interfaces.DashboardActivityInterface;
import com.talentsprint.android.esa.models.ProfileObject;
import com.talentsprint.android.esa.utils.AppUtils;
import com.talentsprint.android.esa.utils.CircleTransform;
import com.talentsprint.android.esa.utils.PreferenceManager;
import com.talentsprint.android.esa.utils.TalentSprintApi;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private DashboardActivityInterface dashboardInterface;
    private ImageView profilePic;
    private TextView name;
    private TextView customerType;
    private TextView joinDate;
    private TextView logout;
    private ImageView edit;
    private View nameLyt;
    private EditText nameEdtTxt;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dashboardInterface = (DashboardActivityInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dashboardInterface.setCurveVisibility(true);
        dashboardInterface.showProgress(true);
        View fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);
        findViews(fragmentView);
        getProfile();
        return fragmentView;
    }

    private void findViews(View fragmentView) {
        profilePic = fragmentView.findViewById(R.id.profilePic);
        name = fragmentView.findViewById(R.id.name);
        customerType = fragmentView.findViewById(R.id.customerType);
        joinDate = fragmentView.findViewById(R.id.joinDate);
        logout = fragmentView.findViewById(R.id.logout);
        edit = fragmentView.findViewById(R.id.edit);
        nameLyt = fragmentView.findViewById(R.id.nameLyt);
        nameEdtTxt = fragmentView.findViewById(R.id.nameEdtTxt);
        logout.setOnClickListener(this);
        edit.setOnClickListener(this);
        nameLyt.setVisibility(View.GONE);
    }

    private void getProfile() {
        dashboardInterface.showProgress(true);
        TalentSprintApi apiService = dashboardInterface.getApiService();
        Call<ProfileObject> getExams = apiService.getProfile();
        getExams.enqueue(new Callback<ProfileObject>() {
            @Override
            public void onResponse(Call<ProfileObject> call, Response<ProfileObject> response) {
                dashboardInterface.showProgress(false);
                if (response.isSuccessful()) {
                    setValues(response);
                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<ProfileObject> call, Throwable t) {
                if (dashboardInterface != null)
                    dashboardInterface.showProgress(false);
                if (getActivity() != null)
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setValues(Response<ProfileObject> response) {
        ProfileObject profileObject = response.body();
        name.setText(profileObject.getName());
        customerType.setText(profileObject.getPlanName());
        joinDate.setText(profileObject.getActiveSince());
        Picasso.with(getActivity()).load(profileObject.getProfilePic()).transform(new CircleTransform()).into(profilePic);
    }

    @Override
    public void onClick(View view) {
        if (view == logout) {
            if (logout.getText().toString().equalsIgnoreCase("SAVE")) {
                if (nameEdtTxt.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), "Enter a valid name", Toast.LENGTH_SHORT).show();
                } else {
                    saveProfile();
                    nameEdtTxt.clearFocus();
                    AppUtils.closeKeyboard(edit, getActivity());
                    logout.setText("LOGOUT");
                    nameLyt.setVisibility(View.GONE);
                    name.setVisibility(View.VISIBLE);
                    name.setText(nameEdtTxt.getText());
                }
            } else {
                logoutUser();
            }
        } else if (view == edit) {
            nameLyt.setVisibility(View.VISIBLE);
            name.setVisibility(View.GONE);
            nameEdtTxt.setText(name.getText());
            logout.setText("SAVE");
        }
    }

    private void saveProfile() {
        dashboardInterface.showProgress(true);
        TalentSprintApi apiService = dashboardInterface.getApiService();
        Call<JSONObject> getExams = apiService.editProfile(nameEdtTxt.getText().toString().trim());
        getExams.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                dashboardInterface.showProgress(false);
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Profile updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                if (dashboardInterface != null)
                    dashboardInterface.showProgress(false);
                if (getActivity() != null)
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logoutUser() {
        dashboardInterface.showProgress(true);
        TalentSprintApi apiService = dashboardInterface.getApiService();
        Call<JSONObject> getExams = apiService.logoutUser();
        getExams.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                dashboardInterface.showProgress(false);
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                    PreferenceManager.deleteAll(getActivity());
                    dashboardInterface.examAdded();
                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                if (dashboardInterface != null)
                    dashboardInterface.showProgress(false);
                if (getActivity() != null)
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
