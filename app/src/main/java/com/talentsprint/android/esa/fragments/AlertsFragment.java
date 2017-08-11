package com.talentsprint.android.esa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.talentsprint.apps.talentsprint.R;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.talentsprint.android.esa.dialogues.CalenderDialogue;
import com.talentsprint.android.esa.interfaces.CalenderInterface;
import com.talentsprint.android.esa.interfaces.DashboardActivityInterface;
import com.talentsprint.android.esa.models.NotificationsObject;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.AppUtils;
import com.talentsprint.android.esa.views.LinearLayoutManagerWithSmoothScroller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlertsFragment extends Fragment implements View.OnClickListener, CalenderInterface {

    private DashboardActivityInterface dashboardInterface;
    private ImageView calender;
    private TextView noAlerts;
    private RecyclerView alertsRecycler;
    private String currentDate;
    private HashMap<String, Integer> dateIndexingMap = new HashMap<String, Integer>();

    public AlertsFragment() {
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
        dashboardInterface.setCurveVisibility(true);
        View fragmentView = inflater.inflate(R.layout.fragment_alerts, container, false);
        findViews(fragmentView);
        currentDate = AppUtils.getCurrentDateString();
        getAlerts();
        return fragmentView;
    }

    private void getAlerts() {
        List<NotificationsObject> notificationsList = new ArrayList<NotificationsObject>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NotificationsObject> notificationsObjects = realm.where(NotificationsObject.class).equalTo("type",
                AppConstants.ALERT).greaterThan
                ("expiryDateLong", System.currentTimeMillis()).findAll();
        notificationsList = realm.copyFromRealm(notificationsObjects);
        if (notificationsList == null || notificationsList.size() == 0)
            noAlerts.setVisibility(View.VISIBLE);
        else noAlerts.setVisibility(View.GONE);
        HashMap<String, ArrayList<NotificationsObject>> notificationsMap = new HashMap<String, ArrayList<NotificationsObject>>();
        int dateRowNumber = 0;
        for (int i = 0; i < notificationsList.size(); i++) {
            NotificationsObject notificationsObject = notificationsList.get(i);
            String addedDate = notificationsObject.getAddedDate();
            if (notificationsMap.containsKey(addedDate)) {
                notificationsMap.get(addedDate).add(notificationsObject);
            } else {
                dateIndexingMap.put(addedDate, dateRowNumber);
                ArrayList<NotificationsObject> notificationsArrayList = new ArrayList<NotificationsObject>();
                notificationsArrayList.add(notificationsObject);
                notificationsMap.put(addedDate, notificationsArrayList);
                dateRowNumber++;
            }
            dateRowNumber++;
        }
        List<String> datesList = new ArrayList<String>(notificationsMap.keySet());
        Collections.reverse(datesList);
        AlertsAdapter stratergyAdapter = new AlertsAdapter(notificationsMap, (ArrayList<String>) datesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManagerWithSmoothScroller(getActivity());
        alertsRecycler.setLayoutManager(mLayoutManager);
        alertsRecycler.setAdapter(stratergyAdapter);
    }

    private void findViews(View fragmentView) {
        calender = fragmentView.findViewById(R.id.calender);
        alertsRecycler = fragmentView.findViewById(R.id.alertsRecycler);
        noAlerts = fragmentView.findViewById(R.id.noAlerts);
        calender.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == calender) {
            calender.setClickable(false);
            dashboardInterface.showProgress(true);
            Bundle bundle = new Bundle();
            bundle.putFloat(AppConstants.X_VALUE, calender.getX());
            int[] postions = new int[2];
            calender.getLocationInWindow(postions);
            bundle.putFloat(AppConstants.Y_VALUE, postions[1]);
            CalenderDialogue dialogue = new CalenderDialogue();
            dialogue.setArguments(bundle);
            dialogue.show(getChildFragmentManager(), null);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    calender.setClickable(true);
                    dashboardInterface.showProgress(false);
                }
            }, 3000);
        }
    }

    @Override
    public void moveNext() {
    }

    @Override
    public void movePrevious() {
    }

    @Override
    public void selectedDate(long date) {
        String selectedDate = AppUtils.getDateInMMMDDYYYY(date);
        if (dateIndexingMap.containsKey(selectedDate)) {
            alertsRecycler.smoothScrollToPosition(dateIndexingMap.get(selectedDate));
        } else {
            Toast.makeText(getActivity(), "No alerts found for the selected date", Toast.LENGTH_SHORT).show();
        }
    }

    public class AlertsAdapter extends SectionedRecyclerViewAdapter<AlertsAdapter.MyViewHolder> {

        private HashMap<String, ArrayList<NotificationsObject>> notificationsHashMap;
        private ArrayList<String> datesList;

        public AlertsAdapter(HashMap<String, ArrayList<NotificationsObject>> notificationsHashMap, ArrayList<String> datesList) {
            this.notificationsHashMap = notificationsHashMap;
            this.datesList = datesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int layoutRes;
            switch (viewType) {
                case VIEW_TYPE_HEADER:
                    layoutRes = R.layout.list_item_alert_header;
                    break;
                default:
                    layoutRes = R.layout.list_item_alert;
                    break;
            }
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(layoutRes, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public int getSectionCount() {
            return datesList.size();
        }

        @Override
        public int getItemCount(int section) {
            return notificationsHashMap.get(datesList.get(section)).size();
        }

        @Override
        public void onBindHeaderViewHolder(MyViewHolder holder, int section, boolean expanded) {
            holder.dateText.setText(datesList.get(section));
        }

        @Override
        public void onBindFooterViewHolder(MyViewHolder holder, int section) {
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int section, int relativePosition, int absolutePosition) {
            final NotificationsObject notificationsObject = notificationsHashMap.get(datesList.get(section)).get
                    (relativePosition);
            holder.alertText.setText(notificationsObject.getTitle());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppUtils.navigateFromNotifications(getActivity(), notificationsObject, false);
                }
            });
        }

        public class MyViewHolder extends SectionedViewHolder {

            public TextView alertText;
            public TextView dateText;
            public View view;

            public MyViewHolder(View view) {
                super(view);
                alertText = view.findViewById(R.id.alertText);
                dateText = view.findViewById(R.id.dateText);
                this.view = view;
            }

        }
    }
}
