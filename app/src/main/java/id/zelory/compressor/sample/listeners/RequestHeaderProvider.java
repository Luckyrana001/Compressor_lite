package id.zelory.compressor.sample.listeners;

import java.util.HashMap;
import java.util.Map;

/* renamed from: id.zelory.compressor.sample.listeners.RequestHeaderProvider */
public class RequestHeaderProvider {
    public static final String AGENT = "User-agent";
    public static final String CONT_LANG = "Content-Language";
    public static final String CONT_TYP = "Content-Type";
    public static boolean IS_HTTPS = true;
    private Map<String, String> headers = new HashMap();

    public Map<String, String> getRequestHeaders() {
        if (!IS_HTTPS) {
            this.headers.put(CONT_LANG, "en");
            this.headers.put(AGENT, System.getProperty("http.agent"));
            this.headers.put(CONT_TYP, "application/json");
        }
        return this.headers;
    }

    public void addCustomHeader(String key, String values) {
        this.headers.put(key, values);
    }
}
