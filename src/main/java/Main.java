import io.gate.gateapi.ApiException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Main {
    static TradeClientKucoin kucoin;
    static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    static Date date = new Date();
    static int mode = 1; //1 for gate; 0 for kucoin

    static {
        try {
            kucoin = new TradeClientKucoin("7055debe-a3eb-428d-9e48-80aa07dafa58", "61a33d0bb8e9230001265e74", "5tartWitMe!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static TradeClientGate gate;
    static {
        try {
            gate = new TradeClientGate();
        } catch (ApiException e) {
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

    public static void main(String[] args) throws IOException, InterruptedException, ApiException {

        do {
            coin = scraper.get_coin();
        } while (Objects.equals(coin, old_coin));

        String symbol = coin + "-USDT";
        System.out.println(dateFormat.format(date) + " NEW COIN DETECTED: " + coin);

        if (mode == 0) kucoin.createBuyOrder(10L, symbol, "market", 1L);
        else gate.createOrder(symbol);

        old_coin = coin;
    }
}