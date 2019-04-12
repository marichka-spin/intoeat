package net.lviv.intoeat.repositories;

import net.lviv.intoeat.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Integer> {

	Group findByName(String name);

    @Query("select distinct g from Group g where UPPER(g.name) like UPPER(:arg) order by g.name ")
    List<Group> findGroupsByName(@Param("arg") String arg);
}
