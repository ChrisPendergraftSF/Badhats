package com.badiplist.source;

import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "sources", path = "sources")
public interface ListSourceRepository extends PagingAndSortingRepository<ListSource, Long> {

    List<ListSource> findByListName(@Param("listName") String listName);

}