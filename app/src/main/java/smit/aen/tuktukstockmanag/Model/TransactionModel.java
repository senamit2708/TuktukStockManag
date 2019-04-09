package smit.aen.tuktukstockmanag.Model;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class TransactionModel {

    String name;
    String num;
    String date;
    long quan;
    String remark;
    long trans;

    public TransactionModel(String name, String num, String date, long quan, String remark, long trans) {
        this.name = name;
        this.num = num;
        this.date = date;
        this.quan = quan;
        this.remark = remark;
        this.trans = trans;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getQuan() {
        return quan;
    }

    public void setQuan(long quan) {
        this.quan = quan;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getTrans() {
        return trans;
    }

    public void setTrans(long trans) {
        this.trans = trans;
    }
}
