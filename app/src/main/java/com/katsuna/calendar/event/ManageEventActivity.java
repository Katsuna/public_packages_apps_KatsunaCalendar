package com.katsuna.calendar.event;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.katsuna.calendar.R;
import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventType;
import com.katsuna.calendar.utils.Injection;
import com.katsuna.calendar.validators.ValidationResult;
import com.katsuna.commons.entities.ColorProfileKeyV2;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.ui.KatsunaActivity;
import com.katsuna.commons.utils.ColorAdjusterV2;
import com.katsuna.commons.utils.ColorCalcV2;
import com.katsuna.commons.utils.KeyboardUtils;

import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Iterator;
import java.util.List;

import static android.media.RingtoneManager.TYPE_ALARM;

public class ManageEventActivity extends KatsunaActivity implements ManageEventContract.View {

    public static final String EXTRA_EVENT_ID = "EVENT_ID";
    public static final String EXTRA_EVENT_TYPE = "EVENT_TYPE";
    public static final int RINGTONE_REQUEST_CODE = 1;

    private ManageEventContract.Presenter mPresenter;
    private FloatingActionButton mPreviousStepFab;
    private FloatingActionButton mNextStepFab;
    private View mEventDaysControl;
    private RadioGroup mEventTypeRadioGroup;
    private EditText mDescription;
    private EditText mHour;
    private EditText mMinute;
    private EditText mMonth;
    private EditText mDay;
    private EditText mYear;
    private View mEventTimeControl;
    private RadioButton mReminderTypeRadioGroup;
    private RadioButton mEventTypeRadioButton;
    private View mEventTypeContainer;
    private View mEventTypeHandler;
    private View mEventTimeHandler;
    private View mEventTimeContainer;
    private View mEventDaysContainer;
    private View mEventDaysHandler;
    private View mEventOptionsHandler;
    private View mEventOptionsContainer;
    private View mEventOptionsControl;
    private TextView mEventOptionsTitle;
    private ToggleButton mVibrateOption;
    private boolean mVibrate;

