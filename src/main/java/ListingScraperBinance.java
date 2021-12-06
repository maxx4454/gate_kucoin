import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;

import static java.lang.System.currentTimeMillis;


public class ListingScraperBinance {
    static String link = "https://www.binancezh.com/bapi/composite/v1/public/cms/article/catalog/list/query?";
//    static String link = "https://www.binancezh.com/gateway-api/v1/public/cms/article/list/query?";
    static FileWriter myWriter;
    GetThread[] threads;
    HttpGet[] httpget;


    static URL resource = FileLoader.class.getClassLoader().getResource("parser.txt");
    static {
        try {
//            myWriter = new FileWriter(String.valueOf(resource));
//            myWriter = new FileWriter("/Users/max/IdeaProjects/gate_kucoin/target/classes/parser.txt");
            myWriter = new FileWriter("/home/ec2-user/gate_kucoin/target/classes/parser.txt");

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
            .setDefaultRequestConfig(RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.STANDARD).build())
            .setConnectionManager(cm)
            .build();


    static String[] urisToGet = {
            //delayed
            "https://www.binance.com/bapi/composite/v1/public/cms/article/catalog/list/query?",

            //status = dead
            "https://www.binancezh.top/bapi/composite/v1/public/cms/article/catalog/list/query?",

            //status = dead after 150 sec
            "https://api.yshyqxx.com/bapi/composite/v1/public/cms/article/catalog/list/query?",

            //status = dead
            "https://www.binancezh.cz/bapi/composite/v1/public/cms/article/catalog/list/query?",

            //status = alive???
            "https://www.binancezh.com/bapi/composite/v1/public/cms/article/catalog/list/query?"
    };

    public ListingScraperBinance() throws IOException {
        System.out.println(resource);
        threads = new GetThread[urisToGet.length];
        httpget = new HttpGet[urisToGet.length];
        for (int i = 0; i < threads.length; i++) {
            httpget[i] = new HttpGet(urisToGet[i]);
            threads[i] = new GetThread(httpClient, httpget[i], urisToGet[i]);
        }
    }


    static class GetThread extends Thread {

        private final CloseableHttpClient httpClient;
        private final HttpContext context;
        private final HttpGet httpget;
        String anCoin;
        String random_string;
        String rand_page_size;
        String random_number;
        Random RanNum = new Random();
        String link;

        public GetThread(CloseableHttpClient httpClient, HttpGet httpget, String link) {
            this.httpClient = httpClient;
            this.context = HttpClientContext.create();
            this.httpget = httpget;
            this.link = link;

        }



        @Override
        public void run() {

            do {
                try {
                    Thread.sleep(100);

                    random_string = RandomStringUtils.randomAlphanumeric(10);
                    random_number = String.valueOf(1 + RanNum.nextInt(99999999));
                    rand_page_size = String.valueOf(1 +  RanNum.nextInt(200));
                    String[] queries = new String[] {"type=1", "catalogId=48", "pageNo=1", "pageSize="+rand_page_size, "rnd="+ currentTimeMillis(),
                            random_string+ "=" +random_number};

                    String uri = link + queries[0] + "&" + queries[1]+ "&" + queries[2]+ "&" + queries[3]+ "&" + queries[4] + "&" + queries[5];

                    CloseableHttpResponse response = httpClient.execute(
                            new HttpGet(uri),
                            context);

                    String str = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
                    response.close();

                    anCoin = getCoin(str);


                    System.out.println(Main.getCurrentTimeStamp() + " PARSING");

                    if (!Objects.equals(anCoin, old_coin)){
                        writeToFile(anCoin);
                    }

                }  // Handle protocol errors
                catch (InterruptedException | IOException | StringIndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                    System.out.println(Main.getCurrentTimeStamp() + " LINK: " + link);
//                    System.out.println(Main.getCurrentTimeStamp() + str);
                    try {
                        Thread.sleep(300*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } while (Objects.equals(anCoin, old_coin));
        }

        public String FinalCoin() {
            return anCoin;
        }
    }

    void startMultipleThreads() throws InterruptedException {
        // create a thread for each URI
        // start the threads
        for (GetThread thread : threads) {
            thread.start();
        }
    }

    void joinMultipleThreads() throws InterruptedException {
//         join the threads
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
