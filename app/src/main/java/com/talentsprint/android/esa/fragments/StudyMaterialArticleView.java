package com.talentsprint.android.esa.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.talentsprint.apps.talentsprint.R;
import com.talentsprint.android.esa.TalentSprintApp;
import com.talentsprint.android.esa.activities.VideoPlayerActivity;
import com.talentsprint.android.esa.interfaces.DashboardActivityInterface;
import com.talentsprint.android.esa.interfaces.StudyMaterialActivityInterface;
import com.talentsprint.android.esa.models.ArticlesObject;
import com.talentsprint.android.esa.utils.ApiClient;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.RoundedCornersTransform;
import com.talentsprint.android.esa.utils.TalentSprintApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudyMaterialArticleView extends Fragment {

    private TextView subjectTopicText;
    private TextView subjectSubTopicText;
    private ArticlesObject.Articles articleObject;
    private TextView title;
    private RelativeLayout imageView;
    private ImageView articleImage;
    private ImageView videoPlay;
    private TextView authorInitial;
    private TextView authorName;
    private TextView description;
    private TextView done;
    private View divider;
    private DashboardActivityInterface dashboardActivityInterface;
    private StudyMaterialActivityInterface studyMaterialActivityInterface;

    public StudyMaterialArticleView() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            dashboardActivityInterface = (DashboardActivityInterface) getActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            studyMaterialActivityInterface = (StudyMaterialActivityInterface) getActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findViews(View fragmentView) {
        subjectTopicText = fragmentView.findViewById(R.id.subjectTopicText);
        subjectSubTopicText = fragmentView.findViewById(R.id.subjectSubTopicText);
        title = fragmentView.findViewById(R.id.title);
        imageView = fragmentView.findViewById(R.id.imageView);
        articleImage = fragmentView.findViewById(R.id.articleImage);
        videoPlay = fragmentView.findViewById(R.id.videoPlay);
        authorInitial = fragmentView.findViewById(R.id.authorInitial);
        authorName = fragmentView.findViewById(R.id.authorName);
        description = fragmentView.findViewById(R.id.description);
        done = fragmentView.findViewById(R.id.done);
        divider = fragmentView.findViewById(R.id.divider);
        subjectTopicText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_study_material_article_view, container, false);
        findViews(fragmentView);
        if (dashboardActivityInterface != null) {
            dashboardActivityInterface.setCurveVisibility(true);
        }
        articleObject = (ArticlesObject.Articles) getArguments().getSerializable(AppConstants.ARTICLE);
        if (articleObject == null || articleObject.getArticleInfo() == null)
            getActivity().onBackPressed();
        else
            setValues();
        return fragmentView;
    }

    private void setValues() {
        subjectTopicText.setText(articleObject.getArticleInfo().getTopic() + " | ");
        subjectSubTopicText.setText(articleObject.getArticleInfo().getSubTopic());
        title.setText(articleObject.getArticleInfo().getTitle());
        description.setText(articleObject.getArticleInfo().getDescription());
        if ((articleObject.getImageUrl() != null && articleObject.getImageUrl().length() > 0) || articleObject.getType()
                .equalsIgnoreCase(AppConstants.VIDEO)) {
            imageView.setVisibility(View.VISIBLE);
            if (articleObject.getType().equalsIgnoreCase(AppConstants.VIDEO)) {
                videoPlay.setVisibility(View.VISIBLE);
                try {
                    if (articleObject.getArticleInfo().getThumbnail() != null && articleObject.getArticleInfo().getThumbnail()
                            .length() > 0)
                        Picasso.with(getActivity()).load(articleObject.getArticleInfo().getThumbnail()).transform(new
                                RoundedCornersTransform()).into(articleImage);
                    else
                        articleImage.setImageResource(R.drawable.logo_color);
                } catch (Exception throwable) {
                    throwable.printStackTrace();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent navigate = new Intent(getActivity(), VideoPlayerActivity.class);
                        navigate.putExtra(AppConstants.URL, articleObject.getArticleInfo().getContentUrl());
                        navigate.putExtra(AppConstants.TASK_ID, articleObject.getArticleInfo().getId());
                        navigate.putExtra(AppConstants.ARTICLE, articleObject.getArticleInfo().getId());
                        startActivity(navigate);
                    }
                });
            } else {
                videoPlay.setVisibility(View.GONE);
                Picasso.with(getActivity()).load(articleObject.getImageUrl()).into(articleImage);
            }
        } else {
            imageView.setVisibility(View.GONE);
        }
        if (articleObject.getArticleInfo().getTeacherName() != null &&
                articleObject.getArticleInfo().getTeacherName().trim().length()
                        > 0) {
            authorInitial.setVisibility(View.VISIBLE);
            authorName.setVisibility(View.VISIBLE);
            authorInitial.setText(articleObject.getArticleInfo().getTeacherName().trim().charAt(0));
            authorName.setText(articleObject.getArticleInfo().getTeacherName().trim());
        } else {
            authorInitial.setVisibility(View.GONE);
            authorName.setVisibility(View.GONE);
        }
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeTask();
            }
        });
        if (getArguments().getBoolean(AppConstants.DASHBOARD, false)) {
            done.setVisibility(View.VISIBLE);
            subjectSubTopicText.setVisibility(View.GONE);
            subjectTopicText.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        } else {
            done.setVisibility(View.GONE);
            subjectSubTopicText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                }
            });
            subjectTopicText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                    studyMaterialActivityInterface.doubleBack();
                }
            });
        }
    }

    private void completeTask() {
        if (dashboardActivityInterface != null)
            dashboardActivityInterface.showProgress(true);
        else
            studyMaterialActivityInterface.showProgress(true);
        TalentSprintApi apiService =
                ApiClient.getCacheClient().create(TalentSprintApi.class);
        Call<JsonObject> getExams = apiService.taskComplete(articleObject.getTaskId(), Long.toString(articleObject
                .getArticleInfo().getId()));
        getExams.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (dashboardActivityInterface != null)
                    dashboardActivityInterface.showProgress(false);
                else
                    studyMaterialActivityInterface.showProgress(false);
                if (response.isSuccessful()) {
                    TalentSprintApp.refreshDashBorad = true;
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                try {
                    if (dashboardActivityInterface != null)
                        dashboardActivityInterface.showProgress(false);
                    else
                        studyMaterialActivityInterface.showProgress(false);
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
