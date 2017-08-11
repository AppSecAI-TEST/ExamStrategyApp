package com.talentsprint.android.esa.dialogues;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.talentsprint.android.esa.interfaces.FiltersInterface;
import com.talentsprint.android.esa.models.StratergyObject;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.apps.talentsprint.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Anudeep Reddy on 7/7/2017.
 */

public class FilterDialogue extends DialogFragment {
    private float x_value, y_value;
    private View main_content;
    private View movableContent;
    private View pointerView;
    private LinearLayout filtersRecyclerHolder;
    private TextView filter1Text;
    private RecyclerView filterRecycler1;
    private TextView filter2Text;
    private RecyclerView filterRecycler2;
    private View apply;
    private View cancel;
    private StratergyObject.FilterOptions filterOptions;
    private FiltersInterface filtersInterface;
    private HashMap<String, Boolean> selectedContentFilters = new HashMap<String, Boolean>();
    private HashMap<String, Boolean> selectedSubjectFilters = new HashMap<String, Boolean>();

    public FilterDialogue() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        filtersInterface = (FiltersInterface) getParentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialogue_filter, container);
        x_value = getArguments().getFloat(AppConstants.X_VALUE);
        y_value = getArguments().getFloat(AppConstants.Y_VALUE);
        filterOptions = (StratergyObject.FilterOptions) getArguments().getSerializable(AppConstants.FILTERS);
        selectedContentFilters = (HashMap<String, Boolean>) getArguments().getSerializable(AppConstants.CONTENT_FILTERS);
        selectedSubjectFilters = (HashMap<String, Boolean>) getArguments().getSerializable(AppConstants.SUBJECT_FILTERS);
        findViews(view);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
        movableContent.setY(y_value);
        pointerView.setX(x_value);
        FiltersAdapter contentTypeAdapter = new FiltersAdapter(filterOptions.getContentType(), selectedContentFilters);
        RecyclerView.LayoutManager mContentTypeLayoutManager = new LinearLayoutManager(getActivity());
        filterRecycler1.setLayoutManager(mContentTypeLayoutManager);
        filterRecycler1.setAdapter(contentTypeAdapter);
        FiltersAdapter subjectsAdapter = new FiltersAdapter(filterOptions.getSubjects(), selectedSubjectFilters);
        RecyclerView.LayoutManager mSubjectsLayoutManager = new LinearLayoutManager(getActivity());
        filterRecycler2.setLayoutManager(mSubjectsLayoutManager);
        filterRecycler2.setAdapter(subjectsAdapter);
        main_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        movableContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtersInterface.filtersSet(selectedContentFilters, selectedSubjectFilters);
                dismiss();
            }
        });
        return view;
    }

    private void findViews(View view) {
        main_content = view.findViewById(R.id.main_content);
        movableContent = view.findViewById(R.id.movableContent);
        pointerView = view.findViewById(R.id.pointerView);
        filtersRecyclerHolder = view.findViewById(R.id.filtersRecyclerHolder);
        filter1Text = view.findViewById(R.id.filter1Text);
        filterRecycler1 = view.findViewById(R.id.filterRecycler1);
        filter2Text = view.findViewById(R.id.filter2Text);
        filterRecycler2 = view.findViewById(R.id.filterRecycler2);
        apply = view.findViewById(R.id.apply);
        cancel = view.findViewById(R.id.cancel);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.MyViewHolder> {

        public HashMap<String, Boolean> filterSelected;
        private List<String> filtersList;

        public FiltersAdapter(List<String> filtersList, HashMap<String, Boolean> filterSelected) {
            this.filtersList = filtersList;
            this.filterSelected = filterSelected;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_filter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if (filterSelected.containsKey(filtersList.get(position).toLowerCase())) {
                holder.checkImage.setImageResource(R.drawable.filter_check);
            } else {
                holder.checkImage.setImageResource(R.drawable.filter_uncheck);
            }
            holder.filterText.setText(filtersList.get(position));
        }

        @Override
        public int getItemCount() {
            if (filtersList == null)
                return 0;
            else
                return filtersList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView filterText;
            public ImageView checkImage;

            public MyViewHolder(View view) {
                super(view);
                checkImage = view.findViewById(R.id.checkImage);
                filterText = view.findViewById(R.id.filterText);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (filterSelected.containsKey(filtersList.get(getAdapterPosition()).toLowerCase())) {
                            filterSelected.remove(filtersList.get(getAdapterPosition()).toLowerCase());
                        } else {
                            filterSelected.put(filtersList.get(getAdapterPosition()).toLowerCase(), true);
                        }
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }
}
