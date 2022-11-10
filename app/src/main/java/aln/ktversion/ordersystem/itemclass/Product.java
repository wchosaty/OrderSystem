package aln.ktversion.ordersystem.itemclass;

public class Product {
    public static final String GROUP_RICE = "飯",GROUP_NOODLE ="麵",GROUP_SOUP = "湯";
    public static final Integer RICE = 1,NOODLE =2,SOUP = 3;
    private Boolean titleFlag;
    private String name;
    private double price;
    private Integer waitTime,count,topThree,id;
    private String group;

    public Product(Boolean titleFlag, String name, double price, Integer waitTime, Integer count, Integer topThree, Integer id, String group) {
        this.titleFlag = titleFlag;
        this.name = name;
        this.price = price;
        this.waitTime = waitTime;
        this.count = count;
        this.topThree = topThree;
        this.id = id;
        this.group = group;
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
