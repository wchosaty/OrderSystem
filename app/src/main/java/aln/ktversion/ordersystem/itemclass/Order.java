package aln.ktversion.ordersystem.itemclass;

import java.io.PipedReader;
import java.util.List;

public class Order {
    public static final Integer
            STATUS_BUILD = 1, STATUS_WORKING = 2, STATUS_PRODUCT_FINISH = 3,
            STATUS_WAIT_PAY = 4, STATUS_END = 5, STATUS_CANCEL = 6;
    private String name,storeName;
    private List<Product> list;
    private Integer startTime, EndTime;
    private Integer id, status, waitTime;
    private String phone,address,email;
    private double totalPay;

    public Order() {
    }

}