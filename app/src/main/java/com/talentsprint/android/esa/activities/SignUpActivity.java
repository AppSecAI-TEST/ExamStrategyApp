package com.talentsprint.android.esa.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.talentsprint.android.esa.R;

public class SignUpActivity extends Activity {

    private ImageView back;
    private EditText emailEdtTxt;
    private EditText nameEdtTxt;
    private EditText mobileEdtTxt;
    private TextView singUp;
    private ImageView googleLogin;
    private ImageView fbLogin;
    private ImageView emailLogin;
    private TextView termsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_sign_up);
        findViews();
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent navigate = new Intent(SignUpActivity.this, TermsAndConditionsActivity.class);
                startActivity(navigate);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent navigate = new Intent(SignUpActivity.this, TermsAndConditionsActivity.class);
                startActivity(navigate);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        termsText.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString span = new SpannableString(termsText.getText().toString());
        String[] termsSplits = termsText.getText().toString().split("Terms and Conditions");
        int termsSlitStart = termsSplits[0].length() - 1;
        int termsSplitEnd = termsText.getText().toString().length() - termsSplits[1].length();
        String[] privacySplits = termsText.getText().toString().split("Privacy policy");
        int privacySlitStart = privacySplits[0].length() - 1;
        int privacySplitEnd = termsText.getText().toString().length();
        span.setSpan(clickableSpan1, termsSlitStart, termsSplitEnd, Spanned
                .SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(Color.parseColor("#3366ff")), termsSlitStart, termsSplitEnd,
                Spannable
                        .SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(clickableSpan, privacySlitStart, privacySplitEnd, Spanned
                .SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(Color.parseColor("#3366ff")), privacySlitStart, privacySplitEnd,
                Spannable
                        .SPAN_EXCLUSIVE_EXCLUSIVE);
        termsText.setText(span);
    }

    private void findViews() {
        back = findViewById(R.id.back);
        emailEdtTxt = findViewById(R.id.emailEdtTxt);
        nameEdtTxt = findViewById(R.id.nameEdtTxt);
        mobileEdtTxt = findViewById(R.id.mobileEdtTxt);
        singUp = findViewById(R.id.singUp);
        googleLogin = findViewById(R.id.googleLogin);
        fbLogin = findViewById(R.id.fbLogin);
        emailLogin = findViewById(R.id.emailLogin);
        termsText = findViewById(R.id.termsText);
    }

}
