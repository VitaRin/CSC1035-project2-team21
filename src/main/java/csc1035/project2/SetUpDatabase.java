package csc1035.project2;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SetUpDatabase {

    public static void main(String[] args) {
        SetUpRooms();
        SetUpModules();
        SetUpStudents();
        SetUpStaff();
    }

    public static void SetUpRooms(){
        Session se = null;

        List<Rooms> rooms = new ArrayList<>();
        InputStream stream = SetUpDatabase.class.getClassLoader().getResourceAsStream("rooms.csv");
        Scanner sc = new Scanner(stream);

        sc.nextLine();
        while (sc.hasNextLine()) {

            String[] line = sc.nextLine().split(",");
            rooms.add(new Rooms(line[0], line[1], Integer.parseInt(line[2]), Integer.parseInt(line[3])));
        }

        try {
            se = HibernateUtil.getSessionFactory().openSession();
            se.beginTransaction();

            for (Rooms r: rooms) {
                se.persist(r);
            }

            se.getTransaction().commit();

        } catch (HibernateException e) {
            if (se != null) se.getTransaction().rollback();
            e.printStackTrace();

        } finally {
            assert se != null;
            se.close();
        }
    }

    public static void SetUpModules(){
        Session se = null;

        List<Module> modules = new ArrayList<>();
        InputStream stream = SetUpDatabase.class.getClassLoader().getResourceAsStream("modules.csv");
        Scanner sc = new Scanner(stream);

        sc.nextLine();
        while (sc.hasNextLine()) {

            String[] line = sc.nextLine().split(",");
            modules.add(new Module(line[0], line[1], Integer.parseInt(line[2]), Integer.parseInt(line[3])));
        }

        try {
            se = HibernateUtil.getSessionFactory().openSession();
            se.beginTransaction();

            for (Module m: modules) {
                se.persist(m);
            }

            se.getTransaction().commit();

        } catch (HibernateException e) {
            if (se != null) se.getTransaction().rollback();
            e.printStackTrace();

        } finally {
            assert se != null;
            se.close();
        }
    }

    public static void SetUpStudents(){
        Session se = null;

        List<Student> students = new ArrayList<>();
        InputStream stream = SetUpDatabase.class.getClassLoader().getResourceAsStream("students.csv");
        Scanner sc = new Scanner(stream);

        sc.nextLine();
        while (sc.hasNextLine()) {

            String[] line = sc.nextLine().split(",");
            students.add(new Student(Integer.parseInt(line[0]), line[1], line[2]));
        }

        try {
            se = HibernateUtil.getSessionFactory().openSession();
            se.beginTransaction();

            for (Student s: students) {
                se.persist(s);
            }

            se.getTransaction().commit();

        } catch (HibernateException e) {
            if (se != null) se.getTransaction().rollback();
            e.printStackTrace();

        } finally {
            assert se != null;
            se.close();
        }
    }

    public static void SetUpStaff(){
        Session se = null;

        List<Staff> staff = new ArrayList<>();
        InputStream stream = SetUpDatabase.class.getClassLoader().getResourceAsStream("staff.csv");
        Scanner sc = new Scanner(stream);

        sc.nextLine();
        while (sc.hasNextLine()) {

            String[] line = sc.nextLine().split(",");
            staff.add(new Staff(line[0], line[1], line[2]));
        }

        try {
            se = HibernateUtil.getSessionFactory().openSession();
            se.beginTransaction();

            for (Staff s: staff) {
                se.persist(s);
            }

            se.getTransaction().commit();

        } catch (HibernateException e) {
            if (se != null) se.getTransaction().rollback();
            e.printStackTrace();

        } finally {
            assert se != null;
            se.close();
        }
    }
}