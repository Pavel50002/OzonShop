package Utils;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RestUtil {


    public static String Token;
    public String getToken() {
        return Token;
    }
    public void setToken(String token) {
        this.Token = token;
    }
////////////////////////////////////////

    public static String Id;
    public String getId() {
        return Id;
    }
    public void setId(String id) {
        this.Id = id;
    }
///////////////////////////////////////


        public static String Uid;
        public String getUserIdIn() {
            return Uid;
        }
        public void setUserIdIn(String UserId) {
            this.Uid = UserId;
        }



    public String generateStringFromResource(String path) throws IOException {

        return new String(Files.readAllBytes(Paths.get(path)));

    }

}
