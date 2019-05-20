package Utils;



import com.jayway.restassured.RestAssured;
import com.jayway.restassured.filter.session.SessionFilter;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.jayway.restassured.RestAssured.*;

public class RestUtil {


    public static String Token;
    public String getToken() {
        return Token;
    }
    public void setToken(String token) {
        this.Token = token;
    }


    public static String Id;
    public String getId() {
        return Id;
    }
    public void setId(String id) {
        this.Id = id;
    }





    public String generateStringFromResource(String path) throws IOException {

        return new String(Files.readAllBytes(Paths.get(path)));

    }

}
