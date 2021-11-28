import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Calendar;

public class ListingScraperBinance {
    String coin;
    String announcement;
    static String link = "https://www.binance.com/bapi/composite/v1/public/cms/article/catalog/list/query?catalogId=48&pageNo=1&pageSize=15&rnd=";

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(link))
//                .headers("Content-Type", "text/plain;charset=UTF-8")
            .GET()
//                .POST(HttpRequest.BodyPublishers.ofString("GET"))
            .build();

    public ListingScraperBinance() throws URISyntaxException {
    }

    void get_announcement () throws URISyntaxException, IOException, InterruptedException {
        System.out.println(Calendar.getInstance().getTime() + " DEPLOYED");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        sendAsync(...)
        announcement = response.body();
        System.out.println(Calendar.getInstance().getTime() + " RESPONSE");
    }

    String get_coin () throws URISyntaxException, IOException, InterruptedException {
        get_announcement();
        int index = announcement.indexOf("Will List");

        boolean a = false;
        StringBuilder token = new StringBuilder();
        for (int i = index; i < index + 40; i++) {
            if (announcement.charAt(i) == '(') a = true;
            else if (a && !(announcement.charAt(i) == ')')){
                token.append(announcement.charAt(i));
            }
            if (announcement.charAt(i) == ')') return token.toString();
        }
        return null;
    }
}
