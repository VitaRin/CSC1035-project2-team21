package csc1035.project2;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.io.*;
import java.util.*;

public class RoomBooking implements RoomBookingInterface{

    List<Room> rooms;
    List<Room> bookedRooms = new ArrayList<>();
    List<Room> availableRooms = new ArrayList<>();
    static InputCheck ic = new InputCheck();
    File roomFile = new File("src\\main\\resources\\bookedRooms.cvs");

    public void listOfRooms() {
        Session se = HibernateUtil.getSessionFactory().openSession();
        se.beginTransaction();

        rooms = se.createQuery("FROM Rooms").list();

        if (availableRooms.size() == 0) {
            availableRooms = rooms;
        }

        se.getTransaction().commit();
        se.close();
    }

    // Books a room by adding it to bookedRooms list and recording it in bookedRooms.cvs file
    public void bookRooms() {
        UI.availableRoomsList();
        UI.bookRoomsText();
        bookedRoomsFile();

        int choice = ic.get_int_input(1, availableRooms.size());
        Room room = availableRooms.get(choice - 1);

        bookedRooms.add(room);
        availableRooms.remove(room);
        writeToBookedRooms(room);
        UI.roomBookingConfirmation(room);
    }

    // Saves a booked room to bookedRooms.cvs file
    public void writeToBookedRooms(Room room) {
        try {
            FileWriter bookedRoomWriter = new FileWriter(roomFile, true);
            bookedRoomWriter.write(room + "\n");
            bookedRoomWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    // Reads the bookedRooms.cvs file and matches the room to an existing recorded room
    // adding all booked rooms from the file to bookedRooms list
    public void bookedRoomsFile() {
        try {
            Scanner roomFileReader = new Scanner(roomFile);
            while (roomFileReader.hasNextLine()) {
                String line = roomFileReader.nextLine();
                String[] items = line.split("'");
                float roomNumber = Float.parseFloat(items[1]);
                for (Room room : rooms) {
                    if (room.getRoomNumber() == roomNumber && !room.isIn(bookedRooms)) {
                        bookedRooms.add(room);
                        break;
                    }
                }
            }
            roomFileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
    }

    // Distinguishes the room to cancel, removes it from bookedRooms and adds it to availableRooms
    public void cancelRooms() {
        int choice = ic.get_int_input(1, bookedRooms.size());
        Room room = bookedRooms.get(choice - 1);

        bookedRooms.remove(room);
        availableRooms.add(room);
        cancelRoomsFile(room);
        UI.roomCancelConfirmation(room);
    }

    // Reads the contents of bookedRooms.cvs file and matches the selected room to
    // a booked room, overriding the file without that room.
    public void cancelRoomsFile(Room room) {
        try {
            Scanner roomFileReader = new Scanner(roomFile);
            StringBuilder file = new StringBuilder();

            while (roomFileReader.hasNextLine()) {
                String line = roomFileReader.nextLine();
                String[] items = line.split("'");
                float roomNumber = Float.parseFloat(items[1]);

                if (!(room.getRoomNumber() == roomNumber)) {
                    file.append(line);
                    file.append("\n");
                }
            }
            roomFileReader.close();
            FileWriter roomFileWriter = new FileWriter(roomFile);

            roomFileWriter.write(String.valueOf(file));
            roomFileWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    // Determines available rooms
    public void availableRooms() {
        listOfRooms();

        if (bookedRooms.size() == 0) {
            bookedRoomsFile();
        }
        availableRooms.removeIf(room -> room.isIn(bookedRooms));
    }

    // Creates a timetable list for the chosen room
    public void producingRoomTimetable(int choice) {
        Session se = HibernateUtil.getSessionFactory().openSession();
        Room room = rooms.get(choice - 1);
        double roomNumberLower = room.getRoomNumber() - 0.0001;
        double roomNumberHigher = room.getRoomNumber() + 0.0001;

        se.beginTransaction();
        String hql = "FROM Time t WHERE t.roomNumber BETWEEN " + roomNumberLower + " AND " + roomNumberHigher;
        List<Time> timetables = se.createQuery(hql).list();
        se.close();
        UI.timetableRoomsResult(room, timetables);
    }

    public void changeRoomType(Room room, String newType) {
        Session se = HibernateUtil.getSessionFactory().openSession();
        double roomNumberLower = room.getRoomNumber() - 0.0001;
        double roomNumberHigher = room.getRoomNumber() + 0.0001;

        room.setType(newType);
        Transaction t = se.beginTransaction();
        String hql = "UPDATE Rooms SET type = '" + newType +
                "' WHERE roomNumber BETWEEN " + roomNumberLower + " AND " + roomNumberHigher;
        Query query = se.createQuery(hql);
        query.executeUpdate();
        t.commit();
        se.close();
        UI.changeRoomResult(room);
    }

    public void changeRoomCapacity(Room room, int newCapacity) {
        Session se = HibernateUtil.getSessionFactory().openSession();
        double roomNumberLower = room.getRoomNumber() - 0.0001;
        double roomNumberHigher = room.getRoomNumber() + 0.0001;

        room.setMaxCapacity(newCapacity);
        Transaction t = se.beginTransaction();
        String hql = "UPDATE Rooms SET maxCapacity = " + newCapacity +
                " WHERE roomNumber BETWEEN " + roomNumberLower + " AND " + roomNumberHigher;
        Query query = se.createQuery(hql);
        query.executeUpdate();
        t.commit();
        se.close();
        UI.changeRoomResult(room);
    }

    public void changeRoomSocDistCapacity(Room room, int newCapacity) {
        Session se = HibernateUtil.getSessionFactory().openSession();
        double roomNumberLower = room.getRoomNumber() - 0.0001;
        double roomNumberHigher = room.getRoomNumber() + 0.0001;

        room.setSocialDistCapacity(newCapacity);
        Transaction t = se.beginTransaction();
        String hql = "UPDATE Rooms SET socialDistCapacity = " + newCapacity +
                " WHERE roomNumber BETWEEN " + roomNumberLower + " AND " + roomNumberHigher;
        Query query = se.createQuery(hql);
        query.executeUpdate();
        t.commit();
        se.close();
        UI.changeRoomResult(room);
    }
}
