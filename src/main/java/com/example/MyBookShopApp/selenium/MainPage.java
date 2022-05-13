package com.example.MyBookShopApp.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
        String infamy ="24/02/2020";
        String[] dateDdMmYy = infamy.split("/");

        int yearDiff = Integer.parseInt(dateDdMmYy[2])- Calendar.getInstance().get(Calendar.YEAR);

        WebElement fromDate = driver.findElement(By.id("fromdaterecent"));

        fromDate.click();
        pause();

        WebElement previousLink;
        WebElement nextLink;
        WebElement midLink = driver.findElement(By.className("datepicker--nav-title"));

        midLink.click();
        pause();

        if(yearDiff!=0){
            if(yearDiff>0){
                for(int i=0;i< yearDiff;i++){
                    nextLink = driver.findElement(By.cssSelector("div[data-action='next']"));
                    nextLink.click();
                    Thread.sleep(1000);
                }
            }

            else if(yearDiff<0){
                for(int i=0;i< (yearDiff*(-1));i++){
                    previousLink = driver.findElement(By.cssSelector("div[data-action='prev']"));
                    previousLink.click();
                    Thread.sleep(1000);
                }
            }
        }

        List<WebElement> months = driver.findElements(By.className("datepicker--cell-month"));
        months.get(Integer.parseInt(dateDdMmYy[1])-1).click();
        pause();

        List<WebElement> days = driver.findElements(By.className("datepicker--cell-day"));

        for (WebElement day : days) {
            boolean date = day.getAttribute("data-date").equals(String.valueOf(Integer.parseInt(dateDdMmYy[0])));
            boolean month = day.getAttribute("data-month").equals(String.valueOf(Integer.parseInt(dateDdMmYy[1]) - 1));
            boolean year = day.getAttribute("data-year").equals(dateDdMmYy[2]);

            if (date && month && year) {
                day.click();
                break;
            }
        }

        return this;
    }

    public MainPage toPopular() {
        WebElement navigationPanel = driver.findElement(By.id("navigate"));
        WebElement popular = navigationPanel.findElement(By.cssSelector("a[href='/books/popular']"));
        popular.click();
        return this;
    }

    public MainPage choosePopular() {
        List<WebElement> books = driver.findElements(By.className("Card"));
        books.get(new Random().nextInt(books.size() - 1)).click();
        return this;
    }

    public MainPage toAuthors() {
        WebElement navigationPanel = driver.findElement(By.id("navigate"));
        WebElement popular = navigationPanel.findElement(By.cssSelector("a[href='/authors']"));
        popular.click();
        return this;
    }

    public MainPage toSomeAuthor() {
        List<WebElement> authors = driver.findElements(By.className("Authors-item"));
        authors.get(new Random().nextInt(authors.size() - 1)).findElement(By.tagName("a")).click();
        return this;
    }


//    private void setAttribute(WebElement element, String attName, String attValue) {
//        driver.executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);",
//                element, attName, attValue);
//
//        //        setAttribute(fromDate, "data-refreshfrom", "01.01.2021");
////        JavascriptExecutor js = driver;
////
////        js.executeScript("document.getElementById('fromdaterecent').value = '01.01.2020'");
////        pause();
//    }


}
