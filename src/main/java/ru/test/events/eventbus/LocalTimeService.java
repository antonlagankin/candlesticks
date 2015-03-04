package ru.test.events.eventbus;

import java.util.Calendar;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LocalTimeService implements TimeService {

    private EventBus eventBus;
    private ScheduledExecutorService scheduledExecutorService;

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void setScheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }

    @Override
    public void start() {
        scheduledExecutorService.scheduleAtFixedRate(
                new Runnable() {

                    @Override
                    public void run() {
                        System.out.println(Calendar.getInstance());
                    }
                },
                0,
                100,
                TimeUnit.MILLISECONDS
        );
    }
}
