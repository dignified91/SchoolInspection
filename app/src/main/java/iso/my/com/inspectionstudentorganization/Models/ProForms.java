package iso.my.com.inspectionstudentorganization.Models;

public class ProForms {

    private String id;
    private String proname;
    private String manager;
    private String status;
    private Boolean st;

    public ProForms(String id, String proname, String manager, String status, Boolean st) {
        this.id = id;
        this.proname = proname;
        this.manager = manager;
        this.status = status;
        this.st = st;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProname() {
        return proname;
    }

    public void setProname(String proname) {
        this.proname = proname;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
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
