package ahmedpro.com.reminderme.Model;


public class Task {
    private int id;
    private String name;
    private String time;
    private String data;
    private boolean checkBox;

    public Task() {
    }

    public Task(String name, String time) {
        this.name = name;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public boolean isCheckBox() {
        return checkBox;
    }

    public void setId(int id) {

        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCheckBox(boolean checkBox) {
        this.checkBox = checkBox;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {

        this.data = data;
    }

    @Override
    public String toString() {
        return getName();
    }
}
