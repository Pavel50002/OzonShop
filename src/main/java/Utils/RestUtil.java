package Utils;



import com.jayway.restassured.RestAssured;
import com.jayway.restassured.filter.session.SessionFilter;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.*;

public class RestUtil {


    public static String Token;
    public String getToken() {
        return Token;
    }
    public void setToken(String token) {
        this.Token = token;
    }






}
