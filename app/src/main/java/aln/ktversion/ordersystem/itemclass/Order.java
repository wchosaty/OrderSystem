package aln.ktversion.ordersystem.itemclass;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {
    public static final String STATUS_BUILD = "成立", STATUS_WORKING = "製作中", STATUS_PRODUCT_FINISH = "完成",
            STATUS_WAIT_PAY = "未付款", STATUS_END = "結單", STATUS_CANCEL = "取消";
    private String customerName;
    private List<Product> list;
    private Date startTime, EndTime;
    private Long order_id;
    private String status;
    private double totalPay;

    public Order() {
    }

    public Order(String customerName, List<Product> list, Date startTime, Date endTime, Long order_id, String status, double totalPay) {
        this.customerName = customerName;
        this.list = list;
        this.startTime = startTime;
        EndTime = endTime;
        this.order_id = order_id;
        this.status = status;
        this.totalPay = totalPay;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<Product> getList() {
        return list;
    }

    public void setList(List<Product> list) {
        this.list = list;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return EndTime;
    }

    public void setEndTime(Date endTime) {
        EndTime = endTime;
    }

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(double totalPay) {
        this.totalPay = totalPay;
    }
}