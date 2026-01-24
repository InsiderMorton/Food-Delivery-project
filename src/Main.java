import services.OrderService;
import exceptions.*;
import java.util.HashMap;
import java.util.Map;


public class Main {
    public static void main(String[] args) {
        OrderService service = new OrderService();

        try {
            // 1. Формируем корзину (ID блюда -> Количество)
            Map<Integer, Integer> cart = new HashMap<>();
            cart.put(1, 2); // 2 Бургера (ID=1)
            cart.put(2, 1); // 1 Пицца (ID=2)

            // 2. Делаем заказ для клиента с ID=1
            System.out.println("1. Placing order...");
            service.placeOrder(1, cart);

            // 3. Смотрим активные заказы
            System.out.println("\n2. Viewing active orders...");
            service.viewActiveOrders();

            // 4. Завершаем заказ (укажите ID, который создался на шаге 2, например 1)
            System.out.println("\n3. Completing order...");
            service.completeOrder(1);

            // 5. Проверяем исключение (заказываем мороженое ID=4, которого нет)
            System.out.println("\n4. Testing Exception...");
            Map<Integer, Integer> invalidCart = new HashMap<>();
            invalidCart.put(4, 1);
            service.placeOrder(1, invalidCart);

        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}