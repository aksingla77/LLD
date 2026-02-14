
import java.util.HashMap;
import java.util.Map;

// WITHOUT BUILDER — Shows the problems
class HttpRequest {

    private String url;
    private String method;
    private Map<String, String> headers = new HashMap<>();
    private String body;
    private int timeout;

    // Telescoping constructors — grows exponentially with fields
    public HttpRequest(String url) {
        this(url, "GET", null, 30000);
    }

    public HttpRequest(String url, String method) {
        this(url, method, null, 30000);
    }

    public HttpRequest(String url, String method, String body, int timeout) {
        this.url = url;
        this.method = method;
        this.body = body;
        this.timeout = timeout;
    }

    public void addHeader(String k, String v) {
        headers.put(k, v);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "HttpRequest[" + method + " " + url + ", body=" + body + ", timeout=" + timeout + "ms]";
    }
}

public class WithoutBuilder {

    public static void main(String[] args) {

        // Problem 1: Unreadable — what is null? what is 5000?
        HttpRequest req1 = new HttpRequest("https://api.zerotechdebt.com", "POST", null, 5000);
        System.out.println(req1);

        // Problem 2: Can't skip middle params — want timeout but forced to pass method & body
        HttpRequest req2 = new HttpRequest("https://api.zerotechdebt.com", "GET", null, 3000);

        // Problem 3: Mutable — can be changed after creation
        HttpRequest req3 = new HttpRequest("https://api.zerotechdebt.com");
        req3.setUrl("https://evil.com"); // accidentally or intentionally mutated

        // Problem 4: No validation — invalid object can exist
        HttpRequest req4 = new HttpRequest(null); // URL is null, no error!
        System.out.println(req4);
    }
}
