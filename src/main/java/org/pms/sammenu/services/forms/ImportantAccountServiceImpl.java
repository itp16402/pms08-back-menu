package org.pms.sammenu.services.forms;

import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.domain.Project;
import org.pms.sammenu.domain.forms.BalanceSheetDictionary;
import org.pms.sammenu.domain.forms.important_accounts.ImportantAccount;
import org.pms.sammenu.domain.forms.important_accounts.ImportantAccountAdd;
import org.pms.sammenu.dto.forms.important_accounts.ImportantAccountAddRequestDto;
import org.pms.sammenu.dto.forms.important_accounts.ImportantAccountDto;
import org.pms.sammenu.dto.forms.important_accounts.ImportantAccountRequestDto;
import org.pms.sammenu.exceptions.ResourceNotFoundException;
import org.pms.sammenu.repositories.BalanceSheetDictionaryRepository;
import org.pms.sammenu.repositories.important_accounts.ImportantAccountRepository;
import org.pms.sammenu.utils.BalanceSheetUtils;
import org.pms.sammenu.utils.EssentialSizeUtils;
import org.pms.sammenu.utils.FormUtils;
import org.pms.sammenu.utils.ProjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ImportantAccountServiceImpl implements ImportantAccountService {

    private ImportantAccountRepository importantAccountRepository;
    private BalanceSheetDictionaryRepository balanceSheetDictionaryRepository;
    private ConversionService conversionService;
    private BalanceSheetUtils balanceSheetUtils;
    private ProjectUtils projectUtils;
    private EssentialSizeUtils essentialSizeUtils;
    private FormUtils formUtils;

    @Autowired
    public ImportantAccountServiceImpl(ImportantAccountRepository importantAccountRepository,
                                       BalanceSheetDictionaryRepository balanceSheetDictionaryRepository,
                                       ConversionService conversionService, BalanceSheetUtils balanceSheetUtils,
                                       ProjectUtils projectUtils,
                                       EssentialSizeUtils essentialSizeUtils,
                                       FormUtils formUtils) {
        this.importantAccountRepository = importantAccountRepository;
        this.balanceSheetDictionaryRepository = balanceSheetDictionaryRepository;
        this.conversionService = conversionService;
        this.balanceSheetUtils = balanceSheetUtils;
        this.projectUtils = projectUtils;
        this.essentialSizeUtils = essentialSizeUtils;
        this.formUtils = formUtils;
    }

    @Override
    public ImportantAccountDto fetchImportantAccountByProjectId(Long projectId, Long formListId) {

        log.info("Fetch ImportantAccount by project-sam[id: {}] start", projectId);

        Project project = projectUtils.fetchProject(projectId);

        Optional<ImportantAccount> importantAccountOptional = importantAccountRepository
                .findImportantAccountByProject(project);

        if (!importantAccountOptional.isPresent())
            importantAccountOptional = Optional.ofNullable(initialSave(project));

        ImportantAccountDto importantAccountDto = importantAccountOptional
                .map(importantAccount -> conversionService.convert(importantAccount, ImportantAccountDto.class))
                .orElse(new ImportantAccountDto());

        importantAccountDto.setStatus(formUtils.viewFormStatus(projectId, formListId));

        log.info("Fetch ImportantAccount by project-sam[id: {}] end", projectId);

        return importantAccountDto;
    }

    @Override
    public void save(Long projectId, ImportantAccountRequestDto importantAccountRequestDto, Long formListId, HttpServletRequest request) {

        log.info("Save new ImportantAccount start");

        if (ObjectUtils.isEmpty(importantAccountRequestDto))
            throw new ResourceNotFoundException("ImportantAccount to be saved is empty");

        Project project = projectUtils.fetchProject(projectId);

        ImportantAccount importantAccount = ImportantAccount.builder()
                .id(importantAccountRequestDto.getId())
                .perAmount(essentialSizeUtils.getPerformanceMateriality(projectId))
                .project(project)
                .build();

        importantAccount
                .setImportantAccountAddList(buildImportantAccountAddList(importantAccountRequestDto, importantAccount));

        ImportantAccount savedImportantAccount = importantAccountRepository.save(importantAccount);

        formUtils.changeFormStatus(request, projectId, formListId, importantAccountRequestDto.getStatus());

        log.info("Save new ImportantAccount[id: {}] end", savedImportantAccount.getId());
    }

    private ImportantAccount initialSave(Project project){

        Double performanceMateriality = essentialSizeUtils.getPerformanceMateriality(project.getId());

        ImportantAccount importantAccount = ImportantAccount.builder()
                .perAmount(!ObjectUtils.isEmpty(performanceMateriality) ? performanceMateriality : 0.0)
                .project(project)
                .build();

        importantAccount.setImportantAccountAddList(initializeImportantAccountAddList(importantAccount));

        return importantAccountRepository.save(importantAccount);
    }

    private List<ImportantAccountAdd> initializeImportantAccountAddList(ImportantAccount importantAccount){

        List<BalanceSheetDictionary> lines = balanceSheetDictionaryRepository.findAll();

        return lines.stream()
                .filter(line -> line.getAmount() != 0)
                .map(line -> ImportantAccountAdd.builder()
                        .balanceSheetDictionary(line)
                        .important((short) 0)
                        .y((short) 0)
                        .pd((short) 0)
                        .ak((short) 0)
                        .ap((short) 0)
                        .dd((short) 0)
                        .tp((short) 0)
                        .assessment((short) 0)
                        .isImportantRisk((short) 0)
                        .isImportantAssessment((short) 0)
                        .importantAccount(importantAccount)
                        .build())
                .collect(Collectors.toList());
    }

    private List<ImportantAccountAdd> buildImportantAccountAddList(ImportantAccountRequestDto importantAccountRequestDto,
                                                                   ImportantAccount importantAccount){

        List<ImportantAccountAdd> importantAccountAddList = new ArrayList<>();

        List<ImportantAccountAddRequestDto> importantAccountAddRequestDtoList = importantAccountRequestDto
                .getImportantAccountAddRequestDtoList();

        if (ObjectUtils.isEmpty(importantAccountAddRequestDtoList) || importantAccountAddRequestDtoList.isEmpty())
            return null;

        importantAccountAddRequestDtoList.forEach(importantAccountAddRequestDto -> {

                    if (!ObjectUtils.isEmpty(importantAccountAddRequestDto.getId()) &&
                            !ObjectUtils.isEmpty(importantAccountAddRequestDto.getLineId())){

                        BalanceSheetDictionary balanceSheetDictionary = balanceSheetUtils
                                        .buildBalanceSheetDictionary(importantAccountAddRequestDto.getLineId());

                        ImportantAccountAdd importantAccountAdd = ImportantAccountAdd.builder()
                                .id(importantAccountAddRequestDto.getId())
                                .balanceSheetDictionary(balanceSheetDictionary)
                                .important(importantAccountAddRequestDto.isImportant() ? (short) 1: (short) 0)
                                .y(importantAccountAddRequestDto.getY())
                                .pd(importantAccountAddRequestDto.getPd())
                                .ak(importantAccountAddRequestDto.getAk())
                                .ap(importantAccountAddRequestDto.getAp())
                                .dd(importantAccountAddRequestDto.getDd())
                                .tp(importantAccountAddRequestDto.getTp())
                                .assessment(importantAccountAddRequestDto.isImportant() ?
                                        ((importantAccountAddRequestDto.isAssessment() ? (short) 1: (short) 0)) : (short) 0)
                                .isImportantAssessment(importantAccountAddRequestDto.isImportantAssessment() ? (short) 1: (short) 0)
                                .isImportantRisk(importantAccountAddRequestDto.isImportantRisk() ? (short) 1: (short) 0)
                                .importantAccount(importantAccount)
                                .build();

                        importantAccountAddList.add(importantAccountAdd);
                    }
                });

        return importantAccountAddList;
    }
}
