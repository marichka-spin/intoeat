package net.lviv.intoeat.repositories;

import net.lviv.intoeat.models.Details;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailsRepository extends JpaRepository<Details, Integer> {
}
