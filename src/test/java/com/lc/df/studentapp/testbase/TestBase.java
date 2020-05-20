package com.lc.df.studentapp.testbase;

/* By Jitendra Patel */

import io.restassured.RestAssured;
import org.junit.BeforeClass;

public class TestBase {

    @BeforeClass
    public static void inIt(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port=8080;
        RestAssured.basePath = "/student";

    }
}