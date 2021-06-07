package org.pms.sammenu.convert.forms.important_accounts;

import org.pms.sammenu.dto.forms.important_accounts.ImportantAccountAddDto;
import org.pms.sammenu.dto.forms.important_accounts.ImportantAccountDto;
import org.pms.sammenu.redis.ImportantAccountAddRedis;
import org.pms.sammenu.redis.ImportantAccountRedis;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ImportantAccountRedisToImportantAccountDtoConverter implements Converter<ImportantAccountRedis, ImportantAccountDto> {

    @Override
    public ImportantAccountDto convert(ImportantAccountRedis importantAccount) {
        return ImportantAccountDto.builder()
                .id(importantAccount.getId())
                .perAmount(importantAccount.getPerAmount())
                .importantAccountAddDtoList(buildImportantAccountAddList(importantAccount.getImportantAccountAddList(),
                        importantAccount))
                .build();
    }

    private List<ImportantAccountAddDto> buildImportantAccountAddList(List<ImportantAccountAddRedis> importantAccountAddList,
                                                                      ImportantAccountRedis importantAccount){

        return importantAccountAddList.stream()
                .filter(importantAccountAdd -> importantAccountAdd.getBalanceSheetDictionary().getAmount() != 0)
                .map(importantAccountAdd -> {

                    Double amount = importantAccountAdd.getBalanceSheetDictionary().getAmount();
                    boolean isEssential = false;
                    if (amount > importantAccount.getPerAmount())
                        isEssential = true;


                    return ImportantAccountAddDto.builder()
                            .id(importantAccountAdd.getId())
                            .lineId(importantAccountAdd.getBalanceSheetDictionary().getId())
                            .lineName(importantAccountAdd.getBalanceSheetDictionary().getLine())
                            .amount(amount)
                            .isEssential(isEssential)
                            .isImportant((!ObjectUtils.isEmpty(importantAccountAdd.getIsImportantRisk()) &&
                                    importantAccountAdd.getIsImportantRisk() == 1) || importantAccountAdd.getImportant() == 1)
                            .y(!ObjectUtils.isEmpty(importantAccountAdd.getImportant()) && importantAccountAdd.getImportant() == 1 ? importantAccountAdd.getY() : null)
                            .pd(!ObjectUtils.isEmpty(importantAccountAdd.getImportant()) && importantAccountAdd.getImportant() == 1 ? importantAccountAdd.getPd() : null)
                            .ak(!ObjectUtils.isEmpty(importantAccountAdd.getImportant()) && importantAccountAdd.getImportant() == 1 ? importantAccountAdd.getAk() : null)
                            .ap(!ObjectUtils.isEmpty(importantAccountAdd.getImportant()) && importantAccountAdd.getImportant() == 1 ? importantAccountAdd.getAp() : null)
                            .dd(!ObjectUtils.isEmpty(importantAccountAdd.getImportant()) && importantAccountAdd.getImportant() == 1 ? importantAccountAdd.getDd() : null)
                            .tp(!ObjectUtils.isEmpty(importantAccountAdd.getImportant()) && importantAccountAdd.getImportant() == 1 ? importantAccountAdd.getTp() : null)
                            .isAssessment(importantAccountAdd.getAssessment() == 1)
                            .isImportantRisk(!ObjectUtils.isEmpty(importantAccountAdd.getIsImportantRisk()) &&
                                    importantAccountAdd.getIsImportantRisk() == 1)
                            .isImportantAssessment(!ObjectUtils.isEmpty(importantAccountAdd.getIsImportantAssessment()) &&
                                    importantAccountAdd.getIsImportantAssessment() == 1)
                            .build();
                })
                .sorted(Comparator.comparing(ImportantAccountAddDto::getLineId))
                .collect(Collectors.toList());
    }
}
