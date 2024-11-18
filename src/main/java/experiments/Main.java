package experiments;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Main {
    public static void main(String[] args) {

        double offset = Math.random() * 120;
        double variance = ((Math.random() - 0.5)*2) * 30;




        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        LocalTime targetTime = LocalTime.now().plusSeconds(10);
        long delay = Duration.between(LocalTime.now(), targetTime).toMillis();
        scheduler.schedule(() -> {
            System.out.println("Task 1executed at: " + LocalTime.now());
        }, delay, TimeUnit.MINUTES);

        LocalTime targetTime2 = LocalTime.now().plusSeconds(5);
        long delay2 = Duration.between(LocalTime.now(), targetTime2).toMillis();
        scheduler.schedule(() -> {
            System.out.println("Task 2executed at: " + LocalTime.now());
        }, delay2, TimeUnit.MILLISECONDS);

        // Shut down the scheduler after the task runs
        scheduler.shutdown();

    }
}
