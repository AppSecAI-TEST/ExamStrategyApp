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
import com.talentsprint.android.esa.models.SubTopicsObject;
import com.talentsprint.android.esa.models.TopicsObject;
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
public class StudyMaterialTopicsFragment extends Fragment {

    private RecyclerView topicsRecyclerView;
    private TopicsObject topicsObject;
    private StudyMaterialActivityInterface studyMaterialActivityInterface;

    public StudyMaterialTopicsFragment() {
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
        View fragmentView = inflater.inflate(R.layout.fragment_study_material_topics, container, false);
        topicsRecyclerView = fragmentView.findViewById(R.id.topicsRecyclerView);
        topicsObject = (TopicsObject) getArguments().getSerializable(AppConstants.TOPICS);
        TopicsAdapter adapter = new TopicsAdapter(topicsObject.getTopics());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        topicsRecyclerView.setLayoutManager(mLayoutManager);
        topicsRecyclerView.setAdapter(adapter);
        return fragmentView;
    }

    private void getSubTopics(String topicName) {
        studyMaterialActivityInterface.showProgress(true);
        TalentSprintApi apiService =
                ApiClient.getCacheClient().create(TalentSprintApi.class);
        Call<SubTopicsObject> getExams = apiService.getSubTopics(topicsObject.getExam(), topicsObject.getSubject(), topicName);
        getExams.enqueue(new Callback<SubTopicsObject>() {
            @Override
            public void onResponse(Call<SubTopicsObject> call, Response<SubTopicsObject> response) {
                studyMaterialActivityInterface.showProgress(false);
                if (response.isSuccessful()) {
                    StudyMaterialSubTopicFragment studyMaterialSubTopicFragment = new StudyMaterialSubTopicFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AppConstants.TOPICS, response.body());
                    studyMaterialSubTopicFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, studyMaterialSubTopicFragment, AppConstants.DASHBOARD)
                            .addToBackStack(null).commit();
                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<SubTopicsObject> call, Throwable t) {
                try {
                    studyMaterialActivityInterface.showProgress(false);
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.MyViewHolder> {

        private List<String> topicsList;

        public TopicsAdapter(List<String> topicsList) {
            this.topicsList = topicsList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_subject_topic, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.topicName.setText(topicsList.get(position));
        }

        @Override
        public int getItemCount() {
            if (topicsList != null)
                return topicsList.size();
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
                        getSubTopics(topicsList.get(getAdapterPosition()));
                    }
                });
            }
        }
    }
}
