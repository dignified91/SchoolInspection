package iso.my.com.inspectionstudentorganization.Models;


import iso.my.com.inspectionstudentorganization.SchoolList.InspectionType;

public class ProForm {

    private int id;
    private String name;
    private String region;
    private InspectionType inspectionType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public InspectionType getInspectionType() {
        return inspectionType;
    }

    public void setInspectionType(InspectionType inspectionType) {
        this.inspectionType = inspectionType;
    }
}
