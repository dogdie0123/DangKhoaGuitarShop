package org.group02.guitarshop.service;

import org.group02.guitarshop.entity.Manufacturer;

import java.util.List;

public interface ManufacturerService {
     List<Manufacturer> listAll();
     void delete(int id);
     Manufacturer getManufacturerById(int id);
     void save(Manufacturer manufacturer);
}
