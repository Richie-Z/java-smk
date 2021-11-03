/**
 *
 * @author www.kangsunu.web.id
 */
public class UserSession {

    private static String u_username;
    private static String u_password;
   
    public static String getU_username() {
        return u_username;
    }

    public static void setU_username(String u_username) {
        UserSession.u_username = u_username;
    }

    public static String getU_password() {
        return u_password;
    }

    public static void setU_password(String u_password) {
        UserSession.u_password = u_password;
    }

    
}