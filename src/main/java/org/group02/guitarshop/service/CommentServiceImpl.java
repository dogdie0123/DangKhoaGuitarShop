package org.group02.guitarshop.service;

import org.group02.guitarshop.entity.Message;
import org.group02.guitarshop.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Service("commentService")
@Slf4j
public class CommentServiceImpl implements CommentService {
  @Autowired
  CommentRepository repository;

  @Override
  public Integer insertComment(Message message) {
    Message returnedInvoice = repository.save(message);
    return returnedInvoice.getId();
  }

  @Override
  public Message getCommentById(Integer id) {
    Message message = repository.findById(id).get();
    return message;
  }

  @Override
  public List<Message> findAllCommentOfProduct(Integer productId) {
    return repository.messageList(productId);
  }

  @Override
  public List<Message> findAllCommentNotApprove() {
    return repository.messageListNotApprove();
  }

  @Override
  public List<Message> findAllCommentApproved() {
    return repository.messageListApproved();
  }

  @Override
  public void approveComment(Integer commentId) {
    Message message = repository.findById(commentId).get();
    message.setStatus(1);
    repository.save(message);
  }

  @Override
  public void deleteComment(Integer commentId) {
    repository.deleteById(commentId);
  }
}
