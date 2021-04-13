package com.badiplist.source;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "badips", path = "badips")
public interface BadIpsRepository extends PagingAndSortingRepository<BadIp, Long> {

    List<BadIp> findByIp(@Param("ip") String ip);

    List<BadIp> findById(@Param("id") String id);

}
