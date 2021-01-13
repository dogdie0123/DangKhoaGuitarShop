package org.group02.guitarshop.repository;

import org.group02.guitarshop.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Message, Integer> {
}
