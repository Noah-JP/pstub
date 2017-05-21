package com.gt.stub.web.service;

import com.gt.stub.persistence.entity.CardInfo;
import com.gt.stub.persistence.entity.Owner;

/**
 * Created by noah on 2017. 5. 20..
 */
public interface CardService {

    CardInfo createBlank(String cardNo);

    CardInfo createBy(String cardNo, CardInfo createData);

    CardInfo update(CardInfo updateData);

    CardInfo addOwner(String cardNo, Owner owner);

    void delete(String cardNo);

    CardInfo get(String cardNo);

    boolean existToken(String token);

    String maxCardNo();

}
