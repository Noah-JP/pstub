package com.gt.stub.persistence.repository;

import com.gt.stub.persistence.entity.CardInfo;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by noah on 2017. 5. 20..
 */
public interface CardInfoCURDRepository extends CrudRepository<CardInfo, String> {
    CardInfo findByToken(String token);
}
