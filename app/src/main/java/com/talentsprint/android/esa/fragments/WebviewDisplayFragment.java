package com.talentsprint.android.esa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.talentsprint.apps.talentsprint.R;
import com.squareup.picasso.Picasso;
import com.talentsprint.android.esa.utils.RoundedCornerTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebviewDisplayFragment extends Fragment {

    ImageView webViewContainer;
    TextView title;
    TextView description;

    public WebviewDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_webview_display, container, false);
        title = fragmentView.findViewById(R.id.title);
        description = fragmentView.findViewById(R.id.description);
        webViewContainer = fragmentView.findViewById(R.id.webViewContainer);
        Picasso.with(getActivity()).load(getArguments().getString("imageURL")).
                transform(new RoundedCornerTransformation(5, 0, RoundedCornerTransformation.CornerType.ALL)).into(webViewContainer);
        title.setText(getArguments().getString("title"));
        description.setText(getArguments().getString("description"));
        return fragmentView;
    }

}
