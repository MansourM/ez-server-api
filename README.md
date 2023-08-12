# ez-server-api
this is an WIP api, developed to make specific REST calls easy and fast to code.

# Installation:

### Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

### Step 2. Add the dependency

    dependencies {
	        implementation 'com.github.MansourM.ez-server-api:{$networkLibrary}:{$version}'
	}

##### Networking Libraries
"fan" uses [Fast Android Networking](https://github.com/amitshekhariitbhu/Fast-Android-Networking "Fast Android Networking") for networking

@deprecated
~"ion" uses[ ION](https://github.com/koush/ion " ION") for networking (more utility but needs google play service to work)~

##### Current Version
[![](https://jitpack.io/v/MansourM/ez-server-api.svg)](https://jitpack.io/#MansourM/ez-server-api) `0.5.6` !

#### examples

    implementation 'com.github.MansourM.ez-server-api:fan:0.5.6'
<strike>

    implementation 'com.github.MansourM.ez-server-api:ion:0.1.5

</strike>

# Examples
`G.java` application class
```java
public class G extends Application {

    public static Context APP_CONTEXT;
    public static ServerApi API;


    @Override
    public void onCreate() {
        super.onCreate();

        APP_CONTEXT = getApplicationContext();

        API = new ServerApi(APP_CONTEXT);
        API.getConfig().setLogging(true);
        API.getConfig().setLoggingTag("Mansour_API");
        API.setErrorMessageHandler(new MyApiCallbackDefaultHandler());

    }

    public static void message(String msg) {
        Toast.makeText(APP_CONTEXT, msg, Toast.LENGTH_SHORT).show();
    }

    private static class MyApiCallbackDefaultHandler extends BaseApi.CallbackDefaultHandler {

        @Override
        public void handleErrorMessage(String message, JsonObject data) {
            G.message("Default Error Message Handler, msg: " + message);
        }

        @Override
        public void handleFailure() {
            G.message("Connection Error!");
        }
    }


}


```

### Full kit

```java
G.API.request("http://192.168.1.22:8084/api/v1/app/ping")
                .setMethod(Request.Method.POST)
                .setRequestJason(new JsonBuilder("key1","value1").add("key2","value2").build())
                .setRequestTimeout(10) //seconds
                .setToken("public") //adds "token" to header
                .setCustomCallback(new SerfApiCallback() {

                    @Override
                    public void onSuccess(String message, JsonObject response) {
                        //response code is //200-300
                    }
                    @Override
                    public void onFailure() {
                        //no proper response
                        //e.g. no internet or parse error
                    }

                    @Override
                    public void onErrorMessage(String message, JsonObject response) {
                        //response code is //400-500
                    }

                    @Override
                    public void onResponse() {
                        //request in finished
                        //used for thins like loading(false)
                    }
                });

```
### Short form with helpers

`AR.java`

```java
//Api Request Helper class
public class AR {
    public static String getBaseIp() {
        return AppSettings.getInstance().getServerIp();
    }

    public static void setBaseIp(String baseIp) {
        AppSettings.getInstance().setServerIp(baseIp);
    }

    public static String getBasePort() {
        return AppSettings.getInstance().getServerPort();
    }

    public static void setBasePort(String port) {
        AppSettings.getInstance().setServerPort(port);
    }
    
    public static String baseUrl() {
        String Prefix = "https://";
        if (BuildConfig.DEBUG && AppSettings.getInstance().useHttpDebug())
            Prefix = "http://";
        return Prefix + getBaseIp() + ":" + getBasePort() + "/api/v1";
    }

    public static class App {
        public static String baseUrl() {
            return AR.baseUrl() + "/app";
        }

        public static UrlHelper ping() {
            return UrlHelper.get(baseUrl() + "/ping");
        }
    }
}

```

`SomeActivity.java`

```java
//There are custom callbacks based on what we need this one uses success callback (the only callback we can access in onSuccess, onError and onfailure use the default bahviour defined in G.java)
G.API.request(AR.App.ping())
                .setCustomCallback((message, response) -> {
                    
                });

```