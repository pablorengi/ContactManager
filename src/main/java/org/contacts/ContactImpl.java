package org.contacts;

public class ContactImpl implements Contact {

    private static int counter = 0;
    private int id;
    private String name;
    private String notes;


    public ContactImpl(String name, String notes) {
        this.id = counter;
        this.name = name;
        this.notes = notes;
        counter++;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void addNotes(String note) {

    }
}
