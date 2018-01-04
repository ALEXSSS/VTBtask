package com.websystique.springmvc.repo;

import com.websystique.springmvc.model.DBEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Transactional(isolation = Isolation.SERIALIZABLE)
@Repository
public interface DBEntryRepository extends CrudRepository<DBEntry, Long> {
}



