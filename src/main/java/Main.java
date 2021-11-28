import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Objects;

public class Main {
    static TradeClientKucoin kucoin;
    static {
        try {
            kucoin = new TradeClientKucoin("7055debe-a3eb-428d-9e48-80aa07dafa58", "61a33d0bb8e9230001265e74", "5tartWitMe!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static ListingScraperBinance scraper;

    static {
        try {
            scraper = new ListingScraperBinance();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    static String coin;
    static String old_coin = "KEK";

    //мб из файла выгружать


    public Main() throws IOException {
    }

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {

        do {
            coin = scraper.get_coin();
        } while (Objects.equals(coin, old_coin));

        String symbol = coin + "-USDT";
        System.out.println(Calendar.getInstance().getTime() + " NEW COIN DETECTED: " + coin);

        kucoin.createBuyOrder(10L, symbol, "market", 1L);

        old_coin = coin;
    }
}