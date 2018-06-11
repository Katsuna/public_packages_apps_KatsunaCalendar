package com.katsuna.calendar.data.source;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;


import com.katsuna.calendar.data.Event;

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

    private static final Event EVENT = new Event( "name",3,5);

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
        Event newEvent = new Event(eventId, "test", "description2", 10,11,2018, "13:00","14:00");
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
        Event updatedEvent = new Event(eventId, "test", "description2", 10,11,2018, "13:00","14:00");
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
