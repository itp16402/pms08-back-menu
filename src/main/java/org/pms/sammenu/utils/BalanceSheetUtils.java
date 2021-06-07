package org.pms.sammenu.utils;

import org.pms.sammenu.domain.Project;
import org.pms.sammenu.domain.forms.BalanceSheetDictionary;
import org.pms.sammenu.domain.forms.BasicInfo;
import org.pms.sammenu.enums.BalanceSheetType;
import org.pms.sammenu.exceptions.ResourceNotFoundException;
import org.pms.sammenu.repositories.BalanceSheetDictionaryRepository;
import org.pms.sammenu.repositories.BasicInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BalanceSheetUtils {

    private BalanceSheetDictionaryRepository balanceSheetDictionaryRepository;
    private BasicInfoRepository basicInfoRepository;
    private BasicInfoUtils basicInfoUtils;

    @Autowired
    public BalanceSheetUtils(BalanceSheetDictionaryRepository balanceSheetDictionaryRepository,
                             BasicInfoRepository basicInfoRepository,
                             BasicInfoUtils basicInfoUtils) {
        this.balanceSheetDictionaryRepository = balanceSheetDictionaryRepository;
        this.basicInfoRepository = basicInfoRepository;
        this.basicInfoUtils = basicInfoUtils;
    }

    public BalanceSheetDictionary buildBalanceSheetDictionary(Long id){

        Optional<BalanceSheetDictionary> balanceSheetDictionary = balanceSheetDictionaryRepository.findById(id);

        if (!balanceSheetDictionary.isPresent())
            throw new ResourceNotFoundException("There is not balance-sheet-line with id: " + id);

        return balanceSheetDictionary.get();
    }

    public BalanceSheetType getBalanceSheetType(Long projectId){

        Optional<BasicInfo> basicInfo = basicInfoRepository
                .findBasicInfoByProject(Project.builder().id(projectId).build());

        if (!basicInfo.isPresent())
            throw new ResourceNotFoundException("There is not basic-info for order: " + projectId);

        return BalanceSheetType.fromValue(basicInfo.get().getBalanceSheetType());
    }
}
