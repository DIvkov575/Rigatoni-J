package org.divkov.rigatoni;

import lombok.Data;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Data
public class Controller {
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    static private String weightedRandomPlayablePick(String[] playables) {
        if (Math.random() < 0.4) {
            return playables[0];
        } else {
            return playables[(int)(Math.random() * playables.length)];
        }

    }


    public void schedule_event(Driver driver) {
        int delay = (6*60) + (int)(120 * (Math.random() - 0.5));
        this.scheduler.schedule(() -> {

            String [] playables = driver.getPlayables();
            if (!driver.isAsleep() && Math.random() < 0.25) {
                driver.sleep();
            } else {
                String playable = weightedRandomPlayablePick(driver.getPlayables());
                driver.play(playable);
            }
            this.schedule_event(driver);
        }, delay, TimeUnit.MINUTES);
    }

    public void initialization(Driver driver ) {

        assert (driver.getDriver() == null);
        driver.init();
        driver.authenticate();
        Main.random_sleep(4, 6);

        driver.play(driver.getPlayables()[0]);

        this.schedule_event(driver);

        System.out.println("Driver" + driver.getUsername() + "initialized at" + LocalTime.now());
    }

    public void shutdown(Driver driver) {
        driver.close();

    }

}