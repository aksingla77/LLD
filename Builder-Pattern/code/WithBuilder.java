
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// PRODUCT — immutable, package-private constructor
class HttpRequest {

    private final String url; //mandatory
    private final String method; //optional
    private final Map<String, String> headers; //optional
    private final String body; //optional
    private final int timeout; //optional

    // Package-private: accessible by HttpRequestBuilder (same package)
    HttpRequest(String url, String method, Map<String, String> headers, String body, int timeout) {
        this.url = url;
        this.method = method;
        this.headers = Collections.unmodifiableMap(headers);
        this.body = body;
        this.timeout = timeout;
    }

    public void showRequest() {
        System.out.println("HttpRequest[" + method + " " + url + ", headers=" + headers
                + ", body=" + (body != null ? body : "none") + ", timeout=" + timeout + "ms]");
    }
}

// BUILDER — separate class, fluent (returns this)
class HttpRequestBuilder {

    private String url;
    private String method = "GET";
    private Map<String, String> headers = new HashMap<>();
    private String body;
    private int timeout = 30000;

    public HttpRequestBuilder withUrl(String url) {
        this.url = url;
        return this;
    }

    public HttpRequestBuilder withMethod(String method) {
        this.method = method;
        return this;
    }

    public HttpRequestBuilder withHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public HttpRequestBuilder withBody(String body) {
        this.body = body;
        return this;
    }

    public HttpRequestBuilder withTimeout(int ms) {
        this.timeout = ms;
        return this;
    }

    public HttpRequest build() {
        if (url == null || url.isEmpty()) {
            throw new IllegalStateException("URL is required");
        }
        return new HttpRequest(url, method, headers, body, timeout);
    }
}

// DIRECTOR — bundles common configurations (optional)
class RequestDirector {

    public HttpRequest simpleGet(String url) {
        return new HttpRequestBuilder()
                .withUrl(url)
                .build();
    }

    public HttpRequest jsonPost(String url, String json) {
        return new HttpRequestBuilder()
                .withUrl(url)
                .withMethod("POST")
                .withHeader("Content-Type", "application/json")
                .withBody(json)
                .build();
    }

    public HttpRequest authenticatedRequest(String url, String token) {
        return new HttpRequestBuilder()
                .withUrl(url)
                .withHeader("Authorization", "Bearer " + token)
                .build();
    }
}

public class WithBuilder {

    public static void main(String[] args) {

        // Direct builder usage — readable, flexible
        HttpRequest req1 = new HttpRequestBuilder()
                .withUrl("https://api.zerotechdebt.com/users")
                .withMethod("POST")
                .withHeader("Content-Type", "application/json")
                .withBody("{\"name\": \"Akshit Singla\", \"age\": 25}")
                .withTimeout(5000)
                .build();
        req1.showRequest();

        // Order doesn't matter — same result
        HttpRequest req2 = new HttpRequestBuilder()
                .withTimeout(5000)
                .withMethod("POST")
                .withBody("{\"name\": \"Akshit Singla\", \"age\": 25}")
                // .withUrl("https://api.zerotechdebt.com/users")
                .build();
        req2.showRequest();

        {
            // Using Director for common patterns
            RequestDirector director = new RequestDirector();

            HttpRequest getReq = director.simpleGet("https://api.zerotechdebt.com/users");
            getReq.showRequest();

            HttpRequest postReq = director.jsonPost("https://api.zerotechdebt.com/users", "{\"id\": 1}");
            postReq.showRequest();
        }

        // Validation at build() — fails fast
        try {
            new HttpRequestBuilder().withMethod("GET").build(); // no URL
        } catch (IllegalStateException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }
}
