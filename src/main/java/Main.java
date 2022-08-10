import io.gate.gateapi.ApiException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class Main {
    static TradeClientKucoin kucoin;
    static int mode = 0; //1 for gate; 0 for kucoin

    static {
        try {
            kucoin = new TradeClientKucoin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static TradeClientGate gate;
    static {
        gate = new TradeClientGate();
    }

    static ListingScraperBinance scraper;

    static {
        try {
            scraper = new ListingScraperBinance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String coin;
    static String old_coin;

    static {
        try {
            old_coin = FileLoader.readFile("old_coin.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static String getCurrentTimeStamp() {
        Date now = new Date();
        return sdf.format(now);
    }

    public static void main(String[] args) throws IOException, InterruptedException, ApiException {
        System.out.println(getCurrentTimeStamp() + " oldcoin is: " + old_coin);

        scraper.startMultipleThreads();

        do {
            coin = FileLoader.readFile("parser.txt");
        } while (Objects.equals(null, coin) || Objects.equals(old_coin, coin));

        String symbol = coin + "-USDT";
        System.out.println(getCurrentTimeStamp()  + " NEW COIN DETECTED: " + coin);

        if (mode == 0) kucoin.createBuyOrder(10L, symbol, "market", 1000L);
        else gate.createOrder(symbol);

        System.out.println(getCurrentTimeStamp()  + " ORDERED: " + coin);
        scraper.joinMultipleThreads();

        old_coin = coin;
//        FileLoader.writeFile(coin);


    }
}
