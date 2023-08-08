package ir.masterz.mansour.ez.serverapi;

public class UrlHelper {
    public final String url;
    public final int method;

    public UrlHelper(String url, int methodId) {
        this.url = url;
        method = methodId;
    }

    public UrlHelper(String url) {
        this.url = url;
        method = Request.Method.POST;
    }
}
