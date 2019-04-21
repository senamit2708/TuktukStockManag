package smit.aen.tuktukstockmanag.Model;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ProductM{

    String name;
    String num;
    double bPrice;
    double sPrice;
    String desc;
    long quan;
    //for checking product is deleted or not
    boolean aval;
    String brand;
    String cat;

    public ProductM() {
    }

    public ProductM(String name, String num, double bPrice, double sPrice, String desc, long quan) {
        this.name = name;
        this.num = num;
        this.bPrice = bPrice;
        this.sPrice = sPrice;
        this.desc = desc;
        this.quan = quan;
    }

    public ProductM(String name, String num, double bPrice, double sPrice, String desc, long quan, String brand, String cat) {
        this.name = name;
        this.num = num;
        this.bPrice = bPrice;
        this.sPrice = sPrice;
        this.desc = desc;
        this.quan = quan;
        this.brand = brand;
        this.cat = cat;
    }

    public long getQuan() {
        return quan;
    }

    public void setQuan(long quan) {
        this.quan = quan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public double getbPrice() {
        return bPrice;
    }

    public void setbPrice(double bPrice) {
        this.bPrice = bPrice;
    }

    public double getsPrice() {
        return sPrice;
    }

    public void setsPrice(double sPrice) {
        this.sPrice = sPrice;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }
}
