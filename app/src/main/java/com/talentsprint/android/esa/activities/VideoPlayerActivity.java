package com.talentsprint.android.esa.activities;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.JsonObject;
import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.TalentSprintApp;
import com.talentsprint.android.esa.utils.ApiClient;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.ServiceManager;
import com.talentsprint.android.esa.utils.TalentSprintApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoPlayerActivity extends Activity {

    private VideoView videoView;
    private ProgressBar progressBar;
    private View progressBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_player);
        if (!new ServiceManager(TalentSprintApp.appContext).isNetworkAvailable()) {
            Toast.makeText(VideoPlayerActivity.this, "No network available", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            videoView = findViewById(R.id.videoView);
            progressBar = findViewById(R.id.progressBar);
            progressBarView = findViewById(R.id.progressBarView);
            progressBar.setVisibility(View.GONE);
            progressBarView.setVisibility(View.GONE);
            String url = getIntent().getStringExtra(AppConstants.URL);
            MediaController mc = new MediaController(this);
            mc.setAnchorView(videoView);
            mc.setMediaPlayer(videoView);
            Uri video = Uri.parse(url);
            videoView.setMediaController(mc);
            videoView.setVideoURI(video);
            videoView.requestFocus();
            videoView.start();
            showProgress(true);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    showProgress(false);
                }
            });
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    if (!new ServiceManager(TalentSprintApp.appContext).isNetworkAvailable()) {
                        Toast.makeText(VideoPlayerActivity.this, "No network available", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    return false;
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    completeTask();
                }
            });
        }
    }

    private void completeTask() {
        showProgress(true);
        TalentSprintApi apiService =
                ApiClient.getCacheClient().create(TalentSprintApi.class);
        Call<JsonObject> getExams = apiService.taskComplete(getIntent().getStringExtra(AppConstants.TASK_ID), getIntent()
                .getStringExtra(AppConstants.ARTICLE));
        getExams.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                showProgress(false);
                if (response.isSuccessful()) {
                    finish();
                } else {
                    Toast.makeText(VideoPlayerActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                try {
                    showProgress(false);
                    Toast.makeText(VideoPlayerActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void showProgress(boolean isShow) {
        if (isShow) {
            progressBar.setVisibility(View.VISIBLE);
            progressBarView.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            progressBarView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
