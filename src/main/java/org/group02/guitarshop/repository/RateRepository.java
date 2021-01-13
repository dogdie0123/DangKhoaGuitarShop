package org.group02.guitarshop.repository;

import org.group02.guitarshop.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RateRepository extends JpaRepository<Rate, Integer> {
    @Query("select r from Rate r where r.productByIdProduct.id =?1 order by r.createdTime desc")
    public List<Rate> findByProductId(int id);
}
