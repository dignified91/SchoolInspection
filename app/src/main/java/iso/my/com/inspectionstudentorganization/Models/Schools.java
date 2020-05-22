package iso.my.com.inspectionstudentorganization.Models;

public class Schools {
    private String id;
    private String schoolname;
    private String region;
    private String status;
    private Boolean st;

    public Schools(String id, String schoolname, String region, String status, Boolean st) {
        this.id = id;
        this.schoolname = schoolname;
        this.region = region;
        this.status = status;
        this.st = st;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getSt() {
        return st;
    }

    public void setSt(Boolean st) {
        this.st = st;
    }

}
