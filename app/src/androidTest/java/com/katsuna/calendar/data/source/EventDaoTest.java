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
import android.support.test.runner.AndroidJUnit4;


import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventStatus;
import com.katsuna.calendar.data.EventType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertEquals;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class EventDaoTest {

    private static final Event EVENT = new Event( EventType.REMINDER, 10,11,2018, 12,20,"description2", EventStatus.ACTIVE, null, false );

    private CalendarDatabase mDatabase;

    @Before
    public void initDb() {
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                CalendarDatabase.class).build();
    }

    @After
    public void closeDb() {
        mDatabase.close();
    }

    @Test
    public void insertTaskAndGetById() {
        long eventId = mDatabase.eventsDao().insertEvent(EVENT);
        EVENT.setEventId(eventId);

        Event loaded = mDatabase.eventsDao().getEventById(eventId);

        assertEquals(EVENT, loaded);
    }

    @Test
    public void insertTaskReplacesOnConflict() {
        //Given that an event is inserted
        long eventId = mDatabase.eventsDao().insertEvent(EVENT);
        EVENT.setEventId(eventId);

        // When an event with the same id is inserted
        Event newEvent = new Event(eventId, EventType.REMINDER, 10,11,2018, 12,20,"description2", EventStatus.ACTIVE, null,false);
        mDatabase.eventsDao().insertEvent(newEvent);

        // When getting the event by id from the database
        Event loaded = mDatabase.eventsDao().getEventById(EVENT.getEventId());

        // The loaded data contains the expected values
        assertEquals(loaded, newEvent);
    }

    @Test
    public void updateEventAndGetById() {
        // When inserting an event
        long eventId = mDatabase.eventsDao().insertEvent(EVENT);
        EVENT.setEventId(eventId);


        // When the event is updated
        Event updatedEvent = new Event(eventId, EventType.REMINDER, 10,11,2018, 12,20,"description2", EventStatus.ACTIVE, null, false);
        mDatabase.eventsDao().updateEvent(updatedEvent);

        // When getting the event by id from the database
        Event loaded = mDatabase.eventsDao().getEventById(EVENT.getEventId());

        // The loaded data contains the expected values
        assertEquals(loaded, updatedEvent);
    }

    @Test
    public void deleteEventByIdAndGettingEvents() {
        //Given an event inserted
        mDatabase.eventsDao().insertEvent(EVENT);

        //When deleting a task by id
        mDatabase.eventsDao().deleteEventById(EVENT.getEventId());

        //When getting the events
        List<Event> events = mDatabase.eventsDao().getEvents();

        // The list is empty
        assertThat(events.size(), is(0));
    }

}
