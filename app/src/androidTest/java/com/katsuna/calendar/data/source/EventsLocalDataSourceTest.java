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
package com.katsuna.calendar.data.source;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventStatus;
import com.katsuna.calendar.data.EventType;
import com.katsuna.calendar.util.SingleExecutors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Integration test for the {@link EventsLocalDataSource}.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventsLocalDataSourceTest {

    private EventsLocalDataSource mLocalDataSource;

    private CalendarDatabase mDatabase;

    @Before
    public void setup() {
        // using an in-memory database for testing, since it doesn't survive killing the process
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                CalendarDatabase.class)
                .build();
        EventsDao eventsDao = mDatabase.eventsDao();

        // Make sure that we're not keeping a reference to the wrong instance.
        EventsLocalDataSource.clearInstance();
        mLocalDataSource = EventsLocalDataSource.getInstance(new SingleExecutors(), eventsDao);
    }

    @After
    public void cleanUp() {
        mDatabase.close();
        EventsLocalDataSource.clearInstance();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mLocalDataSource);
    }

    @Test
    public void saveEvent_retrievesEvent() {
        // Given a new event
       final Event newEvent = new Event( EventType.REMINDER, 5,11,2018, 12,20,"description2", EventStatus.ACTIVE,null, false);

        // When saved into the persistent repository
        mLocalDataSource.saveEvent(newEvent);

        // Then the event can be retrieved from the persistent repository
        mLocalDataSource.getEvent(newEvent.getEventId(), new EventsDataSource.GetEventCallback() {
            @Override
            public void onEventLoaded(Event event) {
                assertThat(event, is(newEvent));
            }

            @Override
            public void onDataNotAvailable() {
                fail("Callback error");
            }
        });
    }

    @Test
    public void getEvents_retrieveSavedEvents() {
        // Given 2 new events in the persistent repository
        final Event newEvent1 =new Event( EventType.REMINDER, 10,11,2018, 12,20,"description2", EventStatus.ACTIVE, null, false);
        mLocalDataSource.saveEvent(newEvent1);
        final Event newEvent2 = new Event( EventType.REMINDER, 10,11,2018, 1,20,"description2", EventStatus.ACTIVE, null, false);
        mLocalDataSource.saveEvent(newEvent2);

        // Then the events can be retrieved from the persistent repository
        mLocalDataSource.getEvents(new EventsDataSource.LoadEventsCallback() {
            @Override
            public void onEventsLoaded(List<Event> events) {
                assertNotNull(events);
                assertTrue(events.size() >= 2);

                boolean newEvent1IdFound = false;
                boolean newEvent2IdFound = false;
                for (Event event : events) {
                    if (event.getEventId()== newEvent1.getEventId()) {
                        newEvent1IdFound = true;
                    }
                    if (event.getEventId() == newEvent2.getEventId()) {
                        newEvent2IdFound = true;
                    }
                }
                assertTrue(newEvent1IdFound);
                assertTrue(newEvent2IdFound);
            }

            @Override
            public void onDataNotAvailable() {
                fail();
            }
        });
    }
}
