package org.pms.sammenu.redis;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportantAccountAddRedis {

    private Long id;
    private BalanceSheetDictionaryRedis balanceSheetDictionary;
    private Short important;
    private Short y;
    private Short pd;
    private Short ak;
    private Short ap;
    private Short dd;
    private Short tp;
    private Short assessment;
    private Short isImportantRisk;
    private Short isImportantAssessment;
}
