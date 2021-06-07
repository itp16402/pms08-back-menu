package org.pms.sammenu.redis;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceSheetDictionaryRedis {

    private Long id;
    private String line;
    private String type;
    private String language;
    private Double amount;
}
