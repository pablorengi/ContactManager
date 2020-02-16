package org.contacts;

import java.util.Calendar;
import java.util.Set;

public class MeetingImpl implements Meeting {

    private int id;
    private Calendar date;
    private Set<Contact> contacts;

    public MeetingImpl() {
    }

    public MeetingImpl(int id, Calendar date, Set<Contact> contacts) {
        this.id = id;
        this.date = date;
        this.contacts = contacts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public void setSetOfContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }
}
