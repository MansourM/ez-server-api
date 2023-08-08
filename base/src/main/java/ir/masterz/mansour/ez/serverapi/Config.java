package ir.masterz.mansour.ez.serverapi;

public class Config {
    private boolean LoggingEnabled = false;
    public static String TAG = "ez_server_api";

    private boolean AllowDuplicateRequests = false;

    public void setLogging(boolean isEnabled) {
        LoggingEnabled = isEnabled;
    }

    public boolean loggingEnabled() {
        return LoggingEnabled;
    }

    public void setLoggingTag(String tag) {
        TAG = tag;
    }

    public String getLoggingTag() {
        return TAG;
    }

    public boolean allowDuplicateRequests() {
        return AllowDuplicateRequests;
    }

    public void setAllowDuplicateRequests(boolean allow) {
        AllowDuplicateRequests = allow;
    }

}
