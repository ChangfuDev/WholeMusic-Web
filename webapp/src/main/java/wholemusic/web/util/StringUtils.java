package wholemusic.web.util;

public class StringUtils {
    public static boolean areNotEmpty(String str1, String str2) {
        return isNotEmpty(str1) && isNotEmpty(str2);
    }

    /**
     * 支持可变参数的版本，当参数中的字符串所有都是非空字符串时，才返回true
     *
     * @param strings
     * @return
     */
    public static boolean areNotEmpty(String... strings) {
        if (strings != null) {
            for (String str : strings) {
                if (!isNotEmpty(str)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }
}
