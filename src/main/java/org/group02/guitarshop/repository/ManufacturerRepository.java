package org.group02.guitarshop.repository;

import org.group02.guitarshop.entity.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerRepository extends JpaRepository <Manufacturer, Integer> {
}
