package iso.my.com.inspectionstudentorganization.OfficeDet;

public enum OfficeInspectionType {

    Type(0,"type","نوع کاربری"),
    INSCODE(1,"inscode","شماره بیمه"),
    ADDRESS(2,"address","آدرس"),
    PHONE(3,"phone","تلفن"),
    ECOCODE(4,"ecocode","کد اقتصادی");


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
