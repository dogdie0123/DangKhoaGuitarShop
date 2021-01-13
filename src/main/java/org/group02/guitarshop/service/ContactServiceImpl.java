package org.group02.guitarshop.service;

import org.group02.guitarshop.entity.Message;
import org.group02.guitarshop.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    ContactRepository repository;

    @Override
    public void insertMessage(Message message) {
        repository.save(message);
    }
}
