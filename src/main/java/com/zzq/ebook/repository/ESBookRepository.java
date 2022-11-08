package com.zzq.ebook.repository;


import com.zzq.ebook.entity.ESBook;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface ESBookRepository extends ElasticsearchRepository<ESBook, String> {

    List<ESBook> findAll();

    @Highlight(fields = {
            @HighlightField(name = "description")
    })
    @Query("{\"match_phrase\":{\"description\":\"?0\"}}")
    SearchHits<ESBook> findByDescription(String keyword);

    @Highlight(fields = {
            @HighlightField(name = "description")
    })
    List<ESBook> findByDescriptionLike(String keyword);
}


// @Query("{\"match\":{\"description\":\"?0\"}}")


//
