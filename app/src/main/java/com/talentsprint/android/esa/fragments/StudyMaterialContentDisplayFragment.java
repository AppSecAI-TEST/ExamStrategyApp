package com.talentsprint.android.esa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.apps.talentsprint.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudyMaterialContentDisplayFragment extends Fragment {

    private WebView webViewContainer;
    private TextView subjectTopicText;
    private TextView subjectSubTopicText;

    public StudyMaterialContentDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_study_material_content_display, container, false);
        findViews(fragmentView);
        subjectTopicText.setText(getArguments().getString(AppConstants.TOPICS, "") + " | ");
        subjectSubTopicText.setText(getArguments().getString(AppConstants.SUB_TOPIC, ""));
        webViewContainer.getSettings().setJavaScriptEnabled(true);
        String url = getArguments().getString(AppConstants.URL);
        if (url.contains(".pdf")) {
            url = "http://docs.google.com/viewer?url=" + url;
        }
        webViewContainer.loadUrl(url);
        return fragmentView;
    }

    private void findViews(View fragmentView) {
        webViewContainer = fragmentView.findViewById(R.id.webViewContainer);
        subjectTopicText = fragmentView.findViewById(R.id.subjectTopicText);
        subjectSubTopicText = fragmentView.findViewById(R.id.subjectSubTopicText);
    }

}
