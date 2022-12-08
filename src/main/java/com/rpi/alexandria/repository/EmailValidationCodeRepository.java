package com.rpi.alexandria.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.rpi.alexandria.model.EmailValidationCode;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailValidationCodeRepository extends
    CosmosRepository<EmailValidationCode, String> {

}
