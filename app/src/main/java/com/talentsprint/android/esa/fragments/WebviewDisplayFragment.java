package com.talentsprint.android.esa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.utils.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebviewDisplayFragment extends Fragment {

    WebView webViewContainer;

    public WebviewDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_webview_display, container, false);
        webViewContainer = fragmentView.findViewById(R.id.webViewContainer);
        webViewContainer.loadUrl(getArguments().getString(AppConstants.URL));
        return fragmentView;
    }

}
