package com.ecoven.retrofit;

import android.app.Activity;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constant {

//    public static final String BASE_URL = "http://myspotbh.com/ecoven/webservice/"; http://technorizen.com/ecoven/
    public static final String BASE_URL = "http://technorizen.com/ecoven/webservice/";

    public static final String USER_INFO = "user_info";
    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public final static String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final String driver_id = "driver_id";
    public static final String IS_USER_LOGGED_IN = "IsUserLoggedIn";
    public static final String USER_ID = "userID";
    public static final int LOCATION_REQUEST = 1000;
    public static final int GPS_REQUEST = 1001;
    public static final String SELECTED_LANGUAGE = "selectedLanguage";

    public static boolean isValidEmail(CharSequence target) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(target);

        return matcher.matches();
    }

    public static void showToast(Activity context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
