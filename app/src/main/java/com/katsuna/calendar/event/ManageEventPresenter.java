package com.katsuna.calendar.event;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.katsuna.calendar.R;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventStatus;
import com.katsuna.calendar.data.EventType;
import com.katsuna.calendar.services.IEventsScheduler;
import com.katsuna.calendar.data.source.EventsDataSource;
import com.katsuna.calendar.validators.IEventValidator;
import com.katsuna.calendar.validators.ValidationResult;


import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

class ManageEventPresenter implements ManageEventContract.Presenter,
        EventsDataSource.GetEventCallback {

    @NonNull
    private final EventsDataSource mEventsDataSource;

    @NonNull
    private final ManageEventContract.View mManageEventView;

    @NonNull
    private final IEventValidator mEventValidator;

    private final long mEventId;

    @NonNull
    private final IEventsScheduler mEventsScheduler;

    private boolean mIsDataMissing = true;
    private ManageEventStep mStep = ManageEventStep.TYPE;
    private long mEventIdAutogenerated;
    private EventType mEventType;

    public ManageEventPresenter(long eventId, EventType eventType,
                                @NonNull EventsDataSource eventsDataSource,
                                @NonNull ManageEventContract.View manageEventView,
                                @NonNull IEventValidator eventValidator,
                                @NonNull IEventsScheduler eventsScheduler) {
        mEventId = eventId;
        mEventType =eventType;
        mEventsDataSource = checkNotNull(eventsDataSource, "dataSource cannot be null");
        mManageEventView = checkNotNull(manageEventView, "manageEventView cannot be null!");
        mEventValidator = checkNotNull(eventValidator, "eventValidator cannot be null!");
        mEventsScheduler = checkNotNull(eventsScheduler, "eventsScheduler cannot be null!");

        mManageEventView.setPresenter(this);
    }

    @Override
    public void start() {

        if (!isNewEvent() && mIsDataMissing) {
            populateEvent();
        } else {
            adjustType();

            initTime();
            mManageEventView.setDefaultRingtone();
            mManageEventView.setDefaultVibrate();
        }
    }
    private void adjustType() {
        if (mEventType == EventType.ALARM) {
            mManageEventView.showDescriptionStep(true);


            mManageEventView.setTitle(isNewEvent() ? R.string.add_new_event_title :
                    R.string.edit_event_title);
            mManageEventView.setEventTimeTitle(R.string.set_event_time);
            showStep(ManageEventStep.TIME);
        } else {
            mManageEventView.showDescriptionStep(true);
            mManageEventView.setTitle(isNewEvent() ? R.string.add_new_reminder_title :
                    R.string.edit_reminder_title);
            mManageEventView.setEventTimeTitle(R.string.set_reminder_time);
        }
    }

    private void initTime() {
        LocalDateTime now = LocalDateTime.now();
        mManageEventView.setTime(now.format(DateTimeFormatter.ofPattern("HH")),
                now.format(DateTimeFormatter.ofPattern("mm")));
    }

    @Override
    public void saveEvent(@NonNull EventType eventType, String description, String hour, String minute, String day, String month,  String year, String ringtone, boolean vibrate) {
        List<ValidationResult> results = mEventValidator.validateAll(eventType, description, hour,
                minute);
        if (results.size() == 0) {
            // all good, move on
            Event event;
            if (isNewEvent()) {
                event = new Event(eventType, Integer.parseInt(month), Integer.parseInt(day), Integer.parseInt(year), Integer.parseInt(hour), Integer.parseInt(minute),
                        description,EventStatus.ACTIVE, ringtone, vibrate);
            } else {
                event = new Event(mEventIdAutogenerated, eventType, Integer.parseInt(month), Integer.parseInt(day), Integer.parseInt(year), Integer.parseInt(hour), Integer.parseInt(minute),
                        description,EventStatus.ACTIVE, ringtone, vibrate);
            }
            mEventsDataSource.saveEvent(event);
            mEventsScheduler.reschedule(event);
//            mManageEventView.showEventsList();
            mManageEventView.showCalendarOnReturn(event);
        } else {
            mManageEventView.showValidationResults(results);
        }
    }

    @Override
    public void populateEvent() {
        if (isNewEvent()) {
            throw new RuntimeException("populateEvent() was called but event is new.");
        }
        //noinspection ConstantConditions
        mEventsDataSource.getEvent(mEventId, this);
    }

    @Override
    public boolean isDataMissing() {
        return mIsDataMissing;
    }

    @Override
    public void previousStep() {
        ManageEventStep previousStep = null;
        switch (mStep) {
            case TIME:
                previousStep = ManageEventStep.TYPE;
                break;
            case DAYS:
                previousStep = ManageEventStep.TIME;
                break;
            case OPTIONS:
                previousStep = ManageEventStep.DAYS;
                break;
        }

        if (previousStep != null) {
            mStep = previousStep;
            showStep(previousStep);
        }
    }


    @Override
    public void validateEventTypeInfo(EventType eventType, String description) {
        List<ValidationResult> results = mEventValidator.validateEventType(eventType, description);
        if (results.size() == 0) {
            showStep(ManageEventStep.TIME);
        } else {
            mManageEventView.showValidationResults(results);
        }
    }

    @Override
    public void validateEventTime(String hour, String minute) {
        List<ValidationResult> results = mEventValidator.validateTime(hour, minute);
        if (results.size() == 0) {
            showStep(ManageEventStep.DAYS);
        } else {
            mManageEventView.showValidationResults(results);
        }
    }

    @VisibleForTesting
    @Override
    public void showStep(ManageEventStep step) {
        mStep = step;
        switch (step) {
            case TYPE:
                mManageEventView.showDescriptionControl(true);
                mManageEventView.showEventTimeControl(false);
                mManageEventView.showPreviousStepFab(false);
                mManageEventView.showEventTimeControl(false);
                mManageEventView.showEventOptionsControl(false);

                mManageEventView.showNextStepFab(true);
                break;
            case TIME:
                mManageEventView.showDescriptionControl(false);
                mManageEventView.showEventTimeControl(true);
                mManageEventView.showEventTimeControl(false);
                mManageEventView.showEventTimeControl(false);
                mManageEventView.showEventOptionsControl(false);

//                if (mEventType == EventType.ALARM) {
//                    mManageEventView.showPreviousStepFab(false);
//                    mManageEventView.showNextStepFab(true);
//                } else {
                    mManageEventView.showPreviousStepFab(true);
                    mManageEventView.showNextStepFab(true);
//                }
                break;
            case DAYS:
                mManageEventView.showPreviousStepFab(true);
                mManageEventView.showDescriptionControl(false);
                mManageEventView.showEventTimeControl(false);
                mManageEventView.showEventTimeControl(true);
                mManageEventView.showEventOptionsControl(false);
                mManageEventView.hideKeyboard();
                break;
            case OPTIONS:
                mManageEventView.showPreviousStepFab(true);
                mManageEventView.showDescriptionControl(false);
                mManageEventView.showEventTimeControl(false);
//                mManageEventView.showAlarmDaysControl(false);
                mManageEventView.showEventOptionsControl(true);
                break;
        }
        mManageEventView.adjustFabPositions(step);
    }

    @Override
    public ManageEventStep getCurrentStep() {
        return mStep;
    }


    @Override
    public void eventTypeSelected(EventType eventType) {
        mManageEventView.showDescriptionControl(eventType == EventType.REMINDER);
        mManageEventView.showNextStepFab(true);
    }

    @Override
    public void addHours(String hour, int hours) {
        int h = Integer.parseInt(hour);
        LocalTime time = LocalTime.of(h, 0).plusHours(hours);
        mManageEventView.setHour(time.format(DateTimeFormatter.ofPattern("HH")));
    }

    @Override
    public void addMinutes(String minute, int minutes) {
        int min = Integer.parseInt(minute);
        LocalTime time = LocalTime.of(0, min).plusMinutes(minutes);
        mManageEventView.setMinute(time.format(DateTimeFormatter.ofPattern("mm")));
    }

    private boolean isNewEvent() {
        return mEventId == 0;
    }

    @Override
    public void onEventLoaded(Event event) {
        mEventIdAutogenerated = event.getEventId();
        mManageEventView.loadEvent(event);
        mIsDataMissing = false;
        mManageEventView.showNextStepFab(true);
    }

    @Override
    public void onDataNotAvailable() {
        mManageEventView.showEmptyEventError();
    }
}
