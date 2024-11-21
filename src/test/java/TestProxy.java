import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

//public class TestProxy {
//    public static void main() {
//        String proxyAddress = "geo.iproyal.com:12321";
//        String userAgent = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36";
//        String language = "en-US";
//
//        ChromeOptions options = new ChromeOptions();
//
//        System.out.println("Using Proxy!");
//        Proxy proxy = new Proxy();
//        proxy.setHttpProxy(proxyAddress).setSslProxy(proxyAddress);
//        options.setProxy(proxy);
//
//        options.addArguments("--disable-logging", "--log-level=3", "--disable-infobars", "--disable-extensions");
//        options.addArguments("--window-size=1366,768", "--mute-audio", "--disable-notifications");
//        options.addArguments("--user-agent=" + userAgent, "--lang=" + language);
//
//        ChromeDriver driver = new ChromeDriver(options);
//
//    }
//}
