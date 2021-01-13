package org.group02.guitarshop.repository;

import org.group02.guitarshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountCodeRepository extends JpaRepository<Product, Integer> {
}
