package com.gt.stub.persistence.repository;

import com.gt.stub.persistence.entity.CardInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by noah on 2017. 5. 20..
 */
public interface CardInfoCRUDRepository extends CrudRepository<CardInfo, String> {
    CardInfo findByToken(String token);

    @Query("SELECT MAX(c.cardNo) FROM CardInfo AS c")
    String findMaxCardNo();
}
