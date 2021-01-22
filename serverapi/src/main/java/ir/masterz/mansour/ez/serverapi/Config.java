package ir.masterz.mansour.ez.serverapi;

class Config {
    private boolean LoggingEnabled = false;
    private String Tag = "ez_server_api";

    private int RetryCount = 3;
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

    public void setRetryCount(int retryCount) {
        RetryCount = retryCount;
    }

    public int getRetryCount() {
        return RetryCount;
    }

    public boolean allowDuplicateRequests() {
        return AllowDuplicateRequests;
    }

    public void setAllowDuplicateRequests(boolean allow) {
        AllowDuplicateRequests = allow;
    }

}
