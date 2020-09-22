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
