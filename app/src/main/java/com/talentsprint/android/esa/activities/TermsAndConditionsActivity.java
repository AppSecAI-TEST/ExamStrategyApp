package com.talentsprint.android.esa.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import com.talentsprint.apps.talentsprint.R;
import com.talentsprint.android.esa.utils.AppConstants;

public class TermsAndConditionsActivity extends Activity implements View.OnClickListener {

    private TextView termsConditions;
    private View termsView;
    private TextView privacyPolicy;
    private View privacyView;
    private WebView webView;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_terms_and_conditions);
        findViews();
        if (getIntent().getIntExtra(AppConstants.CONTENT, 0) == 1) {
            loadTermsConditions();
        } else {
            loadPrivacyPolicy();
        }
    }

    private void findViews() {
        termsConditions = findViewById(R.id.termsConditions);
        termsView = findViewById(R.id.termsView);
        privacyPolicy = findViewById(R.id.privacyPolicy);
        privacyView = findViewById(R.id.privacyView);
        webView = findViewById(R.id.webView);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        termsConditions.setOnClickListener(this);
        privacyPolicy.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public void onClick(View view) {
        if (view == back) {
            onBackPressed();
        } else if (view == privacyPolicy) {
            loadPrivacyPolicy();
        } else if (view == termsConditions) {
            loadTermsConditions();
        }
    }

    private void loadTermsConditions() {
        webView.loadUrl("https://www.talentsprint.com/terms-conditions.dpl");
        termsConditions.setTextColor(ContextCompat.getColor(TermsAndConditionsActivity.this,
                R.color.textTitleColor));
        privacyPolicy.setTextColor(ContextCompat.getColor(TermsAndConditionsActivity.this,
                R.color.textTitleColor40Opc));
        termsView.setBackgroundColor(ContextCompat.getColor(TermsAndConditionsActivity.this,
                R.color.colorPrimary));
        privacyView.setBackgroundColor(ContextCompat.getColor(TermsAndConditionsActivity.this,
                R.color.colorPrimary40Opc));
    }

    private void loadPrivacyPolicy() {
        webView.loadUrl("https://www.talentsprint.com/privacy-policy.dpl");
        privacyPolicy.setTextColor(ContextCompat.getColor(TermsAndConditionsActivity.this,
                R.color.textTitleColor));
        termsConditions.setTextColor(ContextCompat.getColor(TermsAndConditionsActivity.this,
                R.color.textTitleColor40Opc));
        privacyView.setBackgroundColor(ContextCompat.getColor(TermsAndConditionsActivity.this,
                R.color.colorPrimary));
        termsView.setBackgroundColor(ContextCompat.getColor(TermsAndConditionsActivity.this,
                R.color.colorPrimary40Opc));
    }
}
