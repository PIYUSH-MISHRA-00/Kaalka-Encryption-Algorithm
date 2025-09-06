package kaalka;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * KaalkaUtils: Utility functions for UTC time handling
 */
public class KaalkaUtils {
    public static String utcNow() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date());
    }
    public static Date utcNowDate() {
        return new Date();
    }
    public static Date parseUtc(String ts) throws Exception {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(ts);
    }
}
