package com.talentsprint.android.esa.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.talentsprint.apps.talentsprint.R;
import com.google.gson.JsonObject;
import com.talentsprint.android.esa.TalentSprintApp;
import com.talentsprint.android.esa.dialogues.CalenderDialogue;
import com.talentsprint.android.esa.interfaces.CalenderInterface;
import com.talentsprint.android.esa.interfaces.DashboardActivityInterface;
import com.talentsprint.android.esa.models.ExamObject;
import com.talentsprint.android.esa.models.GetExamsObject;
import com.talentsprint.android.esa.utils.ApiClient;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.AppUtils;
import com.talentsprint.android.esa.utils.TalentSprintApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyExamsFragment extends Fragment implements View.OnClickListener, CalenderInterface {

    private ImageView add;
    private RelativeLayout noExams;
    private TextView setExam, save, cancel;
    private RecyclerView examsRecycler;
    private DashboardActivityInterface dashboardInterface;
    private ArrayList<ExamObject> addedExams = new ArrayList<ExamObject>();
    private HashMap<String, Integer> examsMap = new HashMap<String, Integer>();
    private AddExamsAdapter addExamsAdapter;
    private int showCalenderPosition = -1;

    public MyExamsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dashboardInterface = (DashboardActivityInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_my_exams, container, false);
        findViews(fragmentView);
        dashboardInterface.setCurveVisibility(true);
        getExams();
        return fragmentView;
    }

    private void getExams() {
        dashboardInterface.showProgress(true);
        TalentSprintApi apiService = ApiClient.getCacheClient(false).create(TalentSprintApi.class);
        Call<GetExamsObject> getExams = apiService.getExams();
        getExams.enqueue(new Callback<GetExamsObject>() {
            @Override
            public void onResponse(Call<GetExamsObject> call, Response<GetExamsObject> response) {
                if (response.isSuccessful()) {
                    getMyExams(response);
                } else {
                    dashboardInterface.showProgress(false);
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<GetExamsObject> call, Throwable t) {
                if (dashboardInterface != null)
                    dashboardInterface.showProgress(false);
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }
        });
    }

    private void deleteExams(final ExamObject examObject) {
        dashboardInterface.showProgress(true);
        TalentSprintApi apiService = ApiClient.getCacheClient(false).create(TalentSprintApi.class);
        Call<JsonObject> getExams = apiService.deleteExams(examObject.getId());
        getExams.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (dashboardInterface != null)
                    dashboardInterface.showProgress(false);
                if (response.isSuccessful()) {
                    addExamsAdapter.removeExamFromList(examObject);
                    TalentSprintApp.refreshDashBorad = true;
                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (dashboardInterface != null)
                    dashboardInterface.showProgress(false);
                if (getActivity() != null)
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMyExams(final Response<GetExamsObject> allExams) {
        dashboardInterface.showProgress(true);
        TalentSprintApi apiService = ApiClient.getCacheClient(false).create(TalentSprintApi.class);
        Call<GetExamsObject> getExams = apiService.getMyExams();
        getExams.enqueue(new Callback<GetExamsObject>() {
            @Override
            public void onResponse(Call<GetExamsObject> call, Response<GetExamsObject> response) {
                dashboardInterface.showProgress(false);
                if (response.isSuccessful()) {
                    ArrayList<ExamObject> previousExams = response.body().getExams();
                    if (previousExams != null) {
                        for (int i = 0; (i < previousExams.size() && i < 4); i++) {
                            previousExams.get(i).setPreviouslyAdded(true);
                        }
                        addedExams.addAll(previousExams);
                        if (addedExams != null && addedExams.size() > 0)
                            setRecyclerVisible();

                    }
                    setValues(allExams);
                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<GetExamsObject> call, Throwable t) {
                if (dashboardInterface != null)
                    dashboardInterface.showProgress(false);
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }
        });
    }

    private void setExams() {
        dashboardInterface.showProgress(true);
        TalentSprintApi apiService = dashboardInterface.getApiService();
        ArrayList<String> examsToAdd = new ArrayList<String>();
        for (int i = 0; i < addedExams.size(); i++) {
            ExamObject examObject = addedExams.get(i);
            if (examObject.getId() != null && !examObject.isPreviouslyAdded()) {
                examsToAdd.add(examObject.getName() + "," + examObject.getDate() + "," + examObject.getId());
            }
        }
        if (examsToAdd.size() > 0) {
            Call<GetExamsObject> getExams = apiService.setExams(examsToAdd);
            getExams.enqueue(new Callback<GetExamsObject>() {
                @Override
                public void onResponse(Call<GetExamsObject> call, Response<GetExamsObject> response) {
                    dashboardInterface.showProgress(false);
                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), AppConstants.EXAMS_ADDED, Toast.LENGTH_SHORT).show();
                        TalentSprintApp.refreshDashBorad = false;
                        dashboardInterface.examAdded();
                    }
                }

                @Override
                public void onFailure(Call<GetExamsObject> call, Throwable t) {
                    if (dashboardInterface != null)
                        dashboardInterface.showProgress(false);
                    if (getActivity() != null)
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            dashboardInterface.showProgress(false);
            Toast.makeText(getActivity(), AppConstants.NO_EXAMS_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    private void setValues(Response<GetExamsObject> response) {
        ArrayList<ExamObject> exams = response.body().getExams();
        ExamObject selectExam = new ExamObject();
        selectExam.setDate("");
        selectExam.setName("Select Exam");
        exams.add(0, selectExam);
        for (int i = 0; i < exams.size(); i++) {
            examsMap.put(exams.get(i).getId(), i);
        }
        addExamsAdapter = new AddExamsAdapter(exams);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        examsRecycler.setLayoutManager(mLayoutManager);
        examsRecycler.setAdapter(addExamsAdapter);
    }

    private void findViews(View fragmentView) {
        add = fragmentView.findViewById(R.id.add);
        noExams = fragmentView.findViewById(R.id.noExams);
        setExam = fragmentView.findViewById(R.id.setExam);
        examsRecycler = fragmentView.findViewById(R.id.examsRecycler);
        save = fragmentView.findViewById(R.id.save);
        cancel = fragmentView.findViewById(R.id.cancel);
        examsRecycler.setVisibility(View.GONE);
        save.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        setExam.setOnClickListener(this);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == setExam) {
            setRecyclerVisible();
            if (addedExams == null || addedExams.size() == 0) {
                addedExams = new ArrayList<ExamObject>();
                AddNewExam();
            }
        } else if (v == add) {
            if (save.getVisibility() == View.GONE) {
                setRecyclerVisible();
                if (addedExams.size() == 0) {
                    AddNewExam();
                    addExamsAdapter.notifyDataSetChanged();
                }
            } else if (addedExams.size() < 4) {
                AddNewExam();
                if (addExamsAdapter != null)
                    addExamsAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), "Only 4 exams can be added", Toast.LENGTH_SHORT).show();
            }
        } else if (v == cancel) {
            getActivity().onBackPressed();
        } else if (v == save) {
            setExams();
        }
    }

    private void AddNewExam() {
        ExamObject emptyObject = new ExamObject();
        addedExams.add(emptyObject);
    }

    private void setRecyclerVisible() {
        examsRecycler.setVisibility(View.VISIBLE);
        save.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.VISIBLE);
        noExams.setVisibility(View.GONE);
    }

    private void setRecyclerInvisible() {
        examsRecycler.setVisibility(View.GONE);
        save.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        noExams.setVisibility(View.VISIBLE);
    }

    @Override
    public void moveNext() {
    }

    @Override
    public void movePrevious() {
    }

    @Override
    public void selectedDate(long date) {
        addedExams.get(showCalenderPosition).setDate(AppUtils.getDateInYYYMMDD(date));
        addExamsAdapter.notifyItemChanged(showCalenderPosition);
    }

    public class AddExamsAdapter extends RecyclerView.Adapter<AddExamsAdapter.MyViewHolder> {

        private List<ExamObject> examsList;
        private ExamsAdapter spinnerAdapter;

        public AddExamsAdapter(ArrayList<ExamObject> examsList) {
            this.examsList = examsList;
            if (addedExams == null || addedExams.size() == 0) {
                addedExams = new ArrayList<ExamObject>();
                AddNewExam();
            }
            spinnerAdapter = new ExamsAdapter(getActivity(), R.layout.drop_down_item_select_exam,
                    examsList);
            spinnerAdapter.setDropDownViewResource(R.layout.drop_down_item_select_exam);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_add_exam, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.examCount.setText(Integer.toString(position + 1));
            final ExamObject addedExamObject = addedExams.get(position);
            holder.examDate.setText(addedExamObject.getDate());
            if (!addedExamObject.isPreviouslyAdded()) {
                holder.examNameSpinner.setVisibility(View.VISIBLE);
                holder.examName.setVisibility(View.INVISIBLE);
                holder.examNameSpinner.setAdapter(spinnerAdapter);
                holder.examNameSpinner.setOnItemSelectedListener(null);
                holder.examNameSpinner.setSelection(examsMap.get(addedExamObject.getId()));
                holder.examNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int spinnerPosition, long l) {
                        ExamObject selectedExam = examsList.get(spinnerPosition);
                        if (addedExamObject.getId() != selectedExam.getId()) {
                            holder.examDate.setText(selectedExam.getDate());
                            addedExamObject.setId(selectedExam.getId());
                            addedExamObject.setName(selectedExam.getName());
                            addedExamObject.setDate(selectedExam.getDate());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            } else {
                holder.examNameSpinner.setVisibility(View.INVISIBLE);
                holder.examName.setVisibility(View.VISIBLE);
                holder.examName.setText(addedExamObject.getName());
            }
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (addedExamObject.isPreviouslyAdded()) {
                        if (!addedExamObject.isNextExam())
                            deleteExams(addedExamObject);
                        else
                            showDialogue(addedExamObject);
                    } else {
                        removeExamFromList(addedExamObject);
                    }
                }
            });
            holder.examDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCalender(addedExamObject, holder, position);
                }
            });
            holder.calender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCalender(addedExamObject, holder, position);
                }
            });
        }

        private void showDialogue(final ExamObject addedExamObject) {
            final Dialog finishDialogue;
            finishDialogue = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
            finishDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(150, 0, 0, 0)));
            finishDialogue.setContentView(R.layout.dialogue_exam_delete);
            TextView yes = finishDialogue.findViewById(R.id.yes);
            TextView cancel = finishDialogue.findViewById(R.id.cancel);
            View mainView = finishDialogue.findViewById(R.id.mainView);
            finishDialogue.show();
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finishDialogue.dismiss();
                    deleteExams(addedExamObject);
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finishDialogue.dismiss();
                }
            });
            mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finishDialogue.dismiss();
                }
            });
        }

        private void removeExamFromList(ExamObject addedExamObject) {
            addedExams.remove(addedExamObject);
            notifyDataSetChanged();
            if (addedExams.size() == 0) {
                setRecyclerInvisible();
            }
        }

        private void showCalender(ExamObject addedExamObject, final MyViewHolder holder, int position) {
            if (!addedExamObject.isPreviouslyAdded()) {
                holder.examDate.setClickable(false);
                dashboardInterface.showProgress(true);
                Bundle bundle = new Bundle();
                bundle.putFloat(AppConstants.X_VALUE, add.getX());
                try {
                    bundle.putLong(AppConstants.DATE_LONG, AppUtils.getLongFromYYYMMDD(addedExamObject.getDate()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int[] postions = new int[2];
                add.getLocationInWindow(postions);
                bundle.putFloat(AppConstants.Y_VALUE, postions[1]);
                try {
                    bundle.putLong(AppConstants.DATE_FUTURE, AppUtils.getLongFromYYYMMDD(addedExamObject.getExamDate()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CalenderDialogue dialogue = new CalenderDialogue();
                dialogue.setArguments(bundle);
                dialogue.show(getChildFragmentManager(), null);
                showCalenderPosition = position;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.examDate.setClickable(true);
                        dashboardInterface.showProgress(false);
                    }
                }, 3000);

            }
        }

        @Override
        public int getItemCount() {
            return addedExams.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView examCount;
            public TextView examDate;
            public TextView examName;
            public View divider;
            public ImageView calender;
            public ImageView delete;
            public Spinner examNameSpinner;

            public MyViewHolder(View view) {
                super(view);
                examCount = view.findViewById(R.id.examCount);
                examDate = view.findViewById(R.id.examDate);
                examName = view.findViewById(R.id.examName);
                divider = view.findViewById(R.id.divider);
                calender = view.findViewById(R.id.calender);
                delete = view.findViewById(R.id.delete);
                examNameSpinner = view.findViewById(R.id.examNameSpinner);

            }
        }
    }

    public class ExamsAdapter extends ArrayAdapter<ExamObject> {

        public ExamsAdapter(Context context, int textViewResourceId, ArrayList<ExamObject> items) {
            super(context, textViewResourceId, items);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(this.getContext())
                        .inflate(R.layout.drop_down_item_select_exam, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.examName = convertView.findViewById(R.id.examName);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ExamObject item = getItem(position);
            if (item != null) {
                viewHolder.examName.setText(item.getName());
            }
            return convertView;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(this.getContext())
                        .inflate(R.layout.drop_down_item_select_exam_list_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.examName = convertView.findViewById(R.id.examName);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ExamObject item = getItem(position);
            if (item != null) {
                viewHolder.examName.setText(item.getName());
            }
            return convertView;
        }

        public class ViewHolder {
            private TextView examName;
        }
    }
}
