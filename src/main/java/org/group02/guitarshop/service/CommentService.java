package org.group02.guitarshop.service;

import org.group02.guitarshop.entity.Message;
import java.util.List;

public interface CommentService {
    Integer insertComment(Message message);
    Message getCommentById(Integer id);
    public List<Message> findAllCommentOfProduct(Integer productId);
    public List<Message> findAllCommentNotApprove();
    public List<Message> findAllCommentApproved();
    public void approveComment(Integer commentId);
    public void deleteComment(Integer commentId);
}
