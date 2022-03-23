package ro.ubbcluj.mpp.proiectproblema1.model;

public class Client implements Identifiable<Integer> {

    private int ID;
    private String name;
    private String address;

    public Client() {
        this.ID = 0;
        this.name = "";
        this.address = "";
    }

    public Client(int ID, String name, String address) {
        this.ID = ID;
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer id) {
        this.ID = id;
    }

    @Override
    public String toString() {
        return "Client{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}