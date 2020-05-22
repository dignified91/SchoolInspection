package iso.my.com.inspectionstudentorganization.Models;

public class SpType {
    private String tId;
    private String tName;

    public SpType(String tId, String tName) {
        this.tId = tId;
        this.tName = tName;
    }

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }
}
