import com.kucoin.sdk.KucoinClientBuilder;
import com.kucoin.sdk.KucoinRestClient;
import com.kucoin.sdk.KucoinPrivateWSClient;
import com.kucoin.sdk.KucoinPublicWSClient;
import com.kucoin.sdk.rest.request.OrderCreateApiRequest;
import com.kucoin.sdk.rest.response.CurrencyResponse;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public class TradeClientKucoin {
    List<String> currList;
    KucoinRestClient kucoinRestClient;
    KucoinPrivateWSClient kucoinPrivateWSClient;
    KucoinPublicWSClient kucoinPublicWSClient;


    public TradeClientKucoin(String secret_api, String public_api, String pass_phrase) throws IOException {
        KucoinClientBuilder builder = new KucoinClientBuilder().withBaseUrl("https://api.kucoin.com").withApiKey(public_api, secret_api, pass_phrase);
        kucoinRestClient = builder.buildRestClient();
        kucoinPrivateWSClient = builder.buildPrivateWSClient();
        kucoinPublicWSClient = builder.buildPublicWSClient();
    }


    public void createBuyOrder(Long price, String symbol, String type, Long volume) throws IOException {
        String order_id = UUID.randomUUID().toString();
        OrderCreateApiRequest request = OrderCreateApiRequest.builder()
                .price(BigDecimal.valueOf(price)).funds(BigDecimal.valueOf(volume)).side("buy")
                .symbol(symbol).type(type).clientOid(order_id).build();
        kucoinRestClient.orderAPI().createOrder(request);
        System.out.println(Main.getCurrentTimeStamp() + " SUCCESSFULLY ORDERED");
    }


    public void getCurrenciesList() throws IOException {
        List<CurrencyResponse> currList = kucoinRestClient.currencyAPI().getCurrencies();
        for (CurrencyResponse c : currList){
            this.currList.add(c.getCurrency());
        }
    }

//    kucoinRestClient.timeAPI().getServerTimeStamp();

//    kucoinPrivateWSClient.onOrderActivate(response -> {
//        System.out.println(response);
//    }, "ETH-BTC", "KCS-BTC");

//    kucoinPrivateWSClient.onAccountBalance(response -> {
//        System.out.println(response);
//    });
}
