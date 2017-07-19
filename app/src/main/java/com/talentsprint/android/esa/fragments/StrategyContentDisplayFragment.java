package com.talentsprint.android.esa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.interfaces.DashboardActivityInterface;
import com.talentsprint.android.esa.utils.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class StrategyContentDisplayFragment extends Fragment {

    WebView webViewContainer;
    private DashboardActivityInterface dashboardInterface;

    public StrategyContentDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dashboardInterface.setCurveVisibility(true);
        View fragmentView = inflater.inflate(R.layout.fragment_strategy_content_display, container, false);
        webViewContainer = fragmentView.findViewById(R.id.webViewContainer);
        webViewContainer.getSettings().setJavaScriptEnabled(true);
        String url = getArguments().getString(AppConstants.URL);
        if (url.contains(".pdf")) {
            url = "http://docs.google.com/viewer?url=" + url;
        }
        webViewContainer.loadUrl(url);
        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dashboardInterface = (DashboardActivityInterface) context;
    }
}
