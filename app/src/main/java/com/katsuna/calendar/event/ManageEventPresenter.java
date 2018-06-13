package com.katsuna.calendar.event;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventStatus;
import com.katsuna.calendar.data.EventType;
import com.katsuna.calendar.services.IEventsScheduler;
import com.katsuna.calendar.data.source.EventsDataSource;


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


    @Override
    public void start() {
        if (!isNewEvent() && mIsDataMissing) {
            populateEvent();
        } else {
            initTime();
        }
    }

    private void initTime() {
        LocalDateTime now = LocalDateTime.now();
        mManageEventView.setTime(now.format(DateTimeFormatter.ofPattern("HH")),
                now.format(DateTimeFormatter.ofPattern("mm")));
    }



    @Override
    public void saveEvent(@NonNull EventType eventType, String description, String hour, String minute, String day) {
        List<ValidationResult> results = mEventValidator.validateAll(eventType, description, hour,
                minute);
        if (results.size() == 0) {
            // all good, move on
            Event event;
            if (isNewEvent()) {
                event = new Event(eventType, Integer.parseInt(hour), Integer.parseInt(minute),
                        description,EventStatus.ACTIVE);
            } else {
                event = new Event(mEventIdAutogenerated, eventType, Integer.parseInt(hour),
                        Integer.parseInt(minute), description, EventStatus.ACTIVE);
            }
            mEventsDataSource.saveEvent(event);
            mEventsScheduler.reschedule(event);
            mManageEventView.showEventsList();
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
        ManageEventStep nextStep = null;
        switch (mStep) {
            case TIME:
                nextStep = ManageEventStep.TYPE;
                break;
            case DAYS:
                nextStep = ManageEventStep.TIME;
                break;
        }

        if (nextStep != null) {
            mStep = nextStep;
            showStep(nextStep);
        }
    }

    @Override
    public void validateEventTypeInfo(EventType eventType, String description) {

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
    private void showStep(ManageEventStep step) {
        mStep = step;

        mManageEventView.adjustFabPositions(step);
    }

    @Override
    public ManageEventStep getCurrentStep() {
        return mStep;
    }

    @Override
    public void eventTypeSelected(EventType eventType) {

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
