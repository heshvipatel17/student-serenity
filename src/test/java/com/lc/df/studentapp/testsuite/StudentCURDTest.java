package com.lc.df.studentapp.testsuite;

/* By Jitendra Patel */

import com.lc.df.studentapp.model.StudentPojo;
import com.lc.df.studentapp.testbase.TestBase;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.lc.df.studentapp.utils.TestUtils.getRandomValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasValue;


@RunWith(SerenityRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class StudentCURDTest extends TestBase {

    static String firstName = "Jitu" + getRandomValue();
    static String lastName = "Patel" + getRandomValue();
    static String programme = "Student App Testing";
    static String email = "xyz" + getRandomValue() + "@gmail.com";
    static int studentId;

    @Title("This test will create a new student")
    @Test
    public void test001() {

        List<String> courses = new ArrayList<>();
        courses.add("Serenity");
        courses.add("RestAssrued");
        courses.add("JAVA");

        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courses);

        SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json")
 //               .contentType(ContentType.JSON)
                .log()
                .all()

                .when()
                .body(studentPojo)
                .post()

                .then()
                .log()
                .all()
                .statusCode(201);

    }

    @Title("Verify if the student was added to the application")
    @Test
    public void test002() {

        //declared these as variable to avoid repeating common code while declaring path
        String p1 = "findAll{it.firstName=='";
        String p2 = "'}.get(0)";

        HashMap<String, Object> value = SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json")
                //               .contentType(ContentType.JSON)
                .when()
                .get("/list")
                .then()
                .statusCode(200)
                .extract()
                .path(p1 + firstName + p2);

        //verifying that the student was added
        assertThat(value, hasValue(firstName));

        //casting studentId to int as value returns a object to use as end point for the other tests in end to end tests
        studentId = (int) value.get("id");
    }

    @Title("Update the user information and verify the updated information")
    @Test
    public void test03() {

        String p1 = "findAll{it.firstName=='";
        String p2 = "'}.get(0)";

        //updated the existing 1st name as its on end to end test
        firstName = firstName + "_PutRequest";

        List<String> courses = new ArrayList<>();
        courses.add("JAVA");
        courses.add("API");

        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courses);

        SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json")
                //               .contentType(ContentType.JSON)
                .log()
                .all()
                .when()
                .body(studentPojo)
                .put("/" + studentId)
                .then()
                .log()
                .all()
                .statusCode(200);

        HashMap<String, Object> value = SerenityRest.rest()
                .given()
                .when()

                //verify that the student record is updated by the above request from this test
                .get("/list")
                .then()
                .statusCode(200)
                .extract()
                .path(p1 + firstName + p2);
        System.out.println(value);
        assertThat(value, hasValue(firstName));
    }

    @Title("Delete the student and verify if the student is deleted!")
    @Test
    public void test04(){
        SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json")
                //               .contentType(ContentType.JSON)
                .when()
                .delete("/"+studentId)

                .then()
                .statusCode(204);

        SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json")
                //               .contentType(ContentType.JSON)

                .when()
                .get("/"+studentId)

                .then()
                .statusCode(404);
    }
}