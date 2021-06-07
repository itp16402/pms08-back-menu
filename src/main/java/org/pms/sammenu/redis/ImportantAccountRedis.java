package org.pms.sammenu.redis;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("a231")
public class ImportantAccountRedis {

    private Long id;
    private Double perAmount;
    private List<ImportantAccountAddRedis> importantAccountAddList;
    private Long projectId;
}
