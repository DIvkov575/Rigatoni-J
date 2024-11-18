package org.divkov.rigatoni;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Data
@Jacksonized
public class Driver {
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
    private String username;
    private String password;
    private String[] playables;

    private WebDriver driver = null;
    private String state = "";
    private String[] queue = new String[0];

    public void createDriver() {
        Random random = new Random();
        String userAgent = USER_AGENTS[random.nextInt(USER_AGENTS.length)];
        String language= SUPPORTED_LANGUAGES[random.nextInt(SUPPORTED_LANGUAGES.length)];

        System.setProperty("webdriver.chrome.driver", "assets/chromedriver");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-logging", "--log-level=3", "--disable-infobars", "--disable-extensions");
        options.addArguments("--window-size=1366,768", "--mute-audio", "--disable-notifications");
        options.addArguments("--user-agent=" + userAgent, "--lang=" + language);

        this.driver = new ChromeDriver(options);
    }

    public void authenticate() {
        this.driver.get("https://www.spotify.com/us/login/");

        WebElement usernameInput = this.driver.findElement(By.id("login-username"));
        usernameInput.sendKeys(this.username);
        Main.random_sleep(1,3);

        // sometimes renders "username-only" page ∴ try-login to continue to password entry
       try {
           WebElement passwordInput = this.driver.findElement(By.id("login-password"));
           passwordInput.sendKeys(this.password);
           Main.random_sleep(1,3);
        } catch (Exception e) {
            this.driver.findElement(By.cssSelector("button[data-testid='login-button']")).click();
            Main.random_sleep(1,3);
            WebElement passwordInput = this.driver.findElement(By.id("login-password"));
            passwordInput.sendKeys(this.password);
            Main.random_sleep(1,3);
       }
        this.driver.findElement(By.cssSelector("button[data-testid='login-button']")).click();
        Main.random_sleep(1,3);
    }

    public void play() {
        String xpath;
        String url = this.driver.getCurrentUrl();
        System.out.println(url);

        String regex = "https://open\\.spotify\\.com/([^/]+)/";
        Matcher matcher = Pattern.compile(regex).matcher(url);

        switch (matcher.group(1)) {
            case "playlist" -> xpath = """
                    //*[@id="main"]/div/div[2]/div[4]/div/div[2]/div[2]/div/main/section/div[2]/div[2]/div[2]/div/div/div[1]/button
                    """;
            case "artist" -> xpath = """
                    //*[@id="main"]/div/div[2]/div[4]/div/div[2]/div[2]/div/main/section/div/div[2]/div[2]/div[2]/div/div/span/div/button
                    """;
            case "track" -> xpath = """
                   //*[@id="main"]/div/div[2]/div[4]/div/div[2]/div[2]/div/main/section/div[3]/div[2]/div/div/div/button 
                    """;
            default  -> {throw new IllegalArgumentException("invalid page url to match");}
        }

        this.driver.findElement(By.xpath(xpath)).click();
    }

    public void navigate(String url) {
        this.driver.get(url);
        this.setState(url);
    }

    public void close() {
        this.driver.close();
        this.setState("");
    }
}


