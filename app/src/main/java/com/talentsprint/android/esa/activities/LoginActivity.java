package com.talentsprint.android.esa.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.JsonObject;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.fragments.LoginHomeFragment;
import com.talentsprint.android.esa.interfaces.LoginInterface;
import com.talentsprint.android.esa.utils.ApiClient;
import com.talentsprint.android.esa.utils.ApiUrls;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.PreferenceManager;
import com.talentsprint.android.esa.utils.TalentSprintApi;

import org.json.JSONObject;

import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends FragmentActivity implements LoginInterface, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 99;
    ProgressBar progressBar;
    private CallbackManager callbackManager;
    private View googleLogin;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_login);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LoginHomeFragment(), AppConstants.DASHBOARD).commit();
        callbackManager = CallbackManager.Factory.create();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, LoginActivity.this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void googleLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void showProgress(boolean isShow) {
        if (isShow) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public void loginGoogle(View googleLogin) {
        this.googleLogin = googleLogin;
        googleLogin();
    }

    @Override
    public void loginFb(View fbLogin) {
        fbLogin(fbLogin);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        googleLogin.setClickable(true);
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Uri photoUrl = acct.getPhotoUrl();
            String profilePic = null;
            if (photoUrl != null)
                profilePic = photoUrl.toString();
            loginUser(false, null, acct.getDisplayName(), acct.getEmail(), profilePic);
        } else {
            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginUser(final boolean isFacebook, String fbId, String name, String email, String profilePic) {
        showProgress(true);
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        String oneSignalId = status.getSubscriptionStatus().getUserId();
        TalentSprintApi apiService =
                ApiClient.getClient().create(TalentSprintApi.class);
        Call<JsonObject> getHomeDetails;
        RequestBody fbIdBody = null;
        RequestBody profilePicBody = null;
        RequestBody oneSignalIdBody = null;
        RequestBody emailBody = null;
        if (fbId != null)
            fbIdBody = RequestBody.create(MediaType.parse("text/plain"), fbId);
        if (profilePic != null)
            profilePicBody = RequestBody.create(MediaType.parse("text/plain"), profilePic);
        if (oneSignalId != null)
            oneSignalIdBody = RequestBody.create(MediaType.parse("text/plain"), oneSignalId);
        if (email != null)
            emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        JsonObject body = new JsonObject();
        if (isFacebook) {
            getHomeDetails = apiService.loginFb(fbIdBody, profilePicBody, oneSignalIdBody);
        } else {
            getHomeDetails = apiService.loginGoogle(emailBody, profilePicBody, oneSignalIdBody);
        }
        body.addProperty(ApiUrls.ONE_SIGNAL, oneSignalId);
        if (profilePic != null)
            body.addProperty(ApiUrls.PROFILE_PIC_URL, profilePic);
        getHomeDetails.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                showProgress(false);
                if (response.isSuccessful()) {
                    try {
                        JsonObject responseBody = response.body();
                        if (responseBody.get("status").getAsString().equalsIgnoreCase("Ok")) {
                            PreferenceManager.saveString(LoginActivity.this, AppConstants.USER_TYPE, responseBody.get
                                    ("accessType").getAsString());
                            if (isFacebook)
                                PreferenceManager.saveBoolean(LoginActivity.this, AppConstants.FB_USER, true);
                            else
                                PreferenceManager.saveBoolean(LoginActivity.this, AppConstants.GMAIL_USER, true);
                            PreferenceManager.saveBoolean(LoginActivity.this, AppConstants.IS_LOGGEDIN, true);
                            LoginActivity.this.finish();
                        } else {
                            Toast.makeText(LoginActivity.this, responseBody.get("status").getAsString(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showProgress(false);
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fbLogin(final View fbLogin) {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        fbLogin.setClickable(true);
                        showProgress(true);
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest
                                .GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject json, GraphResponse response) {
                                if (response.getError() != null) {
                                    Toast.makeText(LoginActivity.this, response.getError().getErrorMessage(), Toast.LENGTH_SHORT)
                                            .show();
                                    showProgress(false);
                                } else {
                                    String jsonresult = String.valueOf(json);
                                    System.out.println("JSON Result" + jsonresult);
                                    String fbUserId = json.optString("id");
                                    String fbUserFirstName = json.optString("name");
                                    String fbUserEmail = json.optString("email");
                                    String fbUserProfilePics = "http://graph.facebook.com/" + fbUserId + "/picture?type=large";
                                    loginUser(true, fbUserId, fbUserFirstName, fbUserEmail, fbUserProfilePics);
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "User cancelled Facebook login!", Toast.LENGTH_SHORT).show();
                        fbLogin.setClickable(true);
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                        fbLogin.setClickable(true);
                    }
                });
    }
}
