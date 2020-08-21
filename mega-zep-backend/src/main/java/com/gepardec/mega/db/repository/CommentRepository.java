package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.Comment;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CommentRepository implements PanacheRepository<Comment> {
}
