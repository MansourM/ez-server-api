package ir.masterz.mansour.ez.serverapi;

import com.google.gson.JsonObject;

public class JsonBuilder {

    private JsonObject jo;

    public JsonBuilder() {
        clear();
    }

    public JsonBuilder(String key, String value) {
        clear();
        add(key,value);
    }
    public JsonBuilder(String key, boolean value) {
        clear();
        add(key,value);
    }

    public JsonBuilder add(String key, String value) {
        jo.addProperty(key, value);
        return this;
    }


    public JsonBuilder(String key, Number value) {
        clear();
        add(key,value);
    }

    public JsonBuilder add(String key, Number value) {
        jo.addProperty(key, value);
        return this;
    }

    public JsonBuilder add(String key, boolean value) {
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
