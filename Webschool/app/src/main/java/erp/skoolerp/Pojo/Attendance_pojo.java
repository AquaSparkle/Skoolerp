package erp.skoolerp.Pojo;

/**
 * Created by shalu on 19/06/17.
 */

public class Attendance_pojo
{
    String date, reason, month, descrp, eve_date;
    boolean absent;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public boolean isAbsent() {
        return absent;
    }

    public void setAbsent(boolean absent) {
        this.absent = absent;
    }

    public String getDescrp() {
        return descrp;
    }

    public void setDescrp(String descrp) {
        this.descrp = descrp;
    }

    public String getEve_date() {
        return eve_date;
    }

    public void setEve_date(String eve_date) {
        this.eve_date = eve_date;
    }
}
