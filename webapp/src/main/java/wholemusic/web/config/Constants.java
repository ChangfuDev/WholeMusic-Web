package wholemusic.web.config;

public class Constants {
    @SuppressWarnings("SpellCheckingInspection")
    public static final String WEIBO_CLIENT_ID = getOAuthConsts("WEIBO_CLIENT_ID");

    @SuppressWarnings("SpellCheckingInspection")
    public static final String WEIBO_CLIENT_SECRET = getOAuthConsts("WEIBO_CLIENT_SECRET");

    @SuppressWarnings("SpellCheckingInspection")
    public static final String WEIBO_CALLBACK = getOAuthConsts("WEIBO_CALLBACK");

    private static String getOAuthConsts(String field) {
        return getConst(getSamePackageClassName("OAuthConst"), field);
    }

    private static String getSamePackageClassName(String className) {
        String[] parts = Constants.class.getCanonicalName().split("\\.");
        parts[parts.length - 1] = className;
        final String result = String.join(".", parts);
        return result;
    }

    private static String getConst(String className, String field) {
        try {
            return (String) Class.forName(className).getDeclaredField(field).get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