    private int mPrimaryColor2;
    private int mSecondaryColor2;
    private TextView mEventTimeTitle;
    private int mBlack34Color;
    private int mBlack58Color;
    private int mWhiteColor;
    private TextView mEventDaysTitle;
    private TextView mEventTypeTitle;
    private ImageView mAddHourButton;
    private ImageView mSubtractHourButton;
    private ImageView mAddMinuteButton;
    private ImageView mSubtractMinuteButton;
    private Day mEventDay;
    private TextView mEventDayTitle;
    private TextView mRingtoneOption;
    private Uri mRingtoneUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_event);
        mEventDay = (Day) getIntent().getSerializableExtra("Day");
        init();

        long eventId = getIntent().getLongExtra(EXTRA_EVENT_ID, 0);
        EventType eventType = (EventType) getIntent().getSerializableExtra(EXTRA_EVENT_TYPE);

        // Create the presenter
        new ManageEventPresenter(eventId, eventType,
                Injection.provideEventsDataSource(getApplicationContext()), this,
                Injection.provideEventValidator(),
                Injection.provideEventScheduler(getApplicationContext()));
    }

    private void init() {
        mEventDayTitle = findViewById(R.id.current_event_day);
        if(mEventDay != null) {
            mEventDayTitle.setText(mEventDay.getDayName() + ", " + mEventDay.getMonth());
        }
        else
        {
            mEventDayTitle.setVisibility(View.GONE);
        }

        mRingtoneOption = findViewById(R.id.event_ringtone_option);
        mRingtoneOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickRingtone();
            }
        });


        mVibrateOption = findViewById(R.id.event_vibrate_option);
        mVibrateOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVibrate = !mVibrate;
                adjustVibrateOption(mVibrate);
            }
        });

        mEventTypeContainer = findViewById(R.id.event_type_container);
        mEventTypeHandler = findViewById(R.id.event_type_handler);
        mEventTypeRadioGroup = findViewById(R.id.event_type_radio_group);

        mEventTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.event_type_radio_button:
                        mPresenter.eventTypeSelected(EventType.ALARM);
                        break;
                    case R.id.reminder_type_radio_button:
                        mPresenter.eventTypeSelected(EventType.REMINDER);
                        break;
                }
            }
        });
        mEventTypeRadioButton = findViewById(R.id.event_type_radio_button);
        mReminderTypeRadioGroup = findViewById(R.id.reminder_type_radio_button);
        mDescription = findViewById(R.id.event_description);

        mEventTimeHandler = findViewById(R.id.event_time_handler);
        mEventTimeContainer = findViewById(R.id.event_time_container);
        mEventTimeControl = findViewById(R.id.event_time_control);
        mEventTypeTitle = findViewById(R.id.event_type_text);
        mEventTimeTitle = findViewById(R.id.event_time_text);
        mEventDaysTitle = findViewById(R.id.event_days_text);
        mEventOptionsTitle = findViewById(R.id.event_options_text);


        mEventOptionsHandler = findViewById(R.id.event_options_handler);
        mEventOptionsContainer = findViewById(R.id.event_options_container);
        mEventOptionsControl = findViewById(R.id.event_options_group);

        mHour = findViewById(R.id.hour);
        mHour.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mHour.setTextColor(mPrimaryColor2);
                } else {
                    mHour.setTextColor(mBlack58Color);
                }
            }
        });
        mHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String output = null;
                try {
                    int input = Integer.parseInt(s.toString());
                    if (input > 23) {
                        output = "23";
                    } else if (input < 0) {
                        output = "00";
                    }
                } catch (NumberFormatException ex) {
                    output = "00";
                }

                // limits overriden or NumberFormatException case
                if (output != null) {
                    mHour.setText(output);
                }
            }
        });

        mMinute = findViewById(R.id.minute);
        mMinute.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mMinute.setTextColor(mPrimaryColor2);
                } else {
                    mMinute.setTextColor(mBlack58Color);
                }
            }
        });
        mMinute.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String output = null;
                try {
                    int input = Integer.parseInt(s.toString());
                    if (input > 59) {
                        output = "59";
                    } else if (input < 0) {
                        output = "00";
                    }
                } catch (NumberFormatException ex) {
                    output = "00";
                }

                // limits overriden or NumberFormatException case
                if (output != null) {
                    mMinute.setText(output);
                }
            }
        });


        mAddHourButton = findViewById(R.id.add_hour_button);
        mAddHourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addHours(mHour.getText().toString(), 1);
            }
        });
        mSubtractHourButton = findViewById(R.id.subtract_hour_button);
        mSubtractHourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addHours(mHour.getText().toString(), -1);
            }
        });
        mAddMinuteButton = findViewById(R.id.add_minute_button);
        mAddMinuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addMinutes(mMinute.getText().toString(), 1);
            }
        });
        mSubtractMinuteButton = findViewById(R.id.subtract_minute_button);
        mSubtractMinuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addMinutes(mMinute.getText().toString(), -1);
            }
        });

        mEventDaysHandler = findViewById(R.id.event_days_handler);
        mEventDaysContainer = findViewById(R.id.event_days_container);


        mPreviousStepFab = findViewById(R.id.prev_step_fab);
        mPreviousStepFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPreviousStep();
            }
        });

        mNextStepFab = findViewById(R.id.next_step_fab);
        mNextStepFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextStep();
            }
        });

        initToolbar(R.drawable.common_ic_close_black54_24dp);

        // static colors init
        mBlack34Color = ContextCompat.getColor(this, R.color.common_black34);
        mBlack58Color = ContextCompat.getColor(this, R.color.common_black58);
        mWhiteColor = ContextCompat.getColor(this, R.color.common_white);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
        adjustProfiles();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RINGTONE_REQUEST_CODE) {
                mRingtoneUri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                setRingtoneTitle(mRingtoneUri);
            }
        }
    }

    private void setRingtone(Event event) {
        mRingtoneUri = Uri.parse(event.getRingtone());
        setRingtoneTitle(mRingtoneUri);
    }

    private void setRingtoneTitle(Uri uri) {
        Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
        String title = getString(R.string.ringtone, ringtone.getTitle(this));
        mRingtoneOption.setText(title);
    }

    private void pickRingtone() {
        //List<KatsunaRingtone> ringtones = RingtoneUtils.getAllRingtones(this);

        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, TYPE_ALARM);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, mRingtoneUri);
        startActivityForResult(intent, RINGTONE_REQUEST_CODE);
    }

    private void adjustVibrateOption(boolean enabled) {
        mVibrateOption.setChecked(enabled);
    }

    private void adjustProfiles() {
        UserProfile userProfile = mUserProfileContainer.getActiveUserProfile();

        mPrimaryColor2 = ColorCalcV2.getColor(this, ColorProfileKeyV2.PRIMARY_COLOR_2,
                userProfile.colorProfile);
        mSecondaryColor2 = ColorCalcV2.getColor(this, ColorProfileKeyV2.SECONDARY_COLOR_2,
                userProfile.colorProfile);

        adjustTimeControls();
        adjustSteps();
    }

    private void adjustTimeControls() {
        GradientDrawable gD = new GradientDrawable();
        gD.setColor(mPrimaryColor2);
        gD.setShape(GradientDrawable.OVAL);
        mAddHourButton.setBackground(gD);
        mAddMinuteButton.setBackground(gD);
        mSubtractHourButton.setBackground(gD);
        mSubtractMinuteButton.setBackground(gD);


        final GradientDrawable shape =  new GradientDrawable();
        float radius = getResources().getDimension(R.dimen.time_controls_radius);
        shape.setCornerRadius(radius);
        shape.setColor(mSecondaryColor2);

        mHour.post(new Runnable() {
            @Override
            public void run() {

                mHour.setBackground(shape);
            }
        });

        mMinute.post(new Runnable() {
            @Override
            public void run() {
                mMinute.setBackground(shape);
            }
        });
    }

    private void adjustSteps() {
        adjustTypeStep();
        adjustTimeStep();
        adjustDaysStep();
        adjustOptionsStep();
        adjustFloatingButtons();
    }

    private void adjustTypeStep() {
        mEventTypeHandler.setBackgroundColor(mPrimaryColor2);
        int colorToApply;
        if (mPresenter.getCurrentStep() == ManageEventStep.TYPE) {
            colorToApply = mPrimaryColor2;
        } else {
            colorToApply = mBlack58Color;
        }
        mEventTypeTitle.setTextColor(colorToApply);
        mDescription.setBackgroundTintList(ColorStateList.valueOf(colorToApply));
        ColorAdjusterV2.setTextViewDrawableColor(mEventTypeTitle, colorToApply);
        ColorAdjusterV2.setTextViewDrawableColor(mEventTypeRadioButton, colorToApply);
        ColorAdjusterV2.setTextViewDrawableColor(mReminderTypeRadioGroup, colorToApply);
    }

    private void adjustTimeStep() {
        switch (getTimeStepStatus()) {
            case NOT_SET:
                mEventTimeHandler.setBackgroundColor(mSecondaryColor2);
                mEventTimeTitle.setTextColor(mBlack34Color);
                ColorAdjusterV2.setTextViewDrawableColor(mEventTimeTitle, mBlack34Color);
                break;
            case ACTIVE:
                mEventTimeHandler.setBackgroundColor(mPrimaryColor2);
                mEventTimeTitle.setTextColor(mPrimaryColor2);
                ColorAdjusterV2.setTextViewDrawableColor(mEventTimeTitle, mPrimaryColor2);
                break;
            case SET:
                mEventTimeHandler.setBackgroundColor(mPrimaryColor2);
                mEventTimeTitle.setTextColor(mBlack58Color);
                ColorAdjusterV2.setTextViewDrawableColor(mEventTimeTitle, mBlack58Color);
                break;
        }
    }

    private void adjustDaysStep() {
        int toggleColor = 0;
        switch (getDaysStepStatus()) {
            case NOT_SET:
                mEventDaysHandler.setBackgroundColor(mSecondaryColor2);
                mEventDaysTitle.setTextColor(mBlack34Color);
                ColorAdjusterV2.setTextViewDrawableColor(mEventDaysTitle, mBlack34Color);
                toggleColor = mBlack34Color;
                break;
            case ACTIVE:
                mEventDaysHandler.setBackgroundColor(mPrimaryColor2);
                mEventDaysTitle.setTextColor(mPrimaryColor2);
                ColorAdjusterV2.setTextViewDrawableColor(mEventDaysTitle, mPrimaryColor2);
                toggleColor = mPrimaryColor2;
                break;
            case SET:
                mEventDaysHandler.setBackgroundColor(mBlack58Color);
                mEventDaysTitle.setTextColor(mBlack58Color);
                ColorAdjusterV2.setTextViewDrawableColor(mEventDaysTitle, mBlack58Color);
                toggleColor = mBlack58Color;
                break;
        }


    }

    private void adjustFloatingButtons() {
        if (mPresenter.getCurrentStep() == ManageEventStep.DAYS) {
            mNextStepFab.setBackgroundTintList(ColorStateList.valueOf(mPrimaryColor2));
            mNextStepFab.setImageTintList(ColorStateList.valueOf(mWhiteColor));
        } else {
            mNextStepFab.setBackgroundTintList(ColorStateList.valueOf(mWhiteColor));
            mNextStepFab.setImageTintList(ColorStateList.valueOf(mPrimaryColor2));
        }
        mPreviousStepFab.setImageTintList(ColorStateList.valueOf(mPrimaryColor2));
    }

    private StepStatus getTimeStepStatus() {
        ManageEventStep manageEventStep = mPresenter.getCurrentStep();
        System.out.println("Im here with event step :"+ manageEventStep);
        switch (manageEventStep) {
            case TYPE:
                return StepStatus.NOT_SET;
            case TIME:
                return StepStatus.ACTIVE;
            case DAYS:
                return StepStatus.SET;
            case OPTIONS:
                return StepStatus.SET;
            default:
                throw new RuntimeException("manageEventStep not found");
        }
    }

    private StepStatus getDaysStepStatus() {
        ManageEventStep manageEventStep = mPresenter.getCurrentStep();
        switch (manageEventStep) {
            case TYPE:
                return StepStatus.NOT_SET;
            case TIME:
                return StepStatus.NOT_SET;
            case DAYS:
                return StepStatus.ACTIVE;
            case OPTIONS:
                return StepStatus.SET;
            default:
                throw new RuntimeException("manageEventStep not found");
        }
    }

    private StepStatus getOptionsStepStatus() {
        ManageEventStep manageEventStep = mPresenter.getCurrentStep();
        switch (manageEventStep) {
            case TYPE:
                return StepStatus.NOT_SET;
            case TIME:
                return StepStatus.NOT_SET;
            case DAYS:
                return StepStatus.NOT_SET;
            case OPTIONS:
                return StepStatus.ACTIVE;
            default:
                throw new RuntimeException("manageAlarmStep not found");
        }
    }

    @Override
    protected void showPopup(boolean flag) {
        // no op
    }

    @Override
    public void showEmptyEventError() {

    }

    @Override
    public void showCalendarOnReturn(Event event) {
        Intent i = new Intent();
        i.putExtra("event", event);
        setResult(RESULT_OK, i);
        finish();

    }

    @Override
    public void showEventList() {

    }


    @Override
    public void setTime(String hour, String minute) {
        mHour.setText(hour);
        mMinute.setText(minute);
    }

    @Override
    public void setHour(String hour) {
        mHour.setText(hour);
    }

    @Override
    public void setMinute(String minute) {
        mMinute.setText(minute);
    }

    @Override
    public void loadEvent(Event event) {
        if (event.getEventType() == EventType.ALARM) {
            mEventTypeRadioButton.setChecked(true);
        } else {
            mReminderTypeRadioGroup.setChecked(true);
        }
        mDescription.setText(event.getDescription());
        LocalTime eventTime = LocalTime.of(event.getHour(), event.getMinute());
        mHour.setText(eventTime.format(DateTimeFormatter.ofPattern("HH")));
        mMinute.setText(eventTime.format(DateTimeFormatter.ofPattern("mm")));
        mYear.setText(eventTime.format(DateTimeFormatter.ofPattern("yyyy")));
        setRingtone(event);
        mVibrate = event.isVibrate();
        adjustVibrateOption(mVibrate);
    }

    @Override
    public void showValidationResults(List<ValidationResult> results) {
        StringBuilder sb = new StringBuilder();
        for (Iterator<ValidationResult> i = results.iterator(); i.hasNext(); ) {
            ValidationResult result = i.next();
            sb.append(getString(result.messageResId));
            if (i.hasNext()) {
                sb.append("\n");
            }
        }
        Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNextStepFab(boolean flag) {
        mNextStepFab.setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showPreviousStepFab(boolean flag) {
        mPreviousStepFab.setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showDescriptionControl(boolean flag) {
        if (flag) {
            mEventTypeContainer.setBackgroundColor(ContextCompat.getColor(this,
                    R.color.common_white));

            mDescription.setVisibility(View.VISIBLE);

            int elevation = getResources().getDimensionPixelSize(
                    R.dimen.common_selection_elevation);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)
                    mEventTypeHandler.getLayoutParams();
            layoutParams.bottomMargin = elevation;
            mEventTypeHandler.setElevation(elevation);
        } else {
            mEventTypeContainer.setBackgroundColor(ContextCompat.getColor(this,
                    R.color.common_grey50));

            mDescription.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)
                    mEventTypeHandler.getLayoutParams();
            layoutParams.bottomMargin = 0;
            mEventTypeHandler.setElevation(0);
        }

        mDescription.setEnabled(flag);

        adjustSteps();
    }

    @Override
    public void showDescriptionStep(boolean flag) {
        mEventTypeContainer.setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setEventTimeTitle(int resId) {
        mEventTimeTitle.setText(resId);
    }


    @Override
    public void adjustFabPositions(ManageEventStep step) {
        CoordinatorLayout.LayoutParams nextStepFabParams =
                (CoordinatorLayout.LayoutParams) mNextStepFab.getLayoutParams();
        CoordinatorLayout.LayoutParams previousStepFabParams =
                (CoordinatorLayout.LayoutParams) mPreviousStepFab.getLayoutParams();

        switch (step) {
            case TYPE:
                nextStepFabParams.anchorGravity = Gravity.TOP | Gravity.END;
                break;
            case TIME:
                previousStepFabParams.anchorGravity = Gravity.TOP | Gravity.END;
                nextStepFabParams.setAnchorId(R.id.event_time_container);
                nextStepFabParams.anchorGravity = Gravity.BOTTOM | Gravity.END;
                break;
            case DAYS:
                previousStepFabParams.anchorGravity = Gravity.BOTTOM | Gravity.END;
                nextStepFabParams.setAnchorId(R.id.event_days_container);
                nextStepFabParams.anchorGravity = Gravity.BOTTOM | Gravity.END;
                break;
            case OPTIONS:
                previousStepFabParams.anchorGravity = Gravity.TOP | Gravity.END;
                previousStepFabParams.setAnchorId(R.id.event_options_container);
                nextStepFabParams.setAnchorId(R.id.event_options_container);
                nextStepFabParams.anchorGravity = Gravity.BOTTOM | Gravity.END;
                break;        }
    }

    @Override
    public void showEventDayControl(boolean flag) {

    }

    @Override
    public void showEventTimeControl(boolean flag) {
        if (flag) {
            mEventTimeContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.common_white));

            int elevation = getResources().getDimensionPixelSize(
                    R.dimen.common_selection_elevation);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)
                    mEventTypeHandler.getLayoutParams();
            layoutParams.bottomMargin = elevation;
            mEventTimeHandler.setElevation(elevation);
        } else {
            mEventTimeContainer.setBackgroundColor(ContextCompat.getColor(this,
                    R.color.common_grey50));

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)
                    mEventTypeHandler.getLayoutParams();
            layoutParams.bottomMargin = 0;
            mEventTimeHandler.setElevation(0);

        }

        adjustSteps();

        mEventTimeControl.setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showEventOptionsControl(boolean flag) {
        if (flag) {
            mEventOptionsContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.common_white));

            int elevation = getResources().getDimensionPixelSize(
                    R.dimen.common_selection_elevation);

            mEventOptionsHandler.setElevation(elevation);

        } else {
            mEventOptionsContainer.setBackgroundColor(ContextCompat.getColor(this,
                    R.color.common_grey50));

            mEventOptionsHandler.setElevation(0);
        }

        adjustSteps();

        mEventOptionsControl.setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setDefaultRingtone() {
        mRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this, TYPE_ALARM);
        setRingtoneTitle(mRingtoneUri);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void setDefaultVibrate() {
        mVibrate = true;
        adjustVibrateOption(mVibrate);
    }


    @Override
    public void hideKeyboard() {
        KeyboardUtils.hideKeyboard(this);
    }

    @Override
    public void setPresenter(@NonNull ManageEventContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void onNextStep() {
        ManageEventStep step = mPresenter.getCurrentStep();
        System.out.println("IM ON next step"+step);
        switch (step) {
            case TYPE:
                mPresenter.validateEventTypeInfo(getEventType(), mDescription.getText().toString());
                break;
            case TIME:
                mPresenter.validateEventTime(mHour.getText().toString(),
                        mMinute.getText().toString());
                break;
            case DAYS:
                mPresenter.showStep(ManageEventStep.OPTIONS);
                break;
            case OPTIONS:
                mPresenter.saveEvent(getEventType(), mDescription.getText().toString(),
                        mHour.getText().toString(), mMinute.getText().toString(),
                        mEventDay.getDay().toString(),mEventDay.getMonth().toString(),
                        mEventDay.getYear().toString(), mRingtoneUri.toString(), mVibrate
                );

                break;
        }
    }

    private void onPreviousStep() {
        mPresenter.previousStep();

    }

    private void adjustOptionsStep() {
        int toggleColor = 0;
        switch (getOptionsStepStatus()) {
            case NOT_SET:
                mEventOptionsHandler.setBackgroundColor(mSecondaryColor2);
                mEventOptionsTitle.setTextColor(mBlack34Color);
                ColorAdjusterV2.setTextViewDrawableColor(mEventOptionsTitle, mBlack34Color);
                toggleColor = mBlack34Color;
                break;
            case ACTIVE:
                mEventOptionsHandler.setBackgroundColor(mPrimaryColor2);
                mEventOptionsTitle.setTextColor(mPrimaryColor2);
                ColorAdjusterV2.setTextViewDrawableColor(mEventOptionsTitle, mPrimaryColor2);
                toggleColor = mPrimaryColor2;
                break;
            case SET:
                mEventOptionsHandler.setBackgroundColor(mBlack58Color);
                mEventOptionsTitle.setTextColor(mBlack58Color);
                ColorAdjusterV2.setTextViewDrawableColor(mEventOptionsTitle, mBlack58Color);
                toggleColor = mBlack58Color;
                break;
        }
        ColorAdjusterV2.setTextViewDrawableColor(mVibrateOption, toggleColor);
        if (mVibrateOption.isChecked()) {
            mVibrateOption.setTextColor(mPrimaryColor2);
        } else {
            mVibrateOption.setTextColor(mBlack58Color);
        }
    }

    private EventType getEventType() {
        EventType eventType = null;
        switch (mEventTypeRadioGroup.getCheckedRadioButtonId()) {
            case R.id.event_type_radio_button:
                eventType = EventType.ALARM;
                break;
            case R.id.reminder_type_radio_button:
                eventType = EventType.REMINDER;
                break;

        }
        return eventType;
    }

    private enum StepStatus {
        ACTIVE, SET, NOT_SET
    }


}
