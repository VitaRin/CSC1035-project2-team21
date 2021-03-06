package csc1035.project2;

import javax.persistence.*;

/**
 * This is a class for representing all Booked Times and Rooms in University
 */
@Entity(name = "Time")
public class Time {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private int id;

    @Column
    private String timetableName;

    @Column
    private String timeStart;

    @Column
    private String timeEnd;

    @Column
    private String day;

    @Column
    private String moduleID;

    @ManyToOne
    @JoinColumn
    private Room room;

    public Time() {}

    public Time(int id, String timetableName, String timeStart, String timeEnd, String day, String moduleID, Room room) {
        this.id = id;
        this.timetableName = timetableName;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.day = day;
        this.moduleID = moduleID;
        this.room = room;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimetableName() {
        return timetableName;
    }

    public void setTimetableName(String timetableName) {
        this.timetableName = timetableName;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getModuleID() {
        return moduleID;
    }

    public void setModuleID(String moduleID) {
        this.moduleID = moduleID;
    }

//    public double getRoomNumber() {
//        return roomNumber;
//    }
//
//    public void setRoomNumber(double roomNumber) {
//        this.roomNumber = roomNumber;
//    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
