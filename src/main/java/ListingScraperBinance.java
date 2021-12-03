import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
//import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


public class ListingScraperBinance {
    static String link = "https://www.binancezh.top/bapi/composite/v1/public/cms/article/catalog/list/query?catalogId=48&pageNo=1&pageSize=15";
    static FileWriter myWriter;
    GetThread[] threads;
    HttpGet httpget;

    static {
        try {
            myWriter = new FileWriter("/src/main/resources/parser.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String old_coin;
    static {
        try {
            old_coin = FileLoader.readFile("old_coin.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    CloseableHttpClient httpClient = HttpClients.custom()
            .setConnectionManager(cm)
            .build();


    static String[] urisToGet = {
            //1 delayed
//            "https://www.binance.com/bapi/composite/v1/public/cms/article/catalog/list/query?catalogId=48&pageNo=1&pageSize=15&rnd=",

            "https://www.binancezh.top/bapi/composite/v1/public/cms/article/catalog/list/query?catalogId=48&pageNo=1&pageSize=15",
            "https://api.yshyqxx.com/bapi/composite/v1/public/cms/article/catalog/list/query?catalogId=48&pageNo=1&pageSize=15",
            "https://www.binancezh.cz/bapi/composite/v1/public/cms/article/catalog/list/query?catalogId=48&pageNo=1&pageSize=15",
            "https://www.binancezh.com/bapi/composite/v1/public/cms/article/catalog/list/query?catalogId=48&pageNo=1&pageSize=15"
    };

    public ListingScraperBinance() throws IOException {
        threads = new GetThread[urisToGet.length];
        for (int i = 0; i < threads.length; i++) {
            httpget = new HttpGet(urisToGet[i]);
            threads[i] = new GetThread(httpClient, httpget);
        }
    }


    static class GetThread extends Thread {

        private final CloseableHttpClient httpClient;
        private final HttpContext context;
        private final HttpGet httpget;
        String coin;

        public GetThread(CloseableHttpClient httpClient, HttpGet httpget) {
            this.httpClient = httpClient;
            this.context = HttpClientContext.create();
            this.httpget = httpget;
        }

        @Override
        public void run() {
            try {
                CloseableHttpResponse response = httpClient.execute(
                        httpget, context);
                do {
                    try {
                        HttpEntity entity = response.getEntity();
                        String str = IOUtils.toString(entity.getContent(), StandardCharsets.UTF_8);
                        coin = getCoin(str);
                        if (!Objects.equals(coin, old_coin)){
                            writeToFile(coin);
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        response.close();
                    }
                } while (Objects.equals(coin, old_coin));

            } catch (ClientProtocolException ex) {
                // Handle protocol errors
            } catch (IOException ex) {
                // Handle I/O errors
            }
        }
        public String FinalCoin() {
            return coin;
        }

    }

    void getMultipleAnnouncements() throws InterruptedException {
        // create a thread for each URI
        // start the threads
        for (GetThread thread : threads) {
            thread.start();
        }

        // join the threads
        for (GetThread thread : threads) {
            thread.join();
        }
    }


    void getAnnouncement() throws IOException, InterruptedException {
        System.out.println(Main.getCurrentTimeStamp()  + " DEPLOYED");

//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        sendAsync(...)
//        announcement = response.body();
        System.out.println(Main.getCurrentTimeStamp() + " RESPONSE");
    }


    static String getCoin(String announcement) throws IOException, InterruptedException {
//        getAnnouncement();
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

    static void writeToFile (String s) throws IOException {
        myWriter.write(s);
        myWriter.close();
    }
}
