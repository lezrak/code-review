class Calculator {

 def calculate(orderId: String, discount: Double): Double = {
    val response = scala.io.Source.fromURL("yourapi.com/api/v6/order/" + orderId).mkString
    if (response.isBlank) throw new RuntimeException("empty response body")
    val orderResponse = OrderResponse(response)
    var sum = orderResponse.productList.map((e: Product) => e.price * e.quantity).reduce((a, b) => a + b)
    if (sum > 1000) sum = sum * 0.9
    else if (sum > 100) sum = sum * 0.95
    if (orderResponse.accountType eq "NoAccountCustomer") sum = sum * 1.03
    else if (orderResponse.accountType eq "PremiumCustomer") sum = sum * 0.98
    else throw new RuntimeException("unknown customer")
    if (discount == null) sum
    else sum - discount
  }
}

case class OrderResponse(productList: List[Product], accountType: String)
object OrderResponse {
  def apply(json: String): OrderResponse = ??? //let's pretend this is manual deserialization
}

case class Product(price: Double, quantity: Double)