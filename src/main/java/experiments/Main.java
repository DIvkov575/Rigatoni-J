package experiments;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Main {
    public static void main(String[] args){
        System.setProperty("webdriver.chrome.driver", "assets/chromedriver");

        String proxyAddress = "geo.iproyal.com:12321";

        Proxy proxy = new Proxy();
        proxy.setHttpProxy(proxyAddress).setSslProxy(proxyAddress);

        ChromeOptions options = new ChromeOptions();
        options.setProxy(proxy);
        options.addArguments("--disable-logging", "--log-level=3", "--disable-infobars", "--disable-extensions");
        options.addArguments("--window-size=1366,768", "--mute-audio", "--disable-notifications");

        ChromeDriver driver = new ChromeDriver(options);
    }
}
