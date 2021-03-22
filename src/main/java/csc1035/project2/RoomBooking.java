package csc1035.project2;

import csc1035.project2.interfaces.RoomBookingInterface;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.*;

public class RoomBooking implements RoomBookingInterface {

    List<Room> rooms;
    List<Room> availableRooms = new ArrayList<>();
    static InputCheck ic = new InputCheck();
    static UI UI = new UI();
    static Timetable t = new Timetable();

    /**
     * List of All Rooms in Database
     */
    public void listOfRooms() {
        Session se = HibernateUtil.getSessionFactory().openSession();
        se.beginTransaction();

        rooms = se.createQuery("FROM Rooms").list();

        se.getTransaction().commit();
        se.close();
    }

    /**
     * Books a Room
     * @param roomNumber Room number for the room to be booked
     * @param time Timetable for which the room will be booked
     */
    public void bookRooms(double roomNumber, Time time) {
        Session se = HibernateUtil.getSessionFactory().openSession();
        se.beginTransaction();

        Room room = se.get(Room.class, roomNumber);
        time.setRoom(room);

        if (room.getTimes() == null) {
            List<Time> temp = new ArrayList<>();
            temp.add(time);
            room.setTimes(temp);
        } else {
            room.getTimes().add(time);
        }
        se.update(time);
        se.update(room);
        se.getTransaction().commit();
        se.close();
        UI.roomBookingConfirmation(room);
    }

    /**
     * Distinguishes the room to cancel, removes it from bookedRooms and adds it to availableRooms
     */
    public void cancelRooms(int id) {
        Session se = HibernateUtil.getSessionFactory().openSession();
        se.beginTransaction();

        Time time = se.get(Time.class, id);
        Room room = time.getRoom();

        room.getTimes().remove(time);
        time.setRoom(null);
        se.update(time);
        se.update(room);
        se.getTransaction().commit();
        se.close();

        List<Time> temp = new ArrayList<>();
        temp.add(time);

        UI.roomCancelConfirmation(room, temp);
    }

    public List<Time> bookedRoomsCheck() {
        Session se = HibernateUtil.getSessionFactory().openSession();
        se.beginTransaction();

        String hql = "FROM Time WHERE room != null";
        List<Time> times = se.createQuery(hql).list();
        se.close();

        return times;
    }

    /**
     * Determines rooms that have not been booked
     */
    public void availableRooms() {
        Session se = HibernateUtil.getSessionFactory().openSession();
        se.beginTransaction();

        if (availableRooms.size() != 0) {
            List<Room> temp = availableRooms;
            availableRooms.removeAll(temp);
        }

        String hql = "SELECT r FROM Rooms r WHERE r NOT IN (SELECT t.room FROM Time t)";
        List<Room> temp = se.createQuery(hql).list();

        availableRooms.addAll(temp);
        se.close();
    }

    /**
     * Determines available rooms for a specific date and time.
     * @param timeStart Check with this Time Start
     * @param timeEnd Check with this Time End
     * @param day Check with this Day
     */
    public void availableRoomsDT(String timeStart, String timeEnd, String day) {
        Session se = HibernateUtil.getSessionFactory().openSession();
        se.beginTransaction();
        availableRooms();

        String hql = "FROM Time";
        List<Time> times = se.createQuery(hql).list();
        List<Room> unavailableRooms = new ArrayList<>();

        for (Time time : times) {
            String startTime = time.getTimeStart();
            String endTime = time.getTimeEnd();
            String timeDay = time.getDay();
            if (time.getRoom() == null) {
                break;
            }
            List<Room> temp = se.createQuery("FROM Rooms WHERE roomNumber LIKE '" + time.getRoom().getRoomNumber() + "'").list();

            if (!(t.timeOverlap(timeStart, timeEnd, startTime, endTime, day, timeDay))) {
                if (!(temp.get(0).isIn(availableRooms))) {
                    availableRooms.addAll(temp);
                }
            } else {
                unavailableRooms.addAll(temp);
            }
        }
        for (Room room : unavailableRooms) {
            if (room.isIn(availableRooms)) {
                availableRooms.remove(room);
            }
        }
        se.close();
    }

    /**
     * Shows available Rooms with Social Distancing
     * @param timeStart Module Start time
     * @param timeEnd Module End time
     * @param day Module day
     * @param people amount of people for the module
     */
    public void availableRoomsSocDist(String timeStart, String timeEnd, String day, int people) {
        availableRoomsDT(timeStart, timeEnd, day);

        Session se = HibernateUtil.getSessionFactory().openSession();
        se.beginTransaction();

        String hql = "FROM Rooms r WHERE socialDistCapacity >= " + people;
        List<Room> roomsSocDist = se.createQuery(hql).list();

        availableRooms.removeIf(room -> !(room.isIn(roomsSocDist)));
        se.close();
    }

    /**
     * Adds to timetable list the elements from Time entity for a particular room
     * @param choice user's room choice
     */
    public void producingRoomTimetable(int choice) {
        Session se = HibernateUtil.getSessionFactory().openSession();
        Room room = rooms.get(choice - 1);
        double roomNumber = room.getRoomNumber();

        se.beginTransaction();
        String hql = "FROM Time WHERE room LIKE '" + roomNumber + "'";
        List<Time> timetables = se.createQuery(hql).list();
        se.close();
        UI.timetableRoomsResult(room, timetables);
    }

    /**
     * Changes the type of Room to type from user input
     * @param room Room information that needs to be changed
     * @param newType changed Room Type
     */
    public void changeRoomType(Room room, String newType) {
        Session se = HibernateUtil.getSessionFactory().openSession();
        double roomNumber = room.getRoomNumber();

        room.setType(newType);
        Transaction t = se.beginTransaction();
        String hql = "UPDATE Rooms SET type = '" + newType +
                "' WHERE roomNumber LIKE '" + roomNumber + "'";
        Query query = se.createQuery(hql);
        query.executeUpdate();
        t.commit();
        se.close();
        UI.changeRoomResult(room);
    }

    /**
     * Changes the capacity of Room to capacity from user input
     * @param room Room information that needs to be changed
     * @param newCapacity Change to this Capacity
     */
    public void changeRoomCapacity(Room room, int newCapacity) {
        Session se = HibernateUtil.getSessionFactory().openSession();
        double roomNumber = room.getRoomNumber();

        room.setMaxCapacity(newCapacity);
        Transaction t = se.beginTransaction();
        String hql = "UPDATE Rooms SET maxCapacity = " + newCapacity +
                " WHERE roomNumber LIKE '" + roomNumber + "'";
        Query query = se.createQuery(hql);
        query.executeUpdate();
        t.commit();
        se.close();
        UI.changeRoomResult(room);
    }

    /**
     * Changes the socially distant capacity of Room to capacity from user input
     * @param room Room information that needs to be changed
     * @param newCapacity Change to this Capacity
     */
    public void changeRoomSocDistCapacity(Room room, int newCapacity) {
        Session se = HibernateUtil.getSessionFactory().openSession();
        double roomNumber = room.getRoomNumber();

        room.setSocialDistCapacity(newCapacity);
        Transaction t = se.beginTransaction();
        String hql = "UPDATE Rooms SET socialDistCapacity = " + newCapacity +
                " WHERE roomNumber LIKE '" + roomNumber + "'";
        Query query = se.createQuery(hql);
        query.executeUpdate();
        t.commit();
        se.close();
        UI.changeRoomResult(room);
    }
}
