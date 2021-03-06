package edu.launchcode.asmrplaylist.repositories;

import edu.launchcode.asmrplaylist.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;


@Repository
@Transactional
public interface UserDao extends CrudRepository<User, Long> {
}


