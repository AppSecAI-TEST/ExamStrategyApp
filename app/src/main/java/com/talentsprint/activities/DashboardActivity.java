package com.talentsprint.activities;

import android.animation.Animator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.talentsprint.R;
import com.talentsprint.fragments.DashboardFragment;
import com.talentsprint.fragments.ProfileFragment;
import com.talentsprint.fragments.StratergyFragment;
import com.talentsprint.interfaces.DashboardActivityInterface;
import com.talentsprint.utils.AppConstants;

public class DashboardActivity extends FragmentActivity implements DashboardActivityInterface, View.OnClickListener {

    private ImageView header_curve;
    private ImageView menu;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dashboard);
        findViews();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new DashboardFragment(), AppConstants.DASHBOARD).commit();
    }

    private void findViews() {
        menu = findViewById(R.id.menu);
        header_curve = findViewById(R.id.header_curve);
        progressBar = findViewById(R.id.progressBar);
        header_curve.setVisibility(View.GONE);
        menu.setOnClickListener(this);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setCurveVisibility(boolean isShow) {
        if (isShow) {
            header_curve.setVisibility(View.VISIBLE);
        } else {
            header_curve.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProgress(boolean isShow) {
        if (isShow) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment != null) {
            String tag = currentFragment.getTag();
            if (tag != null && tag.equalsIgnoreCase(AppConstants.DASHBOARD)) {
                setCurveVisibility(false);
            } else {
                setCurveVisibility(true);
            }
        }
    }

    @Override
    public void onClick(final View view) {
        if (view == menu) {
            showMenu();
        }
    }

    private void showMenu() {
        final Dialog menuItem = new Dialog(DashboardActivity.this, android.R.style.Theme_Black_NoTitleBar);
        menuItem.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(150, 0, 0, 0)));
        menuItem.setContentView(R.layout.menu_dialogue);
        menuItem.setCanceledOnTouchOutside(true);
        View content = menuItem.findViewById(R.id.content);
        View footer = menuItem.findViewById(R.id.footer);
        View examStratergy = menuItem.findViewById(R.id.examStratergy);
        content.setOnClickListener(this);
        footer.setOnClickListener(this);
        final View mainView = menuItem.findViewById(R.id.mainView);
        final TextView studyMaterial = menuItem.findViewById(R.id.studyMaterial);
        final TextView currentAffairs = menuItem.findViewById(R.id.currentAffairs);
        final TextView myProfile = menuItem.findViewById(R.id.myProfile);
        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuItem.dismiss();
            }
        });
        menuItem.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuItem.dismiss();
            }
        });
        studyMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navigate = new Intent(DashboardActivity.this, StudyMaterialActivity.class);
                startActivity(navigate);
                menuItem.dismiss();
            }
        });
        currentAffairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navigate = new Intent(DashboardActivity.this, CurrentAffairsActivity.class);
                startActivity(navigate);
                menuItem.dismiss();
            }
        });
        examStratergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager supportFragmentManager = getSupportFragmentManager();
                supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, new StratergyFragment(), AppConstants.STRATERGY).addToBackStack(null)
                        .commit();
                menuItem.dismiss();
            }
        });
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager supportFragmentManager = getSupportFragmentManager();
                supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, new ProfileFragment(), AppConstants.PROFILE).addToBackStack(null).commit();
                menuItem.dismiss();
            }
        });
        menuItem.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    float startRadius = 20;
                    float endRadius = mainView.getHeight();
                    Animator animator = null;
                    animator = ViewAnimationUtils.createCircularReveal(mainView, 0, 0, startRadius, endRadius * 2);
                    animator.setDuration(1000);
                    animator.start();
                }
            }
        });
        menuItem.show();
    }
}
