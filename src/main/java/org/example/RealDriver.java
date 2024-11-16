package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Random;

import static org.example.App.DRIVER_PATH;

class Driver {
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
    private WebDriver driver;
    private final String username;
    private final String password;

    Driver(String username, String password) {
        this.username = username;
        this.password = password;

        Random random = new Random();
        String user_agent = USER_AGENTS[random.nextInt(USER_AGENTS.length)];
        String language= SUPPORTED_LANGUAGES[random.nextInt(SUPPORTED_LANGUAGES.length)];

        this.createDriver(user_agent, language);
    }

    private void createDriver(String userAgent, String language) {
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
        WebElement passwordInput = this.driver.findElement(By.id("login-password"));
        usernameInput.sendKeys(this.username);
        passwordInput.sendKeys(this.password);
        this.driver.findElement(By.cssSelector("button[data-testid='login-button']")).click();
    }

    public void pressPlayPlaylist() {
        this.driver.findElement(By.xpath(
                        "//*[@id='main']/div/div[2]/div[4]/div[1]/div[2]/div[2]/div/main/section/div[2]/div[2]/div[2]/div/div/div[1]/button"))
                .click();
    }

    public void enableRepeat() {
        WebElement repeatButton = this.driver.findElement(By.xpath(
                "//*[@id='main']/div/div[2]/div[3]/footer/div/div[2]/div/div[1]/div[2]/button[2]"));
        String ariaLabel = repeatButton.getAttribute("aria-label");
        if ("Enable repeat".equals(ariaLabel)) {
            repeatButton.click();
        } else if ("Enable repeat one".equals(ariaLabel)) {
            repeatButton.click();
        }
    }

    public void tryAcceptCookies(String username) {
        try {
            this.driver.findElement(By.xpath("//button[text()='Accept Cookies']")).click();
            System.out.println(username + " Accepted Cookies");
        } catch (Exception e) {
            System.out.println(username + " failed to accept cookies");
        }
    }

}
