package iso.my.com.inspectionstudentorganization.SchoolList;

public enum InspectionType {

    INSPECTIONABLE(0,"Inspectionable","قابل بازرسی"),
    START_INSPECTION(1,"startInspection","شروع بازرسی"),
    END_INSPECTION(2,"endInspection","پایان بازرسی");

    private int id;
    private String name;
    private String nameFa;

    InspectionType(int id, String name, String nameFa) {
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

    public static InspectionType getTypeFromId(int id){

        for (InspectionType type :InspectionType.values()){
            if(type.getId() == id)
                return type;
        }

        return null;
    }
}
