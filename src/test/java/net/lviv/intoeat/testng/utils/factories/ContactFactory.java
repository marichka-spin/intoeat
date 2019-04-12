package net.lviv.intoeat.testng.utils.factories;

import net.lviv.intoeat.models.Contact;
import net.lviv.intoeat.vmodels.VContact;

public class ContactFactory {

    public static final int TOTAL_CONTACTS_NUMBER = 4;
    public static final int EXISTING_CONTACT_ID_1 = 1;
    public static final int EXISTING_CONTACT_ID_2 = 2;
    public static final String EXISTING_CONTACT_1_ADDRESS = "Adress 1";
    public static final String EXISTING_CONTACT_2_ADDRESS = "Adress 2";
    public static final String EXISTING_CONTACT_1_EMAIL = "emails@1.test";
    public static final String EXISTING_CONTACT_2_EMAIL = "emails@2.test";
    public static final String NEW_CONTACT_ADDRESS = "New Adress";
    public static final String NEW_CONTACT_EMAIL = "new@1.test";

    public static final Integer CONTACTS_NUMBER_OF_PLACE_1 = 1;

    public static final Contact EXISTING_CONTACT_1 = createContact(EXISTING_CONTACT_ID_1, EXISTING_CONTACT_1_ADDRESS, EXISTING_CONTACT_1_EMAIL);
    public static final Contact EXISTING_CONTACT_2 = createContact(EXISTING_CONTACT_ID_2, EXISTING_CONTACT_2_ADDRESS, EXISTING_CONTACT_2_EMAIL);
    public static final Contact UPDATED_CONTACT_1 = createContact(EXISTING_CONTACT_ID_1, NEW_CONTACT_ADDRESS, NEW_CONTACT_EMAIL);
    public static final Contact NEW_CONTACT = createContact(NEW_CONTACT_ADDRESS, NEW_CONTACT_EMAIL);

    public static final VContact EXISTING_VCONTACT_1 = createVContact(EXISTING_CONTACT_ID_1, EXISTING_CONTACT_1_ADDRESS, EXISTING_CONTACT_1_EMAIL);
    public static final VContact EXISTING_VCONTACT_2 = createVContact(EXISTING_CONTACT_ID_2, EXISTING_CONTACT_2_ADDRESS, EXISTING_CONTACT_2_EMAIL);
    public static final VContact UPDATED_VCONTACT_1 = createVContact(EXISTING_CONTACT_ID_1, NEW_CONTACT_ADDRESS, NEW_CONTACT_EMAIL);
    public static final VContact NEW_VCONTACT = createVContact(NEW_CONTACT_ADDRESS, NEW_CONTACT_EMAIL);

    public static Contact createContact(String address, String email) {
        return createContact(null, address, email);
    }

    public static Contact createContact(Integer id, String address, String email) {
        Contact contact = new Contact();
        if(id != null) {
            contact.setId(id);
        }

        contact.setAddress(address);
        contact.setEmail(email);

        return contact;
    }

    public static VContact createVContact(String address, String email) {
        return createVContact(null, address, email);
    }

    public static VContact createVContact(Integer id, String address, String email) {
        VContact vContact = new VContact();
        if(id != null) {
            vContact.id = id;
        }

        vContact.address = address;
        vContact.email = email;

        return vContact;
    }
}
