package iso.my.com.inspectionstudentorganization.Models;


public class Office {
    private String id;
    private String schoolname;
    private String status;

    public Office(String id, String schoolname, String status) {
        this.id = id;
        this.schoolname = schoolname;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
