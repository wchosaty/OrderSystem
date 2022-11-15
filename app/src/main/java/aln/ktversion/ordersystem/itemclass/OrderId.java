package aln.ktversion.ordersystem.itemclass;

import java.util.Date;

public class OrderId {
    private Long order_id;
    private String customer_name;
    private String status;
    private Date start_time;

    public OrderId(Long order_id, String customer_name, String status, Date start_time) {
        super();
        this.order_id = order_id;
        this.customer_name = customer_name;
        this.status = status;
        this.start_time = start_time;
    }
    public Long getOrder_id() {
        return order_id;
    }
    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }
    public String getCustomer_name() {
        return customer_name;
    }
    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Date getStart_time() {
        return start_time;
    }
    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }



}
