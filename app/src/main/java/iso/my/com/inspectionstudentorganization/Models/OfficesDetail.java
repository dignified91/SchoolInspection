package iso.my.com.inspectionstudentorganization.Models;


import iso.my.com.inspectionstudentorganization.OfficeDet.OfficeInspectionType;

public class OfficesDetail {

    private String officename;
    private String officeid;
    private String insurance;
    private String economic;
    private String phone;
    private String address;
    private String officetype;
    private String name;
    private String lastname;
    private String subno;
    private Boolean isselected_type;
    private Boolean isselected_ins;
    private Boolean isselected_ecocode;
    private Boolean isselected_address;
    private Boolean isselected_phone;
    private OfficeInspectionType officeInspectionType;

    public String getOfficename() {
        return officename;
    }

    public void setOfficename(String officename) {
        this.officename = officename;
    }

    public String getOfficeid() {
        return officeid;
    }

    public void setOfficeid(String officeid) {
        this.officeid = officeid;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getEconomic() {
        return economic;
    }

    public void setEconomic(String economic) {
        this.economic = economic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOfficetype() {
        return officetype;
    }

    public void setOfficetype(String officetype) {
        this.officetype = officetype;
    }

    public OfficeInspectionType getOfficeInspectionType() {
        return officeInspectionType;
    }

    public void setOfficeInspectionType(OfficeInspectionType officeInspectionType) {
        this.officeInspectionType = officeInspectionType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSubno() {
        return subno;
    }

    public void setSubno(String subno) {
        this.subno = subno;
    }

    public Boolean getIsselected_type() {
        return isselected_type;
    }

    public void setIsselected_type(Boolean isselected_type) {
        this.isselected_type = isselected_type;
    }

    public void setIsselected_ins(Boolean isselected_ins) {
        this.isselected_ins = isselected_ins;
    }

    public Boolean getIsselected_ecocode() {
        return isselected_ecocode;
    }

    public void setIsselected_ecocode(Boolean isselected_ecocode) {
        this.isselected_ecocode = isselected_ecocode;
    }

    public Boolean getIsselected_address() {
        return isselected_address;
    }

    public void setIsselected_address(Boolean isselected_address) {
        this.isselected_address = isselected_address;
    }

    public Boolean getIsselected_phone() {
        return isselected_phone;
    }

    public void setIsselected_phone(Boolean isselected_phone) {
        this.isselected_phone = isselected_phone;
    }

    public Boolean getIsselected_ins() {
        return isselected_ins;
    }
}
