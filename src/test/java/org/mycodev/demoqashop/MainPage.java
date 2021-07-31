package org.mycodev.demoqashop;

import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class MainPage {
    @FindBy(xpath = "//a[contains(text(),'Proceed to checkout')]")
    public WebElement checkOut;

    @FindBy(xpath = "//a[contains(text(),'View cart')]")
    public WebElement viewCart;

    public MainPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
    
    public int getIndex(int number) {
    	Random r = new Random();
    	// Get a random number between 0 and the number of options.
        int index = r.nextInt(number - 1);

        // we exclude the first element
        if (index == 0) {
            index = 1;
        }
        
        return index++;
    }
}
