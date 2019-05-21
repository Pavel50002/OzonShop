package OzonTeam.ru;

import Utils.RestUtil;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import sun.misc.IOUtils;
import sun.misc.Resource;

import java.io.File;
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
import static org.hamcrest.Matchers.in;

public class OzonMainTest extends RestUtil {


    @BeforeTest()
    public void requestSpec() {
        RestAssured.baseURI = "https://api.ozon.ru/";

    }

    @Test(priority = 1, description = "Авторизация, получение токена")
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
                .body("token_type", equalTo("Bearer"))
                .log()
                .all().extract().response();
        String stringResponse = response.path("access_token");
        RestUtil SessionId = new RestUtil();
        SessionId.setToken(stringResponse);
    }

    @Test(priority = 2, description = "Получение данных аккаунта после авторизации по Bearer Token")
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
    @Test(priority = 3, description = "Проверка статуса авторизации")
    public void StatusAuth() throws IOException{
        given()
                .auth().oauth2(Token)
                .contentType(ContentType.JSON)
                .get(EndPoint.StatusAuth)
                .then()
               .body("verified.forOrder", Matchers.is(true))
               .body("$.otp.active",Matchers.is(false))
                .statusCode(200)
                .log().all();
    }

    @Test(priority = 4, description = "Добавление товара в карзину")
    public void AddProductToCart() throws IOException {
        String JsonBody = generateStringFromResource(EndPoint.bodyozon);
        String IdProduct = JsonPath.given(JsonBody).getString("[0].id");
        given()
                .auth().oauth2(Token)
                .contentType(ContentType.JSON)
                .body(JsonBody)
                .when()
                .post(EndPoint.AddProductToCart)
                .then()
                .statusCode(200).log().all();

        System.out.println("Товар с id "+ IdProduct+" добавлен в карзину");
    }

@Test(priority = 5,description = "Проверка что лежит в карзине")
public void WhatProductToCart() throws IOException{

    given()
            .auth().oauth2(Token)
            .contentType(ContentType.JSON)
            .get(EndPoint.WhatProductToCar)
            .then()
                .body("[0].name",equalTo("Термос Biostal \"Flёr\", цвет: стальной, розовый, 500 мл"))
                .body("[0].price", equalTo(971.0000f))
                .body("[0].cart",equalTo("ozon"))
           .statusCode(200).log().all();

         Id = given()
                .auth().oauth2(Token)
                .contentType(ContentType.JSON)
                .get(EndPoint.WhatProductToCar).jsonPath().getString("[0].id");

}
@Test(priority = 6,description = "Удаление товара из корзины")
    private void DeleteProductToCart() throws IOException {
       String Idarrow = "["+Id+"]";
    System.out.println(Idarrow);
        given()
                .auth().oauth2(Token)
                .contentType(ContentType.JSON)
                .body(Idarrow)
                .when()
                .delete(EndPoint.DeleteProductToCart)
                .then()
                .statusCode(200)
                .log().all();
    System.out.println("Товар с Id "+Idarrow+" удален");
}


}



