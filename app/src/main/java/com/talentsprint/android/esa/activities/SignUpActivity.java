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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.utils.ApiClient;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.AppUtils;
import com.talentsprint.android.esa.utils.PreferenceManager;
import com.talentsprint.android.esa.utils.TalentSprintApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends Activity {

    private ImageView back;
    private EditText emailEdtTxt;
    private EditText nameEdtTxt;
    private EditText mobileEdtTxt;
    private TextView singUp;
    private TextView termsText;
    private ProgressBar progressBar;
    private View progressBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_sign_up);
        findViews();
        setSpanToTerms();
        mobileEdtTxt.setText(PreferenceManager.getString(SignUpActivity.this, AppConstants.MOBILE_NUMBER, ""));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameEdtTxt.getText().toString().trim().length() < 1) {
                    Toast.makeText(SignUpActivity.this, "Enter a valid name", Toast.LENGTH_SHORT).show();
                } else if (!AppUtils.isEmailValid(emailEdtTxt.getText().toString().trim())) {
                    Toast.makeText(SignUpActivity.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser();
                }
            }
        });
    }

    private void registerUser() {
        showProgress(true);
        AppUtils.closeKeyboard(singUp, SignUpActivity.this);
        TalentSprintApi apiService =
                ApiClient.getCacheClient(false).create(TalentSprintApi.class);
        Call<JsonObject> getExams = apiService.registerUser(emailEdtTxt.getText().toString().trim(), nameEdtTxt.getText()
                .toString().trim(), mobileEdtTxt.getText().toString().trim());
        getExams.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                showProgress(false);
                if (response.isSuccessful()) {
                    try {
                        JsonObject responseBody = response.body();
                        if (responseBody.get("status").getAsString().equalsIgnoreCase("Ok")) {
                            PreferenceManager.saveString(SignUpActivity.this, AppConstants.USER_TYPE, responseBody.get
                                    ("accessType").getAsString());
                            PreferenceManager.saveBoolean(SignUpActivity.this, AppConstants.MOBILE_USER, true);
                            PreferenceManager.saveBoolean(SignUpActivity.this, AppConstants.IS_LOGGEDIN, true);
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, responseBody.get("status").getAsString(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                try {
                    showProgress(false);
                    Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setSpanToTerms() {
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
        termsText = findViewById(R.id.termsText);
        progressBar = findViewById(R.id.progressBar);
        progressBarView = findViewById(R.id.progressBarView);
        progressBar.setVisibility(View.GONE);
        progressBarView.setVisibility(View.GONE);
    }

    public void showProgress(boolean isShow) {
        try {
            if (isShow) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarView.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
                progressBarView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
