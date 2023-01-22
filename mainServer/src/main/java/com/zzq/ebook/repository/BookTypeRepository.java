package com.zzq.ebook.repository;

import com.zzq.ebook.entity.BookType;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookTypeRepository extends Neo4jRepository<BookType, Long> {

    @Query("MATCH (a:BookType)-[:relateBooks]->(b) " +
            "WHERE a.typeName = $name " +
            "RETURN b "
    )
    List<BookType> findNodeRelatedBookTypesDistance1(@Param("name") String name);

    @Query("MATCH (a:BookType)-[:relateBooks]->(b)-[:relateBooks]->(c) " +
            "WHERE a.typeName = $name " +
            "RETURN c "
    )
    List<BookType> findNodeRelatedBookTypesDistance2(@Param("name") String name);


    List<BookType> findBookTypesByTypeNameLike(String name);

}
