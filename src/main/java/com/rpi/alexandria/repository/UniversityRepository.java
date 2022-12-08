package com.rpi.alexandria.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.rpi.alexandria.model.University;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends CosmosRepository<University, String> {

  List<University> findAll();

}
