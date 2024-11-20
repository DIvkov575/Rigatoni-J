package org.divkov.rigatoni;

import lombok.Data;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class Driver {
    private static final String proxyAddress = "geo.iproyal.com:12321";
    private static final String[] SUPPORTED_LANGUAGES = {
            "en-US", "en-GB", "en-CA", "en-AU", "en-NZ", "fr-FR", "fr-CA", "fr-BE", "fr-CH", "fr-LU",
            "de-DE", "de-AT", "de-CH", "de-LU", "es-ES", "es-MX", "es-AR", "es-CL", "es-CO", "es-PE",
            "it-IT", "it-CH", "ja-JP", "ko-KR", "pt-BR", "pt-PT", "ru-RU", "tr-TR", "nl-NL", "nl-BE",
            "sv-SE", "da-DK", "no-NO"
    };

    private static final String[] USER_AGENTS = {
            // Chrome (Windows)
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36",

            // Chrome (Mac)
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.2 Safari/605.1.15",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_4_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.6723.116 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.6723.116 Safari/537.36",

            // Firefox (Windows)
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:93.0) Gecko/20100101 Firefox/93.0",
            "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:93.0) Gecko/20100101 Firefox/93.0",
            "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:93.0) Gecko/20100101 Firefox/93.0",

            // Firefox (Mac)
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:93.0) Gecko/20100101 Firefox/93.0",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 11.4; rv:93.0) Gecko/20100101 Firefox/93.0",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:93.0) Gecko/20100101 Firefox/93.0",

            // Safari (Mac)
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.2 Safari/605.1.15",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_4_0) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.2 Safari/605.1.15",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.2 Safari/605.1.15",

            // Opera (Windows)
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36 OPR/80.0.4170.61",
            "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36 OPR/80.0.4170.61",
            "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36 OPR/80.0.4170.61"
    };
    private String username;
    private String password;
    private String[] playables;

    private WebDriver driver = null;
    private String state = "";

    public void init() {
        System.setProperty("webdriver.chrome.driver", "assets/chromedriver");
        Random random = new Random();
        String userAgent = USER_AGENTS[random.nextInt(USER_AGENTS.length)];
        String language= SUPPORTED_LANGUAGES[random.nextInt(SUPPORTED_LANGUAGES.length)];

        ChromeOptions options = new ChromeOptions();

        if (Math.random() < 0.25) {
            Proxy proxy = new Proxy();
            proxy.setHttpProxy(proxyAddress).setSslProxy(proxyAddress);
            options.setProxy(proxy);
        }

        options.addArguments("--disable-logging", "--log-level=3", "--disable-infobars", "--disable-extensions");
        options.addArguments("--window-size=1366,768", "--mute-audio", "--disable-notifications");
        options.addArguments("--user-agent=" + userAgent, "--lang=" + language);

        this.driver = new ChromeDriver(options);
    }

    public void authenticate() {
        this.driver.get("https://www.spotify.com/us/login/");
        this.awaitPageLoad();

        WebElement usernameInput = this.driver.findElement(By.id("login-username"));
        usernameInput.sendKeys(this.username);
        this.awaitPageLoad();
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

    public boolean isAsleep() {
        return this.state.isEmpty();
    }
    public void awaitPageLoad() {
        new WebDriverWait(this.driver, Duration.ofSeconds(1))
                .until(webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete"));
    }

    public void play(String url) {
        String xpath;
        System.out.println(url);

        this.driver.get(url);
        this.setState(url);
        this.awaitPageLoad();
        Main.random_sleep(4,6);

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

    public void sleep() {
        this.driver.get("");
        this.setState("");
    }

    public void close() {
        this.driver.close();
        this.setState("");
    }
}


