package com.katsuna.calendar.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.util.AppExecutors;

import java.util.List;

public class EventsLocalDataSource implements EventsDataSource {
    private static volatile EventsLocalDataSource INSTANCE;

    private final EventsDao mEventsDao;

    private final AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private EventsLocalDataSource(@NonNull AppExecutors appExecutors,
                                  @NonNull EventsDao eventsDao) {
        mAppExecutors = appExecutors;
        mEventsDao = eventsDao;
    }

    public static EventsLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                    @NonNull EventsDao eventsDao) {
        if (INSTANCE == null) {
            synchronized (EventsLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new EventsLocalDataSource(appExecutors, eventsDao);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    static void clearInstance() {
        INSTANCE = null;
    }

    /**
     * Note: {@link LoadEventsCallback#onDataNotAvailable()} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void getEvents(@NonNull final LoadEventsCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Event> Events = mEventsDao.getEvents();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (Events.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onEventsLoaded(Events);
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    /**
     * Note: {@link GetEventCallback#onDataNotAvailable()} is fired if the {@link Event} isn't
     * found.
     */
    @Override
    public void getEvent(final long eventId, @NonNull final GetEventCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Event event = mEventsDao.getEventById(eventId);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (event != null) {
                            callback.onEventLoaded(event);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public Event getEvent(long eventId) {
        return mEventsDao.getEventById(eventId);
    }

    @Override
    public void saveEvent(@NonNull final Event event) {
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                if (event.getEventId() == 0) {
                    long eventId = mEventsDao.insertEvent(event);
                    event.setEventId(eventId);
                } else {
                    mEventsDao.updateEvent(event);
                }
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void deleteEvent(final long EventId) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mEventsDao.deleteEventById(EventId);
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }
}
