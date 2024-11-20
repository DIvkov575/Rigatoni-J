package org.divkov.rigatoni;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Main {

    static void random_sleep(int a, int b) {
        double duration = 1000 * (Math.random() * Math.abs(b - a) + Math.min(a, b));
        try {Thread.sleep((int) duration);}
        catch (InterruptedException e) {throw new RuntimeException(e);}
    }

    public static void main(String[] args) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        Driver[] all_drivers = objectMapper.readValue(new File("./assets/accounts.json"), Driver[].class);
        Driver[] drivers = Arrays.copyOfRange(all_drivers, 0, all_drivers.length);
        Collections.shuffle(List.of(drivers));
        Controller controller = new Controller();


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Arrays.stream(drivers).forEach(controller::shutdown);
        }));

        Arrays.stream(drivers).forEach(controller::initialization);
    }
}