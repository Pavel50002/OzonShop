package OzonTeam.ru;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import sun.misc.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.get;
import static org.apache.http.client.methods.RequestBuilder.post;

//import static io.restassured.module.webtestclient.RestAssuredWebTestClient.given;

//io.restassured.module.jsv.JsonSchemaValidator.*
//io.restassured.RestAssured.equalTo;
//        io.restassured.matcher.RestAssuredMatchers.*
 //       org.hamcrest.Matchers.*
//io.restassured.module.mockmvc.RestAssuredMockMvc.*
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class OzonMainTest extends TestBase {

String SessionId;


    @BeforeTest()
    public void requestSpec() {
        RestAssured.baseURI = "https://api.ozon.ru/";
    }

    @Test(description = "dd")
   public void auth() throws IOException {


        this.SessionId = given().when()
                .contentType("multipart/form-data")
                .multiPart("userName", "pavel50002@yandex.ru")
                .multiPart("password", "97I5k8f4321")
                .multiPart("grant_type", "password")
                .multiPart("client_id", "web")
                .multiPart("app_version", "browser-ozonshop")
                .post("oauth/v1/auth/token")
                .then()
                .body("token_type", equalTo("Bearer"))
                .log()
                .all().extract().path("access_token");
        // String  resource = generateStringFromResource(EndPoint.bodyozon);
     //  String stringResponse = message.path("access_token");
       // SessionId stringResponse = response.as(SessionId.class);



        given()
                .auth().oauth2(SessionId)
                .when()
                .get("https://api.ozon.ru/user/v5/")
                .then().log().all()
                .statusCode(200);
        System.out.println(SessionId);
        //  given().get("user/v5/").then().header("Authorization", stringResponse).log().all();

    }

    @Test(description = "dd")
    public void TEST()  {

        given()
                .auth().oauth2(SessionId)
                .when()
                .get("https://api.ozon.ru/user/v5/")
               .then().log().all();

    }





}



