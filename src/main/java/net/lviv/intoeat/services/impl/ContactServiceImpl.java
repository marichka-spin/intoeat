package net.lviv.intoeat.services.impl;

import net.lviv.intoeat.models.Contact;
import net.lviv.intoeat.repositories.ContactRepository;
import net.lviv.intoeat.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }

}
