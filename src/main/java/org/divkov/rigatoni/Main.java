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

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Driver[] drivers = objectMapper.readValue(new File("./assets/accounts.json"), Driver[].class);
        Controller controller = new Controller();

        Arrays.stream(Arrays.copyOfRange(drivers, 0, 1))
                .forEach(driver -> {
                    controller.initialization(drivers, driver);
                });


    }
}