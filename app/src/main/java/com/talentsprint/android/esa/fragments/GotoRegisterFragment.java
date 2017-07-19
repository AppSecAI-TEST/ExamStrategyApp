package com.talentsprint.android.esa.fragments;

import android.app.Activity;
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
import android.widget.Toast;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.activities.LoginActivity;
import com.talentsprint.android.esa.models.ArticlesObject;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.PreferenceManager;

import static com.talentsprint.android.esa.utils.AppConstants.LOGIN_RESULT;

/**
 * A simple {@link Fragment} subclass.
 */
public class GotoRegisterFragment extends Fragment {

    View registerNow;
    private TextView registerContent;
    private TextView title;
    private TextView subjectTopicText;
    private TextView subjectSubTopicText;
    private View login;
    private ArticlesObject.Articles article;

    public GotoRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_goto_register, container, false);
        article = (ArticlesObject.Articles) getArguments().getSerializable(AppConstants.ARTICLE);
        findViews(fragmentView);
        registerContent.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString span = new SpannableString(registerContent.getText().toString());
        String[] termsSplits = registerContent.getText().toString().split("FREE");
        int termsSlitStart = termsSplits[0].length() - 1;
        int termsSplitEnd = registerContent.getText().toString().length() - termsSplits[1].length();
        Typeface openSansBold = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/OpenSans-Bold.ttf");
        span.setSpan(openSansBold, termsSlitStart, termsSplitEnd, Spanned
                .SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), termsSlitStart, termsSplitEnd, Spanned
                .SPAN_EXCLUSIVE_EXCLUSIVE);
        registerContent.setText(span);
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToLogin();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToLogin();
            }
        });
        title.setText(article.getTitle());
        subjectTopicText.setText(getArguments().getString(AppConstants.TOPICS, "") + " | ");
        subjectSubTopicText.setText(getArguments().getString(AppConstants.SUB_TOPIC, ""));
        return fragmentView;
    }

    private void findViews(View fragmentView) {
        registerNow = fragmentView.findViewById(R.id.registerNow);
        registerContent = fragmentView.findViewById(R.id.registerContent);
        login = fragmentView.findViewById(R.id.login);
        title = fragmentView.findViewById(R.id.title);
        subjectTopicText = fragmentView.findViewById(R.id.subjectTopicText);
        subjectSubTopicText = fragmentView.findViewById(R.id.subjectSubTopicText);
    }

    private void navigateToLogin() {
        if (!PreferenceManager.getBoolean(getActivity(), AppConstants.FB_USER, false) && !PreferenceManager.getBoolean
                (getActivity(), AppConstants.GMAIL_USER, false)) {
            Intent navigate = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(navigate, LOGIN_RESULT);
        } else {
            Toast.makeText(getActivity(), "Already signed-in", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                getActivity().onBackPressed();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }
}
