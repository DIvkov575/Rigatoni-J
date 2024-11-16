package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {
    private static final String ACCOUNTS_PATH = "assets/accounts.txt";
    private static final String URI = "https://open.spotify.com/playlist/1uqAtFEvDCUEwHouRP4Ogx?si=TBIQLRqQTv-SN5OGPyOkBw";


    public static void main(String[] args) {

        System.exit(0);
        System.out.println("-------- Rigatoni --------");

        List<Account> accounts = getAccounts(ACCOUNTS_PATH);
        List<WebDriver> drivers = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        System.out.println("accoutns   " + accounts.toString());

        for (Account account : accounts) {
            executorService.submit(() -> {
                String randomLanguage = getRandomItem(SUPPORTED_LANGUAGES);
                String randomUserAgent = getRandomItem(USER_AGENTS);

                WebDriver driver = createDriver(randomUserAgent, randomLanguage);

                try {
                    authenticate(driver, account.getUsername(), account.getPassword());
                    TimeUnit.SECONDS.sleep(getRandomDelay(4, 6));

                    driver.get(URI);
                    TimeUnit.SECONDS.sleep(getRandomDelay(2, 6));

                    tryAcceptCookies(driver, account.getUsername());
                    TimeUnit.SECONDS.sleep(getRandomDelay(5, 10));

                    try {
                        pressPlayPlaylist(driver);
                        TimeUnit.SECONDS.sleep(getRandomDelay(2, 6));
                    } catch (Exception e) {
                        pressPlayPlaylist(driver);
                        TimeUnit.SECONDS.sleep(getRandomDelay(2, 6));
                    }

                    enableRepeat(driver);
                    drivers.add(driver);

                    System.out.println("Username: " + account.getUsername() + " - Listening process has started.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
    }

    private static List<Account> getAccounts(String filePath) {
        List<Account> accounts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                accounts.add(new Account(parts[0], parts[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accounts;
    }

}
