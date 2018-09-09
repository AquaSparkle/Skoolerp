package erp.skoolerp.Pojo;

/**
 * Created by shalu on 21/06/17.
 */

public class Details_POJO
{
    String due_date, issu_date, book_name, isbn, status, retn_date, req_date, b_status;
    boolean book_req;

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getIssu_date() {
        return issu_date;
    }

    public void setIssu_date(String issu_date) {
        this.issu_date = issu_date;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isBook_req() {
        return book_req;
    }

    public void setBook_req(boolean book_req) {
        this.book_req = book_req;
    }

    public String getRetn_date() {
        return retn_date;
    }

    public void setRetn_date(String retn_date) {
        this.retn_date = retn_date;
    }

    public String getReq_date() {
        return req_date;
    }

    public void setReq_date(String req_date) {
        this.req_date = req_date;
    }

    public String getB_status() {
        return b_status;
    }

    public void setB_status(String b_status) {
        this.b_status = b_status;
    }
}
