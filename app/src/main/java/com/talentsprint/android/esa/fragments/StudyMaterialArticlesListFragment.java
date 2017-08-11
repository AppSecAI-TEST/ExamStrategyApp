package com.talentsprint.android.esa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.talentsprint.android.esa.models.ArticlesObject;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.CircleTransform;
import com.talentsprint.android.esa.utils.PreferenceManager;
import com.talentsprint.apps.talentsprint.R;

import java.util.ArrayList;

import static com.talentsprint.android.esa.utils.AppConstants.PREMIUM;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudyMaterialArticlesListFragment extends Fragment {
    private RecyclerView topicsRecyclerView;
    private TextView subjectTopicText;
    private TextView subjectSubTopicText;
    private ArticlesObject articlesObject;

    public StudyMaterialArticlesListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_study_material_articles_list, container, false);
        articlesObject = (ArticlesObject) getArguments().getSerializable(AppConstants.TOPICS);
        findViews(fragmentView);
        subjectTopicText.setText(articlesObject.getTopic() + " | ");
        subjectSubTopicText.setText(articlesObject.getSubTopic());
        ArticlesAdapter adapter = new ArticlesAdapter(articlesObject.getArticles());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        topicsRecyclerView.setLayoutManager(mLayoutManager);
        topicsRecyclerView.setAdapter(adapter);
        return fragmentView;
    }

    private void findViews(View fragmentView) {
        topicsRecyclerView = fragmentView.findViewById(R.id.topicsRecyclerView);
        subjectTopicText = fragmentView.findViewById(R.id.subjectTopicText);
        subjectSubTopicText = fragmentView.findViewById(R.id.subjectSubTopicText);
        subjectTopicText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void openToLogin(ArticlesObject.Articles article) {
        GotoRegisterFragment gotoRegisterFragment = new GotoRegisterFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstants.ARTICLE, article);
        bundle.putString(AppConstants.TOPICS, articlesObject.getTopic());
        bundle.putString(AppConstants.SUB_TOPIC, articlesObject.getSubTopic());
        gotoRegisterFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, gotoRegisterFragment, AppConstants.DASHBOARD)
                .addToBackStack(null).commit();
    }

    private void openContact(ArticlesObject.Articles article) {
        GoToContactFragment goToContactFragment = new GoToContactFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstants.ARTICLE, article);
        bundle.putString(AppConstants.TOPICS, articlesObject.getTopic());
        bundle.putString(AppConstants.SUB_TOPIC, articlesObject.getSubTopic());
        goToContactFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, goToContactFragment, AppConstants.DASHBOARD)
                .addToBackStack(null).commit();
    }

    private void openContent(ArticlesObject.Articles article) {
        StudyMaterialArticleView studyMaterialArticlesListFragment = new StudyMaterialArticleView();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstants.ARTICLE, article);
        studyMaterialArticlesListFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, studyMaterialArticlesListFragment, AppConstants.ARTICLE)
                .addToBackStack(null).commit();
    }

    public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.MyViewHolder> {

        private ArrayList<ArticlesObject.Articles> articlesList;

        public ArticlesAdapter(ArrayList<ArticlesObject.Articles> articlesList) {
            this.articlesList = articlesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_subject_article, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            ArticlesObject.Articles article = articlesList.get(position);
            holder.topicName.setText(article.getTitle());
            holder.subTopicName.setText(article.getShortDescription());
            if (article.getImageUrl() != null && article.getImageUrl().length() > 0)
                Picasso.with(getActivity()).load(article.getImageUrl()).transform(new CircleTransform()).into(holder.topicImage);
            else
                Picasso.with(getActivity()).load(R.drawable.run_animated).transform(new CircleTransform()).into(holder
                        .topicImage);
        }

        @Override
        public int getItemCount() {
            if (articlesList != null)
                return articlesList.size();
            else
                return 0;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView topicName;
            public TextView subTopicName;
            public ImageView topicImage;

            public MyViewHolder(View view) {
                super(view);
                topicName = view.findViewById(R.id.topicName);
                subTopicName = view.findViewById(R.id.subTopicName);
                topicImage = view.findViewById(R.id.topicImage);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArticlesObject.Articles article = articlesList.get(getAdapterPosition());
                        String accessType = article.getAccessType();
                        if (accessType.equalsIgnoreCase(AppConstants.FREE)) {
                            openContent(article);
                        } else if (accessType.equalsIgnoreCase(AppConstants
                                .FREEMIUM)) {
                            if (PreferenceManager.getBoolean(getActivity(), AppConstants.IS_LOGGEDIN)) {
                                openContent(article);
                            } else {
                                openToLogin(article);
                            }
                        } else if (accessType.equalsIgnoreCase(
                                PREMIUM)) {
                            if (PreferenceManager.getString(getActivity(), AppConstants.USER_TYPE, "").equalsIgnoreCase
                                    (PREMIUM)) {
                                openContent(article);
                            } else {
                                openContact(article);
                            }
                        }

                    }
                });
            }
        }
    }
}
