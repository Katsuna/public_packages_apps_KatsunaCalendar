/**
* Copyright (C) 2020 Manos Saratsis
*
* This file is part of Katsuna.
*
* Katsuna is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Katsuna is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Katsuna.  If not, see <https://www.gnu.org/licenses/>.
*/
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
