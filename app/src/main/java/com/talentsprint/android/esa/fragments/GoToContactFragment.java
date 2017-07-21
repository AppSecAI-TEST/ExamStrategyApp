package com.talentsprint.android.esa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.interfaces.DashboardActivityInterface;
import com.talentsprint.android.esa.models.ArticlesObject;
import com.talentsprint.android.esa.utils.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoToContactFragment extends Fragment {

    private TextView title;
    private TextView subjectTopicText;
    private TextView subjectSubTopicText;
    private ArticlesObject.Articles article;
    private DashboardActivityInterface dashboardActivityInterface;
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
        return fragmentView;
    }

    private void findViews(View fragmentView) {
        title = fragmentView.findViewById(R.id.title);
        subjectTopicText = fragmentView.findViewById(R.id.subjectTopicText);
        subjectSubTopicText = fragmentView.findViewById(R.id.subjectSubTopicText);
        divider = fragmentView.findViewById(R.id.divider);
    }
}
