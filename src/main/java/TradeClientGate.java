import io.gate.gateapi.ApiClient;
//import io.gate.gateapi.ApiException;
import io.gate.gateapi.models.*;
import io.gate.gateapi.api.SpotApi;
import lombok.SneakyThrows;

import java.util.List;

public class TradeClientGate {
    String api_key = "3de3e276618cf71f30c43e6766748141";
    String secret_key = "9c4d119d0ecc8e9a1896875f05948536f4eb977e90b41ecc33b5da3593932354";
    SpotApi spotApi;
    String currencyPair;
    String currency;
    String orderAmount;
    Order order;
    float multi = 1.2f;

    public TradeClientGate () {
        currency = "USDT";
        orderAmount = "1";

        ApiClient client = new ApiClient();
        client.setApiKeySecret(api_key, secret_key);

        spotApi = new SpotApi(client);

        order = new Order();
        order.setAccount(Order.AccountEnum.SPOT);
        order.setAutoBorrow(false);
        order.setTimeInForce(Order.TimeInForceEnum.IOC);
        order.setType(Order.TypeEnum.LIMIT);
        order.setAmount(orderAmount);
        order.setSide(Order.SideEnum.BUY);

    }

    @SneakyThrows
    public void createOrder(String coin) {
        // get last price
        currencyPair = coin.replace('-', '_');
        List<Ticker> tickers = spotApi.listTickers().currencyPair(currencyPair).execute();
        assert tickers.size() == 1;
        String lastPrice = tickers.get(0).getLast();
        assert lastPrice != null;

        float tmp=Float.parseFloat(lastPrice);
        tmp *= multi;
        lastPrice = Float.toString(tmp);

        // create spot order
        order.setAmount(orderAmount);
        order.setPrice(lastPrice);
        order.setCurrencyPair(currencyPair);

        Order created = spotApi.createOrder(order);
        System.out.printf(Main.getCurrentTimeStamp()+ "order created with id %s, status %s\n", created.getId(), created.getStatus());

        while (!Order.StatusEnum.CLOSED.equals(created.getStatus())) {
            order.setPrice(lastPrice);
            tmp=Float.parseFloat(lastPrice);
            tmp *= multi;
            lastPrice = Float.toString(tmp);

            created = spotApi.createOrder(order);
            System.out.printf(Main.getCurrentTimeStamp() + "order created with id %s, status %s\n", created.getId(), created.getStatus());
        }
        System.out.println(Main.getCurrentTimeStamp() + "SUCCESS");
    }
}
