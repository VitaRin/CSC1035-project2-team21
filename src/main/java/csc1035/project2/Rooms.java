package csc1035.project2;
    
import javax.persistence.*;

@Entity(name = "Rooms")
public class Rooms {

    @Id
    @Column(updatable = false, nullable = false)
    private float roomNumber;

    @Column
    private String type;

    @Column
    private int maxCapacity;

    @Column
    private int socialDistCapacity;

    public Rooms(float roomNumber, String type, int maxCapacity, int socialDistCapacity) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.maxCapacity = maxCapacity;
        this.socialDistCapacity = socialDistCapacity;
    }

    public Rooms() {}

    public float getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(float roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getSocialDistCapacity() {
        return socialDistCapacity;
    }

    public void setSocialDistCapacity(int socialDistCapacity) {
        this.socialDistCapacity = socialDistCapacity;
    }

    @Override
    public String toString() {
        return "Rooms{" +
                "roomNumber='" + roomNumber + '\'' +
                ", type='" + type + '\'' +
                ", maxCapacity=" + maxCapacity +
                ", socialDistCapacity=" + socialDistCapacity +
                '}';
    }

    // Checks for logical equivalence
    public boolean compare(Rooms room) {
        if (this == room) {
            return true;
        }
        if (this.roomNumber == room.roomNumber) {
            return true;
        }
        return this.equals(room);
    }
}
