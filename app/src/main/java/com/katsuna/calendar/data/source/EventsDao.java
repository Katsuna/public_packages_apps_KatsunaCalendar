package com.katsuna.calendar.data.source;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.katsuna.calendar.data.Event;

import java.util.List;

@Dao
public interface EventsDao {
    /**
     * Select all events from the events table.
     *
     * @return all events.
     */
    @Query("SELECT * FROM events")
    List<Event> getEvents();

    /**
     * Select an event by id.
     *
     * @param eventId the task id.
     * @return the task with taskId.
     */
    @Query("SELECT * FROM events WHERE eventId = :eventId")
    Event getEventById(long eventId);

    /**
     * Insert an event in the database. If the event already exists, replace it.
     *
     * @param event the event to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertEvent(Event event);

    /**
     * Update an event.
     *
     * @param event event to be updated
     */
    @Update
    void updateEvent(Event event);

    /**
     * Delete an event by id.
     */
    @Query("DELETE FROM events WHERE eventId = :eventId")
    void deleteEventById(long eventId);

}
