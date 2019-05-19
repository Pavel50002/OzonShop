package OzonTeam.ru;


import org.testng.annotations.Test;

public class  FunctionalTest {
    //public class Message {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
        @Test()
    public  void  t (){
            System.out.println(getMessage());
        }
    }
