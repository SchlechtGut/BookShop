package com.example.MyBookShopApp.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class MainPage {

    private String url = "http://localhost:8085/";
    private ChromeDriver driver;

    public MainPage(ChromeDriver driver) {
        this.driver = driver;
    }

    public MainPage callPage() {
        driver.get(url);
        return this;
    }


    public MainPage pause() throws InterruptedException {
        Thread.sleep(2000);
        return this;
    }

    public MainPage setUpSearchToken(String token) {
        WebElement element = driver.findElement(By.id("query"));
        element.sendKeys(token);
        return this;
    }

    public MainPage submit() {
        WebElement element = driver.findElement(By.id("search"));
        element.submit();
        return this;
    }

    public MainPage toGenres() {
        WebElement navigationPanel = driver.findElement(By.id("navigate"));
        WebElement genresUrl = navigationPanel.findElement(By.cssSelector("a[href='/genres']"));
        genresUrl.click();
        return this;
    }

    public MainPage toSomeGenre() {
        WebElement someTag = driver.findElement(By.className("Tag")).findElement(By.tagName("a"));
        someTag.click();
        return this;
    }

    public MainPage toNew() {
        WebElement navigationPanel = driver.findElement(By.id("navigate"));
        WebElement recentUrl = navigationPanel.findElement(By.cssSelector("a[href='/books/recent']"));
        recentUrl.click();
        return this;
    }

    public MainPage changeDate() throws InterruptedException {
        WebElement fromDate = driver.findElement(By.id("fromdaterecent"));

        fromDate.click();

//        fromDate.sendKeys("20150612");

//        setAttribute(fromDate, "data-refreshfrom", "01.01.2021");

        JavascriptExecutor js = driver;

        js.executeScript("document.getElementById('fromdaterecent').value = '01.01.2020'");
        pause();

        WebElement form = driver.findElement(By.className("Section-header-form"));
        form.submit();

//        js.executeScript("document.getElementById('fromdaterecent').setAttribute('data-refreshfrom', '01.01.2021')");
        return this;
    }

    private void setAttribute(WebElement element, String attName, String attValue) {
        driver.executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);",
                element, attName, attValue);
    }
}
