package org.mycodev.demoqashop;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.*;

import static org.testng.Assert.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainPageTest {
	private WebDriver driver;
    private MainPage mainPage;

    @BeforeClass
    public void setUp() {
        ChromeOptions chromeOptions = new ChromeOptions();
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(chromeOptions);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://shop.demoqa.com");

        mainPage = new MainPage(driver);
    }

    @AfterClass
    public void tearDown() {

        driver.quit();
    }

    @Test
    public void addToCart() {
        // Using list web-element get all web-elements, whose classname name as "noo-thumbnail-product"
        List<WebElement> links = driver.findElements(By.className("noo-thumbnail-product"));

        Random r = new Random();

        // get a random link from the list
        String link = links.get(r.nextInt(links.size()-1)).getAttribute("href");

        driver.findElement(By.xpath("(//a[@href='"+ link +"'])[2]")).click();

        String idColor, idSize;

        if (!driver.findElements(By.id("pa_color")).isEmpty()) {
            idColor = "pa_color";
            idSize  = "pa_size";
        } else {
            idColor = "color";
            idSize  = "size";
        }

        WebElement color = driver.findElement(By.id(idColor));

        Select colorOptions = new Select(color);

        // generate an index randomly
        colorOptions.selectByIndex(mainPage.getIndex(colorOptions.getOptions().size()));

        WebElement size = driver.findElement(By.id(idSize));

        Select sizeOptions = new Select(size);

        // generate an index randomly
        sizeOptions.selectByIndex(mainPage.getIndex(sizeOptions.getOptions().size()));

        WebElement submitButton = driver.findElement(By.cssSelector("button.single_add_to_cart_button"));
        submitButton.click();

        String successMessage = driver.findElement(By.className("woocommerce-message")).getText();

        assertTrue(successMessage.endsWith("has been added to your cart."), "Error: no product was added to the cart.");
    }

    @Test(dependsOnMethods={"addToCart"})
    public void viewCart() {
        new Actions(driver)
                .click(mainPage.viewCart)
                .perform();

        String price = driver.findElement(By.cssSelector("span.cart-name-and-total bdi")).getText();

        String total = driver.findElement(By.cssSelector("strong bdi")).getText();

        WebElement productElement = driver.findElement(By.cssSelector("td.product-name > a"));

        assertTrue(productElement.isDisplayed(), "Error: no product was added to the cart, the cart is empty.");

        assertEquals(price, total);
    }

    @Test(dependsOnMethods={"viewCart"})
    public void proceedToCheckOut() {
        new Actions(driver)
                .click(mainPage.checkOut)
                .perform();

        // using JFairy to generate random data
        Fairy fairy = Fairy.create();
        Person person = fairy.person();

        // fill required fields
        driver.findElement(By.id("billing_first_name")).sendKeys(person.getFirstName());
        driver.findElement(By.id("billing_last_name")).sendKeys(person.getLastName());
        
        WebElement country = driver.findElement(By.id("billing_country"));
        Select countriesOptions = new Select(country);
        
        //countriesOptions.selectByValue(person.getNationality().getCode());
        countriesOptions.selectByIndex(mainPage.getIndex(countriesOptions.getOptions().size()));
        
        driver.findElement(By.id("billing_address_1")).sendKeys(person.getAddress().getAddressLine1());
        driver.findElement(By.id("billing_postcode")).sendKeys(person.getAddress().getPostalCode());
        driver.findElement(By.id("billing_city")).sendKeys(person.getAddress().getCity());

        if(!driver.findElements(By.id("billing_state")).isEmpty()) {
            WebElement state = driver.findElement(By.id("billing_state"));
            Select stateOptions = new Select(state);
            stateOptions.selectByIndex(mainPage.getIndex(stateOptions.getOptions().size()));
        }

        driver.findElement(By.id("billing_phone")).sendKeys(person.getTelephoneNumber());
        driver.findElement(By.id("billing_email")).sendKeys(person.getEmail());

        // check on checkbox terms
        driver.findElement(By.id("terms")).click();

        driver.findElement(By.id("place_order")).click();

        WebElement successOrder = driver.findElement(By.cssSelector("p.woocommerce-thankyou-order-received"));

        assertEquals(successOrder.getText(), "Thank you. Your order has been received.");
    }
}
