package org.contacts;

import java.util.Calendar;
import java.util.Set;

public interface Meeting {

    int getId();

    Calendar getDate();

    Set<Contact> getContacts();
}
