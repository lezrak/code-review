

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Calculator {

 public double calculate(String orderId, Double discount) throws URISyntaxException, IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(new URI("yourapi.com/api/v6/order/" + orderId))
                .GET()
                .build();
        var client = HttpClient.newHttpClient();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.body() == null) {
            throw new RuntimeException("null response body");
        }
        var orderResponse = new OrderResponse(response.body());
        double sum = orderResponse.productList.stream().map(e -> e.price * e.quantity).reduce(0d, Double::sum);
        if (sum > 1000) {
            sum = sum * 0.9;
        } else if (sum > 100) {
            sum = sum * 0.95;
        }
        if (orderResponse.accountType == "NoAccountCustomer") {
            sum = sum * 1.03;
        } else if (orderResponse.accountType == "PremiumCustomer") {
            sum = sum * 0.98;
        } else {
            throw new RuntimeException("unknown customer");
        }
        if (discount == null) {
            return sum;
        } else {
            return sum - discount;
        }
    }
}

public class OrderResponse {
    List<Product> productList;
    String accountType;

    // let's pretend this is manual deserialization
    public OrderResponse(String json) {
        productList = new ArrayList<>();
        accountType = "";
    }
}

public class Product {
    double price;
    double quantity;
}