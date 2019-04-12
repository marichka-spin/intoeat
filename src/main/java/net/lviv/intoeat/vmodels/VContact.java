package net.lviv.intoeat.vmodels;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.lviv.intoeat.models.Contact;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VContact extends VBaseModel {

    public String address;
    public String phone;
    public String webSite;
    public String email;
    public String workTime;

    public static VContact transform(Contact contact) {
        VContact vContact = new VContact();
        vContact.id = contact.getId();
        vContact.address = contact.getAddress();
        vContact.phone = contact.getPhone();
        vContact.webSite = contact.getWebSite();
        vContact.email = contact.getEmail();
        vContact.workTime = contact.getWorkTime();
        return vContact;
    }

    public static Contact transformAdmin(VContact vContact) {
        Contact contact = new Contact();
        contact.setId(vContact.id);
        contact.setAddress(vContact.address);
        contact.setEmail(vContact.email);
        contact.setPhone(vContact.phone);
        contact.setWebSite(vContact.webSite);
        contact.setWorkTime(vContact.workTime);
        return contact;
    }
}
