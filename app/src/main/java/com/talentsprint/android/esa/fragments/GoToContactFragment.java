package com.talentsprint.android.esa.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.talentsprint.android.esa.interfaces.DashboardActivityInterface;
import com.talentsprint.android.esa.interfaces.StudyMaterialActivityInterface;
import com.talentsprint.android.esa.models.ArticlesObject;
import com.talentsprint.android.esa.models.GetContact;
import com.talentsprint.android.esa.utils.ApiClient;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.TalentSprintApi;
import com.talentsprint.apps.talentsprint.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoToContactFragment extends Fragment {

    private TextView title;
    private TextView subjectTopicText;
    private TextView subjectSubTopicText;
    private TextView contact;
    private ArticlesObject.Articles article;
    private DashboardActivityInterface dashboardActivityInterface;
    private StudyMaterialActivityInterface studyMaterialActivityInterface;
    private View divider;

    public GoToContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            dashboardActivityInterface = (DashboardActivityInterface) getActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            studyMaterialActivityInterface = (StudyMaterialActivityInterface) getActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_go_to_contact, container, false);
        findViews(fragmentView);
        if (dashboardActivityInterface != null) {
            dashboardActivityInterface.setCurveVisibility(true);
        }
        article = (ArticlesObject.Articles) getArguments().getSerializable(AppConstants.ARTICLE);
        if (article != null) {
            title.setText(article.getTitle());
            subjectTopicText.setText(getArguments().getString(AppConstants.TOPICS, "") + " | ");
            subjectSubTopicText.setText(getArguments().getString(AppConstants.SUB_TOPIC, ""));
        } else {
            divider.setVisibility(View.INVISIBLE);
            subjectTopicText.setVisibility(View.INVISIBLE);
            subjectSubTopicText.setVisibility(View.INVISIBLE);
            title.setText(getArguments().getString(AppConstants.CONTENT, ""));
        }
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContact();
            }
        });
        return fragmentView;
    }

    private void getContact() {
        if (dashboardActivityInterface != null)
            dashboardActivityInterface.showProgress(true);
        else
            studyMaterialActivityInterface.showProgress(true);
        TalentSprintApi apiService =
                ApiClient.getCacheClient().create(TalentSprintApi.class);
        Call<GetContact> getContact = apiService.getContact();
        getContact.enqueue(new Callback<GetContact>() {
            @Override
            public void onResponse(Call<GetContact> call, Response<GetContact> response) {
                if (dashboardActivityInterface != null)
                    dashboardActivityInterface.showProgress(false);
                else
                    studyMaterialActivityInterface.showProgress(false);
                if (response.isSuccessful()) {
                    try {
                        String contactNumber = response.body().getMobileNo();
                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                                "tel", contactNumber, null));
                        startActivity(phoneIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetContact> call, Throwable t) {
                if (dashboardActivityInterface != null)
                    dashboardActivityInterface.showProgress(false);
                else if (studyMaterialActivityInterface != null)
                    studyMaterialActivityInterface.showProgress(false);
                if (getActivity() != null)
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findViews(View fragmentView) {
        title = fragmentView.findViewById(R.id.title);
        subjectTopicText = fragmentView.findViewById(R.id.subjectTopicText);
        subjectSubTopicText = fragmentView.findViewById(R.id.subjectSubTopicText);
        divider = fragmentView.findViewById(R.id.divider);
        contact = fragmentView.findViewById(R.id.contact);
    }
}
