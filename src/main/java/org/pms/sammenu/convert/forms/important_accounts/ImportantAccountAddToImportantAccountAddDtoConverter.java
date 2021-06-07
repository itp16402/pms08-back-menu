package org.pms.sammenu.convert.forms.important_accounts;

import org.pms.sammenu.domain.forms.important_accounts.ImportantAccountAdd;
import org.pms.sammenu.dto.forms.important_accounts.ImportantAccountAddDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class ImportantAccountAddToImportantAccountAddDtoConverter
        implements Converter<ImportantAccountAdd, ImportantAccountAddDto> {

    @Override
    public ImportantAccountAddDto convert(ImportantAccountAdd importantAccountAdd) {
        return ImportantAccountAddDto.builder()
                .id(importantAccountAdd.getId())
                .lineId(importantAccountAdd.getBalanceSheetDictionary().getId())
                .lineName(importantAccountAdd.getBalanceSheetDictionary().getLine())
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
    }
}
