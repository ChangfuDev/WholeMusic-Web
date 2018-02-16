package wholemusic.web.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Objects;

public class CommonUtils {
    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String humanReadableByteCount(long bytes) {
        return humanReadableByteCount(bytes, false);
    }

    public static HashMap<String, Object> getRequestInfoMap(HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("host", "" + request.getRemoteHost());
        map.put("localAddress", "" + request.getLocalAddr());
        map.put("remoteAddress", "" + request.getRemoteAddr());
        HashMap<String, String> headers = new HashMap<>();
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            final String name = names.nextElement();
            headers.put(name, request.getHeader(name));
        }
        map.put("headers", headers);
        return map;
    }

    public static boolean isRequestedByLocalHost(HttpServletRequest request) {
        String host = request.getHeader("host");
        return Objects.equals(request.getRemoteAddr(), request.getLocalAddr())
                && host != null && host.contains("localhost");
    }
}
