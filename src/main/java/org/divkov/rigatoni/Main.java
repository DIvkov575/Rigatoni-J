package org.divkov.rigatoni;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Main {
    static void random_sleep(int a, int b) {
        double duration = 1000 * (Math.random() * Math.abs(b - a) + Math.min(a, b));
        try {Thread.sleep((int) duration);}
        catch (InterruptedException e) {throw new RuntimeException(e);}
    }
    static void fixed_sleep(int k) {
        try {Thread.sleep(k * 1000);}
        catch (InterruptedException e) {throw new RuntimeException(e);}
    }
    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Driver[] drivers= objectMapper.readValue(new File("./assets/accounts.json"), Driver[].class);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        Arrays.stream(Arrays.copyOfRange(drivers, 0, 1)).parallel()
            .forEach(driver -> {

                // schedule initialization
                scheduler.schedule(() -> {
                    for (Driver driver2: drivers) {
                        if (driver2.getUsername().equals(driver.getUsername())) {
                            System.out.println("Driver" + driver2.getUsername() + "initialized at" + LocalTime.now());
                            driver2.createDriver();
                            driver2.authenticate();
                            Main.random_sleep(4, 6);

                            driver.navigate(driver.getPlayables()[0]);
                            Main.random_sleep(4, 6);

                            driver.play();
                            // TODO: schedule next event
                        }
                    }
                }, (int) (Math.random() * 120 * 60_000), TimeUnit.MILLISECONDS);
            });


        scheduler.shutdown();


//        double variance = ((Math.random() - 0.5)*2) * 30;
//
//        LocalTime targetTime = LocalTime.now().plusSeconds(10);
//        long delay = Duration.between(LocalTime.now(), targetTime).toMillis();
//        scheduler.schedule(() -> {
//            System.out.println("Task 1executed at: " + LocalTime.now());
//        }, delay, TimeUnit.MINUTES);
//
//        LocalTime targetTime2 = LocalTime.now().plusSeconds(5);
//        long delay2 = Duration.between(LocalTime.now(), targetTime2).toMillis();
//        scheduler.schedule(() -> {
//            System.out.println("Task 2executed at: " + LocalTime.now());
//        }, delay2, TimeUnit.MILLISECONDS);
//
//        // Shut down the scheduler after the task runs




//        Arrays.stream(Arrays.copyOfRange(drivers, 0, 1)).parallel()
//                .forEach(driver -> {
//                    driver.createDriver();
//
//                    driver.authenticate();
//                    Main.random_sleep(4, 6);
//
//                    driver.navigate(driver.getPlayables()[0]);
//                    Main.random_sleep(4, 6);
//
//                    driver.play();
//                });
    }




}