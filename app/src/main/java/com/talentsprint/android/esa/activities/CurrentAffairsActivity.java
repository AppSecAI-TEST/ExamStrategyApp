package com.talentsprint.android.esa.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.models.AffairsTopicObject;

import java.util.ArrayList;
import java.util.List;

public class CurrentAffairsActivity extends Activity {
    private RecyclerView topicsRecyclerView;
    private ArrayList<AffairsTopicObject> topicsList = new ArrayList<AffairsTopicObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_current_affairs);
        addTopics();
        topicsRecyclerView = findViewById(R.id.topicsRecyclerView);
        SubjectsAdapter adapter = new SubjectsAdapter(topicsList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        topicsRecyclerView.setLayoutManager(mLayoutManager);
        topicsRecyclerView.setAdapter(adapter);
    }

    private void addTopics() {
        /*National,International,Banking Economy and Finance,
        Art & Culture,Sports,Awards & Honors,Science & Technology,Environment - Topics given by Sravya and confirmed that they
        need to be hardcoded*/
        AffairsTopicObject national = new AffairsTopicObject();
        national.setTopicName("National");
        national.setTopicImage(R.drawable.national);
        topicsList.add(national);
        AffairsTopicObject international = new AffairsTopicObject();
        international.setTopicName("International");
        international.setTopicImage(R.drawable.international);
        topicsList.add(international);
        AffairsTopicObject banking = new AffairsTopicObject();
        banking.setTopicName("Banking Economy and Finance");
        banking.setTopicImage(R.drawable.business_finance);
        topicsList.add(banking);
        AffairsTopicObject art = new AffairsTopicObject();
        art.setTopicName("Art & Culture");
        art.setTopicImage(R.drawable.affairs_everything);
        topicsList.add(art);
        AffairsTopicObject sports = new AffairsTopicObject();
        sports.setTopicName("Sports");
        sports.setTopicImage(R.drawable.sports);
        topicsList.add(sports);
        AffairsTopicObject awards = new AffairsTopicObject();
        awards.setTopicName("Awards & Honors");
        awards.setTopicImage(R.drawable.awards);
        topicsList.add(awards);
        AffairsTopicObject science = new AffairsTopicObject();
        science.setTopicName("Science & Technology");
        science.setTopicImage(R.drawable.science_tencnology);
        topicsList.add(science);
        AffairsTopicObject environment = new AffairsTopicObject();
        environment.setTopicName("Environment");
        environment.setTopicImage(R.drawable.environment);
        topicsList.add(environment);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.MyViewHolder> {

        private List<AffairsTopicObject> currentAffairsList;

        public SubjectsAdapter(ArrayList<AffairsTopicObject> currentAffairsList) {
            this.currentAffairsList = currentAffairsList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_current_affair_selection, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if (position % 2 != 0) {
                holder.divider.setVisibility(View.GONE);
            } else {
                holder.divider.setVisibility(View.VISIBLE);
            }
            AffairsTopicObject affairsTopicObject = currentAffairsList.get(position);
            holder.topicName.setText(affairsTopicObject.getTopicName());
            holder.topicImage.setImageResource(affairsTopicObject.getTopicImage());
        }

        @Override
        public int getItemCount() {
            return 8;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public View divider;
            public TextView topicName;
            public ImageView topicImage;

            public MyViewHolder(View view) {
                super(view);
                divider = view.findViewById(R.id.divider);
                topicImage = view.findViewById(R.id.topicImage);
                topicName = view.findViewById(R.id.topicName);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent navigate = new Intent(CurrentAffairsActivity.this, CurrentAffairsTopicsActivity.class);
                        startActivity(navigate);
                    }
                });
            }
        }
    }
}
