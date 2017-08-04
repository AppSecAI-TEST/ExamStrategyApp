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
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.models.CurrentAffairsListObject;
import com.talentsprint.android.esa.models.CurrentAffairsObject;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.CircleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentAffairsListFragment extends Fragment {
    private RecyclerView topicsRecyclerView;
    private CurrentAffairsListObject currentAffairsListObject;

    public CurrentAffairsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_current_affairs_list, container, false);
        currentAffairsListObject = (CurrentAffairsListObject) getArguments().getSerializable(AppConstants.TOPICS);
        topicsRecyclerView = fragmentView.findViewById(R.id.topicsRecyclerView);
        if (currentAffairsListObject != null && currentAffairsListObject.getCurrentAffairArticles() != null &&
                currentAffairsListObject.getCurrentAffairArticles().size() > 0 && currentAffairsListObject
                .getCurrentAffairArticles().get(0).getArticles() != null) {
            CurrentAffairsAdapter adapter = new CurrentAffairsAdapter(currentAffairsListObject
                    .getCurrentAffairArticles().get(0).getArticles());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            topicsRecyclerView.setLayoutManager(mLayoutManager);
            topicsRecyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(getActivity(), "Current Affairs not available", Toast.LENGTH_SHORT).show();
        }
        return fragmentView;
    }

    public class CurrentAffairsAdapter extends RecyclerView.Adapter<CurrentAffairsAdapter.MyViewHolder> {

        private List<CurrentAffairsObject> affairsObjectList;

        public CurrentAffairsAdapter(ArrayList<CurrentAffairsObject> affairsObjectList) {
            this.affairsObjectList = affairsObjectList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_subject_article, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            CurrentAffairsObject currentAffairsObject = affairsObjectList.get(position);
            holder.topicName.setText(currentAffairsObject.getTitle());
            holder.subTopicName.setText(currentAffairsObject.getShortDescription());
            if (currentAffairsObject.getImageUrl() != null && currentAffairsObject.getImageUrl().trim().length() > 0)
                Picasso.with(getActivity()).load(currentAffairsObject.getImageUrl()).
                        transform(new CircleTransform())
                        .into(holder.topicImage);
        }

        @Override
        public int getItemCount() {
            return affairsObjectList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView topicName;
            public TextView subTopicName;
            public ImageView topicImage;

            public MyViewHolder(View view) {
                super(view);
                topicImage = view.findViewById(R.id.topicImage);
                subTopicName = view.findViewById(R.id.subTopicName);
                topicName = view.findViewById(R.id.topicName);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AppConstants.CURRENT_AFFAIRS, currentAffairsListObject.getCurrentAffairArticles
                                ().get(0).getArticles());
                        bundle.putInt(AppConstants.POSITION, getAdapterPosition());
                        CurrentAffairsViewPagerFragment currentAffairsViewPagerFragment = new CurrentAffairsViewPagerFragment();
                        currentAffairsViewPagerFragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .add(R.id.fragment_container, currentAffairsViewPagerFragment, AppConstants
                                        .CURRENT_AFFAIRS).addToBackStack(null).commit();
                    }
                });
            }
        }
    }
}
