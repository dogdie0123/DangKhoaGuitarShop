package org.group02.guitarshop.repository;

import java.util.List;
import org.group02.guitarshop.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Message, Integer> {
  @Query(value ="select * from MESSAGE where product_id =?1 and status=1", nativeQuery = true)
  List<Message> messageList(Integer id);
  @Query(value ="select * from MESSAGE where status=0 ORDER BY Id DESC", nativeQuery = true)
  List<Message> messageListNotApprove();
  @Query(value ="select * from MESSAGE where status=1 ORDER BY Id DESC", nativeQuery = true)
  List<Message> messageListApproved();
}
