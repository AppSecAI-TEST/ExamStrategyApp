package com.talentsprint.android.esa.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.activities.LoginActivity;
import com.talentsprint.android.esa.utils.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class GotoRegisterFragment extends Fragment {

    View registerNow;
    private TextView registerContent;

    public GotoRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_goto_register, container, false);
        registerNow = fragmentView.findViewById(R.id.registerNow);
        registerContent = fragmentView.findViewById(R.id.registerContent);
        registerContent.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString span = new SpannableString(registerContent.getText().toString());
        String[] termsSplits = registerContent.getText().toString().split("FREE");
        int termsSlitStart = termsSplits[0].length() - 1;
        int termsSplitEnd = registerContent.getText().toString().length() - termsSplits[1].length();
        Typeface openSansBold = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/OpenSans-Bold.ttf");
        span.setSpan(openSansBold, termsSlitStart, termsSplitEnd, Spanned
                .SPAN_EXCLUSIVE_EXCLUSIVE);
        registerContent.setText(span);
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navigate = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(navigate, AppConstants.LOGIN_RESULT);
            }
        });
        return fragmentView;
    }

}
