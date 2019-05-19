package OzonTeam.ru;

import Utils.RestUtil;
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

public class OzonMainTest extends RestUtil {


    @BeforeTest()
    public void requestSpec() {
        RestAssured.baseURI = "https://api.ozon.ru/";

    }

    @Test(description = "Авторизация, получение токена")
    public void Auth() throws IOException {


        Response response = given().when()
                .contentType("multipart/form-data")
                .multiPart("userName", "pavel50002@yandex.ru")
                .multiPart("password", "97I5k8f4321")
                .multiPart("grant_type", "password")
                .multiPart("client_id", "web")
                .multiPart("app_version", "browser-ozonshop")
                .post(EndPoint.UrlToken)
                .then()
                //  .body("token_type", equalTo("Bearer"))
                .log()
                .all().extract().response();
        String stringResponse = response.path("access_token");
        RestUtil SessionId = new RestUtil();
        SessionId.setToken(stringResponse);
    }

    @Test(description = "Получение данных аккаунта после авторизации по Bearer Token")
    public void SetBearrerToken() throws IOException {

        given()
                .auth().oauth2(Token)
                .when()
                .get(EndPoint.Auth)
                .then()
                .body("email", equalTo("pavel50002@yandex.ru"))
                .body("loyaltyStatus.id", equalTo(5))
                .body("loyaltyStatus.premiumTypeId", equalTo(4))
                .statusCode(200).log().all();
        System.out.println(getToken());
    }


}



