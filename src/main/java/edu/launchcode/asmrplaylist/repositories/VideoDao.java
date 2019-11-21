package edu.launchcode.asmrplaylist.repositories;

import edu.launchcode.asmrplaylist.models.Video;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface VideoDao extends CrudRepository<Video, Integer> {
}
