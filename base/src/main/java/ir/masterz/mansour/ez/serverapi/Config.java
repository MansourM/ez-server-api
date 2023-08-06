package ir.masterz.mansour.ez.serverapi;

public class Config {
    private boolean LoggingEnabled = false;
    private String Tag = "ez_server_api";

    private boolean AllowDuplicateRequests = false;

    public void setLogging(boolean isEnabled) {
        LoggingEnabled = isEnabled;
    }

    public boolean loggingEnabled() {
        return LoggingEnabled;
    }

    public void setLoggingTag(String tag) {
        Tag = tag;
    }

    public String getLoggingTag() {
        return Tag;
    }

    public boolean allowDuplicateRequests() {
        return AllowDuplicateRequests;
    }

    public void setAllowDuplicateRequests(boolean allow) {
        AllowDuplicateRequests = allow;
    }

}
