package com.portal.repository;

import com.portal.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface UserDao extends CrudRepository<User, Long> {
}
