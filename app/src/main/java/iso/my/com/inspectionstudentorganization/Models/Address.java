package iso.my.com.inspectionstudentorganization.Models;

public class Address {

    private String neighbourhood;
    private String formatted_address;
    private String municipality_zone;
    //  private String route_name;
    //   private String route_type;
    private String in_traffic_zone;
    private String in_odd_even_zone;
    private String city;
    private String state;
    //  private String place;
    // private String addresses;

    public Address(String neighbourhood, String formatted_address, String municipality_zone, String in_traffic_zone, String in_odd_even_zone, String city, String state) {


        //  this.route_name = route_name;
        // this.route_type = route_type;
        this.neighbourhood = neighbourhood;
        this.formatted_address = formatted_address;

        // this.place = place;
        this.municipality_zone = municipality_zone;
        this.in_traffic_zone = in_traffic_zone;
        this.in_odd_even_zone = in_odd_even_zone;
        this.city = city;
        this.state = state;
        // this.addresses = addresses;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }


    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getMunicipality_zone() {
        return municipality_zone;
    }

    public void setMunicipality_zone(String municipality_zone) {
        this.municipality_zone = municipality_zone;
    }

    public String getIn_traffic_zone() {
        return in_traffic_zone;
    }

    public void setIn_traffic_zone(String in_traffic_zone) {
        this.in_traffic_zone = in_traffic_zone;
    }

    public String getIn_odd_even_zone() {
        return in_odd_even_zone;
    }

    public void setIn_odd_even_zone(String in_odd_even_zone) {
        this.in_odd_even_zone = in_odd_even_zone;
    }


}
