package org.group02.guitarshop.service;

import org.group02.guitarshop.entity.DiscountCode;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Service
public class DiscountCodeImpl implements DiscountCodeService {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public DiscountCode getDiscountCodeByCode(String code) {
        try {
            Query query = entityManager.createNativeQuery("SELECT * FROM DiscountCode WHERE Code='" + code + "' AND (GETDATE() BETWEEN Start_Date AND End_Date)", DiscountCode.class);
            DiscountCode discountCode = (DiscountCode) query.getSingleResult();
            return discountCode;
        } catch (Exception e){
            return null;
        }
    }
}
