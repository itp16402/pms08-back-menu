package org.pms.sammenu.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImportantAccountRedisRepository extends CrudRepository<ImportantAccountRedis, Long> {

    Optional<ImportantAccountRedis> findByProjectId(Long projectId);
}
