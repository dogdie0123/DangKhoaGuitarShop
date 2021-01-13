package org.group02.guitarshop.repository;

import org.group02.guitarshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    @Query("select u from User u where u.user_Id =?1")
    User findByUser_Id(int id);

    User save(User user);

    @Query("update User u SET u.enable = true where u.user_Id = ?1")
    @Modifying
    public void enable(Integer id);

    @Query("select u from User u where u.verificationCode = ?1")
    public User findByVerificationCode(String code);

    public User findByResetPasswordToken(String token);


}