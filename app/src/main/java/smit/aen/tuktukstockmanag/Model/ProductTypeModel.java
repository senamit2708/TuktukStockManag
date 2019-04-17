package smit.aen.tuktukstockmanag.Model;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ProductTypeModel {

   String type;
   String cat;

    public ProductTypeModel(String type, String cat) {
        this.type = type;
        this.cat = cat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }
}
