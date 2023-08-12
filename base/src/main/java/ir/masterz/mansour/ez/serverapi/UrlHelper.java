package ir.masterz.mansour.ez.serverapi;

public class UrlHelper {
    public final String url;
    public final int method;

    public UrlHelper(String url, int methodId) {
        this.url = url;
        method = methodId;
    }

    public static UrlHelper get(String url) {
        return new UrlHelper(url, Request.Method.GET);
    }

    public static UrlHelper post(String url) {
        return new UrlHelper(url, Request.Method.POST);
    }

    public static UrlHelper delete(String url) {
        return new UrlHelper(url, Request.Method.DELETE);
    }

    public static UrlHelper put(String url) {
        return new UrlHelper(url, Request.Method.PUT);
    }

    public static UrlHelper patch(String url) {
        return new UrlHelper(url, Request.Method.PATCH);
    }
}
