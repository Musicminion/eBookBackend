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

    // 经过优化后的搜索查询语句，可以支持多字段切开后搜索
    @Highlight(fields = {
            @HighlightField(name = "description")
    })
    @Query("{\"multi_match\":{\"fields\": [\"description\"],\"query\":\"?0\"}}")
    SearchHits<ESBook> findByDescription(String keyword);
}

//      其他可以选用的查询语句，但是效果不是很好
//      @Query("{\"match\":{\"description\":\"?0\"}}")
//      @Query("{\"match_phrase\":{\"description\":\"?0\"}}")


