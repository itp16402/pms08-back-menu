package org.pms.sammenu.redis;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("a231")
public class ImportantAccountRedis {

    @Id
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "SEQ_USER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    private Long id;
    private Double perAmount;
    private List<ImportantAccountAddRedis> importantAccountAddList;
}
