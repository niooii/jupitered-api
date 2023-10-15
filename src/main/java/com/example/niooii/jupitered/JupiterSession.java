package com.example.niooii.jupitered;

import com.example.niooii.chromedriver.ChromeDriverInit;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class JupiterSession {
    @JsonPropertyOrder(alphabetic=false)
    @JsonProperty(index=2)
    private HashMap<String, Double> grades = new HashMap<>();

    @JsonProperty(index=0)
    private String name;
    @JsonProperty(index=1)
    private final String osis;
    private final String password;
    private final String school;
    private final String city;
    private String state;
    private final WebDriver driver;
    private String source = "";
    @JsonProperty(index=3)
    HashMap<String, Integer> assignmentStats = new HashMap<>();
    @JsonProperty(index=6)
    private ArrayList<Course> courses;
    @JsonProperty(index=4)
    private ArrayList<Assignment> missingAssignments = new ArrayList<>();
    @JsonProperty(index=5)
    private ArrayList<Assignment> ungradedAssignments = new ArrayList<>();
    AtomicLong counter = new AtomicLong();

    //session constructor
    JupiterSession(String osis, String password) throws InterruptedException {
        this.osis = osis;
        this.password = password;
        this.school = "Bronx High School Of Science";
        this.city = "Bronx";
        this.state = "New York";
        driver =  new ChromeDriverInit().getChromeDriver();
        login();
    }

    public void login() throws InterruptedException {
        driver.get("https://login.jupitered.com/login/index.php");

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='text_studid1']")))
                .sendKeys(osis);
        driver.findElement(By.id("text_password1")).sendKeys(password);
        driver.findElement(By.id("text_school1")).sendKeys(school);
        driver.findElement(By.id("text_city1")).sendKeys(city);
        driver.findElement(By.id("region1_label")).click();
        Thread.sleep(120);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@val='us_ny']")))
                .click();
        driver.findElement(By.id("loginbtn")).click();
        name = driver.findElement(By.className("toptabnull")).getText();
        courses = retrieveCourses();
        getGrades();
        retrieveStats();
        quit();
    }

    private ArrayList<Course> retrieveCourses() {
        ArrayList<WebElement> navrows = (ArrayList<WebElement>) driver.findElements(By.className("classnav"));
        ArrayList<Course> holder = new ArrayList<>();
        for(int i = 0; i < navrows.size(); i++){
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='touchnavbtn']")))
                    .click();
            navrows = (ArrayList<WebElement>) driver.findElements(By.className("classnav")); //if i dont do this i get a stale element error .....
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(navrows.get(i)))
                    .click();
            holder.add(new Course(driver.getPageSource()));
        }
        return holder;
    }

    public String getName(){
        return name;
    }

    public ArrayList<Course> getCourses(){
        return courses;
    }

    private void retrieveStats(){
        int missing = 0;
        int ungraded = 0;
        int graded = 0;
        for(Course course : courses){
            for(Assignment assignment : course.getAssignments()){
                if(Objects.equals(assignment.getStatus(), "Missing")){
                    missingAssignments.add(assignment);
                    missing++;
                } else if(Objects.equals(assignment.getStatus(), "Ungraded")){
                    ungradedAssignments.add(assignment);
                    ungraded++;
                } else {
                    graded++;
                }
            }
        }
        assignmentStats.put("Total", missing + ungraded + graded);
        assignmentStats.put("Graded", graded);
        assignmentStats.put("Missing", missing);
        assignmentStats.put("Ungraded", ungraded);
    }

    public void getGrades(){
        for (Course x : courses) {
            grades.put(x.getName(), x.getGradeAverage());
        }
    }

    public void updateSource(){
        source = driver.getPageSource();
    }

    public void quit(){
        driver.manage().deleteAllCookies();
        driver.quit();
    }
}
