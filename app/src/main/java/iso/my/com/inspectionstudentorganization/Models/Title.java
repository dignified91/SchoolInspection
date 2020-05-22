package iso.my.com.inspectionstudentorganization.Models;

public class Title
{
    private int id;
    private String title;


    public Title(int id, String title)
    {
        this.id = id;
        this.title = title;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
