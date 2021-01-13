package org.group02.guitarshop.service;
import org.group02.guitarshop.entity.Manufacturer;
import org.group02.guitarshop.repository.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {
    @Autowired
    ManufacturerRepository repository;

    @Override
    public List<Manufacturer> listAll(){
        return repository.findAll();
    }

    @Override
    public Manufacturer getManufacturerById(int id){
        return repository.findById(id).get();
    }

    @Override
    public void delete(int id){
        repository.deleteById(id);
    }

    @Override
    public void save(Manufacturer manufacturer){
        repository.save(manufacturer);
    }

}
