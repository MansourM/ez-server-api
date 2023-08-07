package ir.masterz.mansour.ez.serverapi;

public class RequestHelper {
    public String url;
    public int method;

    RequestHelper(String url, int methodId) {
        this.url = url;
        method = methodId;
    }
}
