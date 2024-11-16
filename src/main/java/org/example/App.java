import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CommandExecutor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SpotifyAutomation {
    private static final String DRIVER_PATH = "assets/chromedriver";
    private static final String ACCOUNTS_PATH = "assets/accounts.txt";
    private static final String URI = "https://open.spotify.com/playlist/1uqAtFEvDCUEwHouRP4Ogx?si=TBIQLRqQTv-SN5OGPyOkBw";

    private static final String[] SUPPORTED_LANGUAGES = {
            "en-US", "en-GB", "en-CA", "en-AU", "en-NZ", "fr-FR", "fr-CA", "fr-BE", "fr-CH", "fr-LU",
            "de-DE", "de-AT", "de-CH", "de-LU", "es-ES", "es-MX", "es-AR", "es-CL", "es-CO", "es-PE",
            "it-IT", "it-CH", "ja-JP", "ko-KR", "pt-BR", "pt-PT", "ru-RU", "tr-TR", "nl-NL", "nl-BE",
            "sv-SE", "da-DK", "no-NO"
    };

    private static final String[] USER_AGENTS = {
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_4_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.6723.116 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.6723.116 Safari/537.36"
    };

    public static void main(String[] args) {
        System.out.println("-------- Rigatoni --------");

        List<Account> accounts = getAccounts(ACCOUNTS_PATH);
        List<WebDriver> drivers = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(5);

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

    private static WebDriver createDriver(String userAgent, String language) {
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-logging", "--log-level=3", "--disable-infobars", "--disable-extensions");
        options.addArguments("--window-size=1366,768", "--mute-audio", "--disable-notifications");
        options.addArguments("--user-agent=" + userAgent, "--lang=" + language);

        return new ChromeDriver(options);
    }

    private static void authenticate(WebDriver driver, String username, String password) {
        driver.get("https://www.spotify.com/us/login/");
        WebElement usernameInput = driver.findElement(By.id("login-username"));
        WebElement passwordInput = driver.findElement(By.id("login-password"));
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        driver.findElement(By.cssSelector("button[data-testid='login-button']")).click();
    }

    private static void pressPlayPlaylist(WebDriver driver) {
        driver.findElement(By.xpath(
                        "//*[@id='main']/div/div[2]/div[4]/div[1]/div[2]/div[2]/div/main/section/div[2]/div[2]/div[2]/div/div/div[1]/button"))
                .click();
    }

    private static void enableRepeat(WebDriver driver) {
        WebElement repeatButton = driver.findElement(By.xpath(
                "//*[@id='main']/div/div[2]/div[3]/footer/div/div[2]/div/div[1]/div[2]/button[2]"));
        String ariaLabel = repeatButton.getAttribute("aria-label");
        if ("Enable repeat".equals(ariaLabel)) {
            repeatButton.click();
        } else if ("Enable repeat one".equals(ariaLabel)) {
            repeatButton.click();
        }
    }

    private static void tryAcceptCookies(WebDriver driver, String username) {
        try {
            driver.findElement(By.xpath("//button[text()='Accept Cookies']")).click();
            System.out.println(username + " Accepted Cookies");
        } catch (Exception e) {
            System.out.println(username + " failed to accept cookies");
        }
    }

    private static String getRandomItem(String[] array) {
        Random random = new Random();
        return array[random.nextInt(array.length)];
    }

    private static int getRandomDelay(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}

class Account {
    private final String username;
    private final String password;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
