package org.pms.sammenu.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportantAccountRedisRepository extends CrudRepository<ImportantAccountRedis, Long> {
}
