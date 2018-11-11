package com.katsuna.calendar.data;

import com.katsuna.calendar.util.LogUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class EventStateManager {
    private static final String TAG = EventStateManager.class.getSimpleName();

    private static final EventStateManager instance = new EventStateManager();
    private final LinkedHashMap<Long, EventState> map = new LinkedHashMap<>();

    private EventStateManager() {
    }

    public static synchronized EventStateManager getInstance() {
        return instance;
    }

    public synchronized boolean eventActive() {
        for(Map.Entry o: map.entrySet()) {
            if (o.getValue() == EventState.ACTIVATED) {
                return true;
            }
        }
        return false;
    }

    public synchronized EventState getEventState(Event Event) {
        return map.get(Event.getEventId());
    }

    public synchronized void setEventState(Event Event, EventState state) {
        LogUtils.i("%s setEventState:  %s", TAG, Event);
        map.put(Event.getEventId(), state);
    }

    public synchronized void removeEvent(Event Event) {
        LogUtils.i("%s removeEvent: %s", TAG, Event);
        map.remove(Event.getEventId());
    }
}
