package org.contacts;

public interface Contact {

    int getId();

    String getName();

    String getNotes();

    void addNotes(String note);
}
