package net.lviv.intoeat.repositories;

import net.lviv.intoeat.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Integer> {

    Place findByName(String name);

    @Query("select distinct p from Place p left join p.tags t where UPPER(p.name) " +
            "like UPPER(:arg) or UPPER(t.name) like UPPER(:arg) order by p.name ")
    List<Place> findPlaceByNameOrTag(@Param("arg") String arg);
}
