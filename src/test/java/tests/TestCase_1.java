package tests;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestCase_1 {

        public static void main(String[] args) throws InterruptedException{
            WebDriverManager.chromedriver().setup();
            ChromeDriver driver=new ChromeDriver();
            driver.get("https://www.webstaurantstore.com/");
            driver.manage().window().maximize();

            // to search for "stainless work table"
            driver.findElement(By.id("searchval")).sendKeys("stainless work table");
            driver.findElement(By.xpath("//button[@class='btn btn-info banner-search-btn']")).click();

            // implicit wait to prevent synchronization problem
            driver.manage().timeouts().implicitlyWait(11, TimeUnit.SECONDS);

            //All the products in the page from search result
            List<WebElement> allTableProducts =driver.findElements(By.xpath("//div[@id='product_listing']//a[@class='description']"));

            int numberOfProducts = allTableProducts.size();
            System.out.println("number of products: "+ numberOfProducts);

            //to check every product for a search result having "Table" word in its title
            // I can use TestNG for Assert class and assertTrue method for shorter solution here

            for(int i= 0; i < allTableProducts.size();i++){

                if(allTableProducts.get(i).getText().contains("Table")){
                    System.out.println("Product "+ i + " Pass");
                }else{
                    System.out.println("Product "+ i+ " Fail");
                }
            }

            WebElement lastItem= allTableProducts.get(allTableProducts.size()-1);
            WebDriverWait wait = new WebDriverWait(driver,11);
            wait.until(ExpectedConditions.elementToBeClickable(lastItem));
            lastItem.click();

            WebElement addButton= driver.findElement(By.id("buyButton"));
            addButton.click();

            WebElement viewCartButton = driver.findElement(By.xpath("//button[contains(text(),'View Cart')]"));

            // I used JavascriptExecutor here to click the view cart button since other way it did not workout
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();",viewCartButton);

            WebElement emptyCartButton = driver.findElement(By.linkText("Empty Cart"));
            wait.until(ExpectedConditions.elementToBeClickable(emptyCartButton));
            emptyCartButton.click();

            // I use getWindowHandle method to switch to pop up window
            String parentWindowHandler= driver.getWindowHandle(); //parent window
            String subWindowHandler = null;

            Set<String> handles = driver.getWindowHandles(); // all window handles
            Iterator<String > iterator =handles.iterator();
            while(iterator.hasNext()){
                subWindowHandler=iterator.next();
            }

            driver.switchTo().window(subWindowHandler);
            Thread.sleep(3000);
            driver.findElement(By.xpath("//button[@class='btn btn-primary']")).click();
            driver.switchTo().window(parentWindowHandler);
            Thread.sleep(3000);
            String message = driver.findElement(By.className("header-1")).getText();
            Thread.sleep(3000);
            System.out.println(message); // to validate that selected item in the cart is empty

            driver.close();

        }

}
