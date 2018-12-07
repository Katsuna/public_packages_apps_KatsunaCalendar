package com.katsuna.calendar.details;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.katsuna.calendar.R;
import com.katsuna.calendar.days.DaysPresenter;
import com.katsuna.calendar.util.Injection;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.ui.KatsunaActivity;
import com.katsuna.commons.utils.IUserProfileProvider;

public class DayDetailsActivity extends KatsunaActivity implements DayDetailsContract.View,
        IUserProfileProvider {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_day_details);
//        adjustProfiles();

        init();

        // Create the presenter
        new DayDetailsPresenter();
    }

    public void init () {

    }

    @Override
    public void setPresenter(DayDetailsContract.Presenter presenter) {

    }

    @Override
    protected void showPopup(boolean flag) {

    }

    @Override
    public UserProfile getProfile() {
        return null;
    }
}
