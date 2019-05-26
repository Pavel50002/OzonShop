package OzonTeam.ru;

import Utils.RestUtil;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.jayway.restassured.response.Cookies;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.client.methods.RequestBuilder;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import sun.misc.IOUtils;
import sun.misc.Resource;

import java.io.*;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
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


      Response response =  given()
                .auth().oauth2(Token)
                .when()
                .get(EndPoint.Auth)
                .then()
              .body("userId", Matchers.is(19162203))
                .body("isLoggedIn", equalTo(true))
                .body("loyaltyStatus.id", equalTo(5))
                .body("loyaltyStatus.premiumTypeId", equalTo(4))
                .statusCode(200)
                .log().all().extract().response();
      String IdUser = response.path("userId").toString();
      RestUtil IDuser1 = new RestUtil();
        IDuser1.setUserIdIn(IdUser);
      String Email = response.path("email");
      RestUtil SetEmail = new RestUtil();
        SetEmail.setUserEmail(Email);

        System.out.println("Id пользователя " + Uid);


        System.out.println("Токен авторизации "+getToken());


        }
    @Test(priority = 3, description = "Проверка статуса авторизации")
    public void StatusAuth() throws IOException{
        given()
                .auth().oauth2(Token)
                .contentType(JSON)
                .get(EndPoint.StatusAuth)
                .then()
                .body("verified.forOrder", Matchers.is(true))
                .body("otp.active",Matchers.is(false))
                .statusCode(200)
                .log().all();
    }

    @Test(priority = 4, description = "Получение id пользователя который авторизовался")
    private void GetIdUser() throws IOException{
        given()
                .auth().oauth2(Token)
                .contentType(JSON)
                .param("clientId", Uid)
                .get(EndPoint.GetIdUser)
                .then()
                .body("fio",equalTo("ERSHOVA Pavel ggr"))
                .body("age", equalTo(29))
                .statusCode(200)
                .log().all();

    }

    @Test(priority = 5, description = "Добавление товара в корзину")
    public void AddProductToCart() throws IOException {
        String JsonBody = generateStringFromResource(EndPoint.bodyozon);
        String IdProduct = JsonPath.given(JsonBody).getString("[0].id");
        given()
                .auth().oauth2(Token)
                .contentType(JSON)
                .body(JsonBody)
                .when()
                .post(EndPoint.AddProductToCart)
                .then()
                .statusCode(200).log().all();

        System.out.println("Товар с id "+ IdProduct+" добавлен в корзину");
    }

@Test(priority = 6,description = "Проверка что лежит в корзине")
public void WhatProductToCart() throws IOException{

    given()
            .auth().oauth2(Token)
            .contentType(JSON)
            .get(EndPoint.WhatProductToCar)
            .then()
                .body("[0].name",equalTo("Термос Biostal \"Flёr\", цвет: стальной, розовый, 500 мл"))
                .body("[0].price", equalTo(1124.0000f))
                .body("[0].cart",equalTo("ozon"))
           .statusCode(200).log().all();

         Id = given()
                .auth().oauth2(Token)
                .contentType(JSON)
                .get(EndPoint.WhatProductToCar).jsonPath().getString("[0].id");

}


@Test(priority = 7,description = "Удаление товара из корзины")
    private void DeleteProductToCart() throws IOException {
       String Idarrow = "["+Id+"]";
    System.out.println(Idarrow);
        given()
                .auth().oauth2(Token)
                .contentType(JSON)
                .body(Idarrow)
                .when()
                .delete(EndPoint.DeleteProductToCart)
                .then()
                .statusCode(200)
                .log().all();
    System.out.println("Товар с Id "+Idarrow+" удален");
}
@Test(priority = 8 , description = "Проверка есть ли Email")
    private  void SearchEmail() {
        given()
                .auth().oauth2(Token)
                .contentType(JSON)
                .when()
                .param("login", Eamil)
                .get(EndPoint.SearchEmail)
                .then()
                .body("status", equalTo("found"))
                .statusCode(200)
                .log().all();
}

@Test(priority = 9, description = "Получаем категорию товаров в .txt сохраняем его в файл в формате .json читаем и создаем assert")
    private void CategoryProduct() throws IOException {

      Response Nout = given()
                .param("context","ozon")
                .param("abgroup", "3")
                .param("areaid", "2")
                .contentType(JSON)
                .get(EndPoint.CategoryProduct)

                .then().statusCode(200)
             //   .body("categories.[0].categories.[1].categories.[1].title",equalTo("Игровые ноутбуки"))
                .extract().response();

      String comp = Nout.asString();
    FileWriter data = new FileWriter("C:/ProjectIDEA/Ozon/src/test/resources/Noutbook.json");
    data.write(comp);
    data.flush();
    data.close();



//    FileReader reader = new FileReader("C:\\ProjectIDEA\\Ozon\\src\\test\\resources\\Noutbook.json");
//    reader.read();
//    reader.toString();
//    System.out.println(reader);

    String JsonBody = generateStringFromResource(EndPoint.Noutbook);
    String Noutbook = JsonPath.given(JsonBody).getString("categories[0].categories[1].categories[1]");

    System.out.println(Noutbook);
assertThat(Noutbook, equalTo("[id:30601, title:Игровые ноутбуки, url:/category/igrovye-noutbuki-15821/]"));

}

@Test(priority = 10, description = "Определение местоположения клиента по ip адресу")
    private void IpAdres () throws IOException {

            given()
                    .auth().oauth2(Token)
                    .accept(JSON)
                    .param("ip","95.165.68.169")
                    .get(EndPoint.Ipaddres)
                    .then()
                    .body("current.name",equalTo("Москва"))
                    .statusCode(200)
                    .log().all();

}

    @Test(priority = 11, description = "Ищем на Ozon asus+zenfone+max+m2")
    private void SearchOzon () throws IOException {
String Url = "asus+zenfone+max+m2";
       Response Idprodyct  = given()
                .auth().oauth2(Token)
                .accept(JSON)
                .param("url", "/searchSuggestions/?text="+Url+"&url=/")
                .get("composer-api.bx/page/json/v1")
                .then()
                .statusCode(200)
                .log().all().extract().response();
        String stringResponse = Idprodyct.path("catalog.searchSuggestions.searchSuggestions-3-default-1.items[5].link");
       RestUtil ItemsId = new RestUtil();
        RestUtil SessionId = new RestUtil();
        SessionId.setIdprod(stringResponse);
        System.out.println(Idprod);
    }
}



