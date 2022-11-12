package aln.ktversion.ordersystem.itemclass;

public class Product {

    private Boolean titleFlag;
    private String name;
    private double price;
    private Integer waitTime,count,topThree,id;
    private String kind;

    public Product(Boolean titleFlag, String name, double price, Integer waitTime, Integer count, Integer topThree, Integer id, String kind) {
        this.titleFlag = titleFlag;
        this.name = name;
        this.price = price;
        this.waitTime = waitTime;
        this.count = count;
        this.topThree = topThree;
        this.id = id;
        this.kind = kind;
    }

    public Boolean getTitleFlag() {
        return titleFlag;
    }

    public void setTitleFlag(Boolean titleFlag) {
        this.titleFlag = titleFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Integer waitTime) {
        this.waitTime = waitTime;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getTopThree() {
        return topThree;
    }

    public void setTopThree(Integer topThree) {
        this.topThree = topThree;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
