package org.group02.guitarshop.service;

import org.group02.guitarshop.entity.Rate;
import org.group02.guitarshop.repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateServiceImpl implements RateService {
    @Autowired
    RateRepository rateRepository;

    public void save(Rate rate){
        rateRepository.save(rate);
    }
}
