package dataTable;

import java.util.Date;

public class DataTableCourse {
    private String name;
    private String dateString;
    private Date date;


    public DataTableCourse(String name, String dateString) {
        this.name = name;
        this.dateString = dateString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateString() {
        return dateString;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
}
