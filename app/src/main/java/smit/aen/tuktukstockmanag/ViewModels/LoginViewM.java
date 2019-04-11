package smit.aen.tuktukstockmanag.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class LoginViewM extends AndroidViewModel {

    private static final String TAG = LoginViewM.class.getSimpleName();

    private boolean loginStatus = false;
//    private String className = null;
    private String mob = null;
//    private String roll = null;
    private String uId = null;
    private String name = null;
    private String pass = null;
    //user type
    private long uType = 0;



    public LoginViewM(@NonNull Application application) {
        super(application);
    }

    public boolean getLogin() {
        return loginStatus;
    }

    public void setLogin(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    public void setuType(long uType) {
        this.uType = uType;
    }

    //    public void setStudentDetail(String className, String mob, String roll, String uId, String name, String pass) {
//        this.className = className;
//        this.mob = mob;
//        this.roll = roll;
//        this.uId = uId;
//        this.name = name;
//        this.pass = pass;
//    }

    public String getMobileNo(){
        return mob;
    }

    public String getUserId() {
        return uId;
    }

    public String getResult() {
        return pass;
    }

    public long getuType() {
        return uType;
    }

    public void setUserDetail(String mob, String uId, String name, String pass, long uType) {
        this.mob = mob;
        this.uId = uId;
        this.name = name;
        this.pass = pass;
        this.uType = uType;
    }
}
