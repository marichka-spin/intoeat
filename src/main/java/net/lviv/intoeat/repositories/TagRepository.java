package net.lviv.intoeat.repositories;


import net.lviv.intoeat.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    Tag findByName(String name);

    @Query("select distinct t from Tag t where UPPER(t.name) like UPPER(:arg) order by t.name ")
    List<Tag> findTagsByName(@Param("arg") String arg);
}
