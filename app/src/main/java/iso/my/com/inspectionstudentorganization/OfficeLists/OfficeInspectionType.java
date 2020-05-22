package iso.my.com.inspectionstudentorganization.OfficeLists;

public enum OfficeInspectionType {

    INSPECTIONABLE(0,"Inspectionable","قابل بازرسی"),
    END_INSPECTION(1,"startInspection","پایان بازرسی");


    private int id;
    private String name;
    private String nameFa;

    OfficeInspectionType(int id, String name, String nameFa) {
        this.id = id;
        this.name = name;
        this.nameFa = nameFa;
    }

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

    public String getNameFa() {
        return nameFa;
    }

    public void setNameFa(String nameFa) {
        this.nameFa = nameFa;
    }

    public static OfficeInspectionType getTypeFromId(int id){

        for (OfficeInspectionType type : OfficeInspectionType.values()){
            if(type.getId() == id)
                return type;
        }

        return null;
    }
}
