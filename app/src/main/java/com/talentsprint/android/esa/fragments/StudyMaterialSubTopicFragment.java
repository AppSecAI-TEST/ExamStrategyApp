package com.talentsprint.android.esa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.interfaces.StudyMaterialActivityInterface;
import com.talentsprint.android.esa.models.ArticlesObject;
import com.talentsprint.android.esa.models.SubTopicsObject;
import com.talentsprint.android.esa.utils.ApiClient;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.TalentSprintApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudyMaterialSubTopicFragment extends Fragment {
    private RecyclerView topicsRecyclerView;
    private TextView subjectTopicText;
    private SubTopicsObject subTopicsObject;
    private StudyMaterialActivityInterface studyMaterialActivityInterface;

    public StudyMaterialSubTopicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        studyMaterialActivityInterface = (StudyMaterialActivityInterface) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_study_material_sub_topic, container, false);
        subTopicsObject = (SubTopicsObject) getArguments().getSerializable(AppConstants.TOPICS);
        findViews(fragmentView);
        subjectTopicText.setText(subTopicsObject.getTopic());
        SubTopicsAdapter adapter = new SubTopicsAdapter(subTopicsObject.getSubTopics());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        topicsRecyclerView.setLayoutManager(mLayoutManager);
        topicsRecyclerView.setAdapter(adapter);
        return fragmentView;
    }

    private void findViews(View fragmentView) {
        topicsRecyclerView = fragmentView.findViewById(R.id.topicsRecyclerView);
        subjectTopicText = fragmentView.findViewById(R.id.subjectTopicText);
    }

    private void getArticles(String subTopicName) {
        studyMaterialActivityInterface.showProgress(true);
        TalentSprintApi apiService =
                ApiClient.getCacheClient().create(TalentSprintApi.class);
        Call<ArticlesObject> getExams = apiService.getArticles(subTopicsObject.getExam(), subTopicsObject.getSubject(),
                subTopicsObject.getTopic(), subTopicName);
        getExams.enqueue(new Callback<ArticlesObject>() {
            @Override
            public void onResponse(Call<ArticlesObject> call, Response<ArticlesObject> response) {
                studyMaterialActivityInterface.showProgress(false);
                if (response.isSuccessful()) {
                    StudyMaterialArticlesListFragment studyMaterialArticlesListFragment = new StudyMaterialArticlesListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AppConstants.TOPICS, response.body());
                    studyMaterialArticlesListFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, studyMaterialArticlesListFragment, AppConstants.DASHBOARD)
                            .addToBackStack(null).commit();
                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<ArticlesObject> call, Throwable t) {
                try {
                    studyMaterialActivityInterface.showProgress(false);
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class SubTopicsAdapter extends RecyclerView.Adapter<SubTopicsAdapter.MyViewHolder> {

        private List<String> subtopicsList;

        public SubTopicsAdapter(List<String> topicsList) {
            this.subtopicsList = topicsList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_subject_sub_topic, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.topicName.setText(subtopicsList.get(position));
        }

        @Override
        public int getItemCount() {
            if (subtopicsList != null)
                return subtopicsList.size();
            else
                return 0;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView topicName;

            public MyViewHolder(View view) {
                super(view);
                topicName = view.findViewById(R.id.topicName);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getArticles(subtopicsList.get(getAdapterPosition()));
                    }
                });
            }
        }
    }
}
