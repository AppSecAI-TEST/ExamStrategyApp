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

import com.talentsprint.android.esa.R;

import java.util.ArrayList;
import java.util.List;

public class CurrentAffairsActivity extends Activity {
    private RecyclerView topicsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_current_affairs);
        topicsRecyclerView = findViewById(R.id.topicsRecyclerView);
        SubjectsAdapter adapter = new SubjectsAdapter(new ArrayList<String>());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        topicsRecyclerView.setLayoutManager(mLayoutManager);
        topicsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.MyViewHolder> {

        private List<String> alertssList;

        public SubjectsAdapter(List<String> alertssList) {
            this.alertssList = alertssList;
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
        }

        @Override
        public int getItemCount() {
            return 8;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public View divider;

            public MyViewHolder(View view) {
                super(view);
                divider = view.findViewById(R.id.divider);
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
