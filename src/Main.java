import edu.aitu.oop3.models.Order;
import edu.aitu.oop3.models.OrderItem;
import edu.aitu.oop3.services.OrderService;

public class Main {

    public static void main(String[] args) {

        OrderService service = new OrderService();

        try {
            Order order = new Order(1, 1);

            // menu_item_id = 1, quantity = 2, price at order = 100 pizza
            order.addItem(new OrderItem(1, 0, 2, 3, 2500));

            // menu_item_id = 2, quantity = 1, price at order = 150 burger
            order.addItem(new OrderItem(2, 0, 1, 3, 1200));

            // menu_item_id = 2, quantity = 1, price at order = 150 ice cream
            try {
                System.out.println("Placing order...");
                Order saved = service.placeOrder(order);

                System.out.println("\nActive orders:");
                service.viewActiveOrders().forEach(System.out::println);

                System.out.println("\nCompleting order...");
                service.completeOrder(saved.getId());

                order.addItem(new OrderItem(2, 0, 4, 1, 300));
            } catch (Exception e) {
                System.err.println("ERROR adding ice cream: " + e.getMessage());
                System.err.println("Order will be placed without ice cream.");
            }


        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
