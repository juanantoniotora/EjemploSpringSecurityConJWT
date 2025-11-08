package com.juanantoniotora.appPruebaTecnica.repository;

import com.juanantoniotora.appPruebaTecnica.entity.RegistryAndCounterEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends CrudRepository<RegistryAndCounterEntity, Long> {


    /**
     *
     * @param id
     * @return
     */
    @Query(value =
            "SELECT COUNT(*) FROM registry r2 " +
                    "WHERE (r2.hotel_id = (SELECT hotel_id FROM registry r WHERE r.search_id = :id) OR (r2.hotel_id IS NULL AND (SELECT hotel_id FROM registry r WHERE r.search_id = :id) IS NULL)) " +
                    "AND (r2.check_in = (SELECT check_in FROM registry r WHERE r.search_id = :id) OR (r2.check_in IS NULL AND (SELECT check_in FROM registry r WHERE r.search_id = :id) IS NULL)) " +
                    "AND (r2.check_out = (SELECT check_out FROM registry r WHERE r.search_id = :id) OR (r2.check_out IS NULL AND (SELECT check_out FROM registry r WHERE r.search_id = :id) IS NULL)) " +
                    "AND (r2.ages = (SELECT ages FROM registry r WHERE r.search_id = :id) OR (r2.ages IS NULL AND (SELECT ages FROM registry r WHERE r.search_id = :id) IS NULL))",
            nativeQuery = true)
    Long countMatchingById(@Param("id") Long id);
}
