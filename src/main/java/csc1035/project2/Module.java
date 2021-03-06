package csc1035.project2;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a class for representing module details in University
 */
@Entity(name = "Module")
public class Module {

    @Id
    @Column(updatable = false, nullable = false)
    private String moduleID;

    @Column
    private String moduleName;

    @Column
    private int credits;

    @Column
    private int weeks;

    @ManyToMany(mappedBy = "modules")
    private Set<Student> students = new HashSet<>();

    @ManyToMany(mappedBy = "modules")
    private Set<Staff> staff = new HashSet<>();

    public Module() {}

    public Module(String moduleID, String moduleName, int credits, int weeks) {
        this.moduleID = moduleID;
        this.moduleName = moduleName;
        this.credits = credits;
        this.weeks = weeks;
    }

    public String getModuleID() {
        return moduleID;
    }

    public void setModuleID(String moduleID) {
        this.moduleID = moduleID;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getWeeks() {
        return weeks;
    }

    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }

    @Override
    public String toString() {
        return moduleID + " " + moduleName  + " " + credits + " " + weeks;
    }
}