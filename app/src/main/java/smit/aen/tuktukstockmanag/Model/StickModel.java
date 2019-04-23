package smit.aen.tuktukstockmanag.Model;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class StickModel {

    private String note;
    private long code;
    private String docName;

    public StickModel(String note, long code) {
        this.note = note;
        this.code = code;
    }

    public StickModel(String note, long code, String docName) {
        this.note = note;
        this.code = code;
        this.docName = docName;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }
}
