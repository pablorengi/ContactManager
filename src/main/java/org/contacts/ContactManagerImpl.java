package org.contacts;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ContactManagerImpl implements ContactManager {

    private Set<Contact> contacts;
    private List<Meeting> meetings;
    private Calendar calendar;

    private int counter;

    public ContactManagerImpl() {
        this.contacts = new HashSet<>();
        this.meetings = new ArrayList<>();
        this.calendar = new GregorianCalendar();
        this.counter = 0;

        File file = new File("contacts.txt");

        if(file.exists()) {
            boolean isContact = false;
            boolean isMeeting = false;

            try(BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    String[] lineItems = line.split(",");
                    if(lineItems[0].equals("ContactId")) {
                        isContact = true;
                    } else if (lineItems[0].equals("MeetingID")){
                        isMeeting = true;
                    } else {
                        if (isContact) {
                            int id = Integer.parseInt(lineItems[0]);
                            String contactName = lineItems[1];
                            String contactNotes = lineItems[2];

                            Contact contact = new ContactImpl(contactName, contactNotes);
                            contacts.add(contact);
                        } else if (isMeeting) {
                            Set<Contact> meetingContacts;
                            int meetingID = Integer.parseInt(lineItems[0]);
                            DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z YYYY");
                            Date date = format.parse(lineItems[1]);

                            Calendar calendar = new GregorianCalendar();
                            calendar.setTime(date);

                            String meetingsContacts = lineItems[2];
                            String[] contacts = meetingsContacts.split(";");

                            int[] contactIds = new int[contacts.length];

                            for (int i = 0; i < contactIds.length; i++) {
                                contactIds[i] = Integer.parseInt(contacts[i]);
                            }
                            meetingContacts = getContacts(contactIds);
                            Calendar dateNow = new GregorianCalendar();

                            if (calendar.after(dateNow)) {
                                Meeting futureMeeting = new FutureMeetingImpl(meetingID, calendar, meetingContacts);
                                meetings.add(futureMeeting);
                            } else {
                                Meeting pastMeeting = new PastMeetingImpl(meetingID, calendar, meetingContacts);
                                meetings.add(pastMeeting);
                            }
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        int id = counter++;
        this.meetings.add(new FutureMeetingImpl(id, date, contacts));
        return id;
    }

    public PastMeeting getPastMeeting(int id) {

        if(!meetings.isEmpty()) {
            for(Meeting meeting : meetings) {
                if(meeting.getId() == id) {
                    return (PastMeeting) meeting;
                }
            }
        } else {
            System.out.println("Meeting list is empty");
            return null;
        }
        return null;
    }

    public FutureMeeting getFutureMeeting(int id) {
        if(!meetings.isEmpty()) {
            for(Meeting meeting : meetings) {
                if(meeting.getId() == id) {
                    return (FutureMeeting) meeting;
                }
            }
        } else {
            System.out.println("Meeting list is empty");
            return null;
        }
        return null;
    }

    public Meeting getMeeting(int id) {
        return meetings.get(id);
    }

    public List<Meeting> getFutureMeetingList(Contact contact) {
        List<Meeting> futureMeetings = new ArrayList<Meeting>();

        for(Meeting meeting : meetings) {
            if(meeting.getContacts().contains(contact)) {
                futureMeetings.add(meeting);
            }
        }
        return futureMeetings;
    }

    public List<Meeting> getFutureMeetingList(Calendar date) {
        List<Meeting> futureMeetings = new ArrayList<Meeting>();

        for(Meeting meeting : meetings) {
            if(meeting.getDate().equals(date)) {
                futureMeetings.add(meeting);
            }
        }
        return futureMeetings;
    }

    public List<PastMeeting> getPastMeetingList(Contact contact) {
        List<PastMeeting> futureMeetings = new ArrayList<>();

        for(Meeting meeting : meetings) {
            if(meeting.getContacts().contains(contact)) {
                futureMeetings.add((PastMeeting) meeting);
            }
        }
        return futureMeetings;
    }

    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        int id = counter++;
        this.meetings.add(new PastMeetingImpl(id, date, contacts, text));
    }

    public void addMeetingNotes(int id, String text) {
        PastMeetingImpl aMeeting;
        for(Meeting meeting : meetings) {
            if(meeting.getId() == id) {
                aMeeting = (PastMeetingImpl) meeting;
                aMeeting.setNotes(text);
            }
        }
    }

    public void addNewContact(String name, String notes) {
        for(Contact contact : contacts) {
            if(name.equals(contact.getName())) {
                return;
            }
        }
        this.contacts.add(new ContactImpl(name, notes));
    }

    public Set<Contact> getContacts(int... ids) {
        Set<Contact> myContacts = new HashSet<>();
        for(Contact contact : contacts) {
            for(int id : ids) {
                if(id == contact.getId()) {
                    myContacts.add(contact);
                }
            }
        }
        return myContacts;
    }

    public Set<Contact> getContacts(String name) {
        Set<Contact> myContacts = new HashSet<>();
        for(Contact contact : contacts) {
            if(contact.getName().equals(name)) {
                myContacts.add(contact);
            }
        }
        return myContacts;
    }

    public void flush() {
        try (FileWriter contactFile = new FileWriter("contacts.txt")) {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("ContactId,ContactName,Notes\n");

            for (Contact c : contacts) {
                stringBuilder.append(c.getId() + "," + c.getName() + "," + c.getNotes() + "\n");
            }

            stringBuilder.append("MeetingID,Date,Contacts,Notes\n");

            for (Meeting meeting : meetings) {
                stringBuilder.append(meeting.getId() + "," + meeting.getDate().getTime() + ",");

                Object[] contactListForDataEntry;
                Object thisContact;
                Set<Contact> meetingContacts = meeting.getContacts();

                for(Contact contact : meetingContacts) {
                    stringBuilder.append(contact.getId() + ";");
                }

                if(meeting instanceof PastMeetingImpl) {
                    PastMeetingImpl pastMeeting = (PastMeetingImpl) meeting;
                    stringBuilder.append("," + pastMeeting.getNotes());
                }

            }
            contactFile.write(stringBuilder.toString());

        } catch (IOException e) {
            System.out.println("An error has occurred");
        }
    }
}
