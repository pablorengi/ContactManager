package org.contacts;

public class App {
    public static void main(String[] args) {
        ContactManagerImpl contactManager = new ContactManagerImpl();

        contactManager.addNewContact("Manager", "Various notes taken");
        contactManager.addNewContact("Supervisor", "Various notes taken");
        contactManager.addNewContact("Secretary", "Various notes taken");

        contactManager.flush();
    }
}
