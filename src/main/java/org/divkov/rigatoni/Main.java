package org.divkov.rigatoni;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

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

        Arrays.stream(drivers).parallel()
                .forEach(driver -> {
                    driver.createDriver();

                    driver.authenticate();
//                    Main.random_sleep(4, 6);

//                    driver.navigate(driver.getPlayables()[0]);
//                    Main.random_sleep(4, 6);
//
//                    driver.pressPlayPlaylist();
                });
    }
}