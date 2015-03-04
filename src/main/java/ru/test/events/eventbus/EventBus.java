package ru.test.events.eventbus;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class EventBus {

    private ExecutorService executorService;
    private ConcurrentHashMap<Class, LinkedBlockingQueue<EventHandler>> eventToHandlerMap = new ConcurrentHashMap<Class, LinkedBlockingQueue<EventHandler>>();
    private ReentrantLock busLock = new ReentrantLock();

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void subscribe(EventHandler eventHandler, Class<? extends  Event> eventClass) {
        busLock.lock();
        try {
            LinkedBlockingQueue<EventHandler> queue = eventToHandlerMap.get(eventClass);
            if (queue == null) {
                queue = new LinkedBlockingQueue<EventHandler>();
                eventToHandlerMap.put(eventClass, queue);
            }
            queue.add(eventHandler);
        }
        finally {
            busLock.unlock();
        }
    }

    public void publish(final Event event) {
        LinkedBlockingQueue<EventHandler> queue = getEventHandlersList(event.getClass());

        if (queue == null) {
            return;
        }

        broadcastEvent(queue, event);
    }

    private LinkedBlockingQueue<EventHandler> getEventHandlersList(Class eventClass) {
        busLock.lock();

        try {
            return eventToHandlerMap.get(eventClass);
        }
        finally {
            busLock.unlock();
        }
    }

    private void broadcastEvent(LinkedBlockingQueue<EventHandler> queue, final Event event) {
        for (final EventHandler handler : queue) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    handler.handle(event);
                }
            });
        }
    }
}
