import ru.test.events.eventbus.EventBus;
import ru.test.events.eventbus.LocalTimeService;

import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        EventBus eventBus = new EventBus();

        LocalTimeService timeService = new LocalTimeService();
        timeService.setEventBus(eventBus);
        timeService.setScheduledExecutorService(Executors.newScheduledThreadPool(1));
        timeService.start();

        Thread.sleep(20000);
    }

}
