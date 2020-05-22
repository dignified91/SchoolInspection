package iso.my.com.inspectionstudentorganization.Models;

public class DriverData {

    private String id;
    private String name;
    private String family;
    private String Mobile;
    private String region;
    private String Personelcode;
    private String Image;
    private String Desc;

    public DriverData(String id, String name, String family, String mobile, String region, String personelcode, String image, String desc) {
        this.id = id;
        this.name = name;
        this.family = family;
        this.Mobile = mobile;
        this.region = region;
        this.Personelcode = personelcode;
        this.Image = image;
        this.Desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPersonelcode() {
        return Personelcode;
    }

    public void setPersonelcode(String personelcode) {
        Personelcode = personelcode;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }
}
