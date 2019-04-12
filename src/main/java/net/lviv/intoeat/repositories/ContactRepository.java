package net.lviv.intoeat.repositories;

import net.lviv.intoeat.models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

}
