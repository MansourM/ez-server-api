package ir.masterz.mansour.ez.serverapi;

import com.google.gson.JsonObject;

public class JsonBuilder {

    private JsonObject jo;

    public JsonBuilder() {
        clear();
    }

    public JsonBuilder add(String key, String value) {
        jo.addProperty(key, value);
        return this;
    }

    public JsonObject build() {
        return jo;
    }

    public void clear() {
        jo = new JsonObject();
    }
}
