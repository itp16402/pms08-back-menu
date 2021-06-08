package org.pms.sammenu.services.forms;

import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.domain.Project;
import org.pms.sammenu.domain.forms.BalanceSheetDictionary;
import org.pms.sammenu.domain.forms.important_accounts.ImportantAccount;
import org.pms.sammenu.domain.forms.important_accounts.ImportantAccountAdd;
import org.pms.sammenu.dto.forms.important_accounts.ImportantAccountAddRequestDto;
import org.pms.sammenu.dto.forms.important_accounts.ImportantAccountDto;
import org.pms.sammenu.dto.forms.important_accounts.ImportantAccountRequestDto;
import org.pms.sammenu.enums.FormStatus;
import org.pms.sammenu.exceptions.ResourceNotFoundException;
import org.pms.sammenu.redis.BalanceSheetDictionaryRedis;
import org.pms.sammenu.redis.ImportantAccountAddRedis;
import org.pms.sammenu.redis.ImportantAccountRedis;
import org.pms.sammenu.redis.ImportantAccountRedisRepository;
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
    private ImportantAccountRedisRepository importantAccountRedisRepository;
    private BalanceSheetDictionaryRepository balanceSheetDictionaryRepository;
    private ConversionService conversionService;
    private BalanceSheetUtils balanceSheetUtils;
    private ProjectUtils projectUtils;
    private EssentialSizeUtils essentialSizeUtils;
    private FormUtils formUtils;

    @Autowired
    public ImportantAccountServiceImpl(ImportantAccountRepository importantAccountRepository,
                                       ImportantAccountRedisRepository importantAccountRedisRepository,
                                       BalanceSheetDictionaryRepository balanceSheetDictionaryRepository,
                                       ConversionService conversionService, BalanceSheetUtils balanceSheetUtils,
                                       ProjectUtils projectUtils,
                                       EssentialSizeUtils essentialSizeUtils,
                                       FormUtils formUtils) {
        this.importantAccountRepository = importantAccountRepository;
        this.importantAccountRedisRepository = importantAccountRedisRepository;
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

        ImportantAccountDto importantAccountDto;
        Project project = projectUtils.fetchProject(projectId);

        FormStatus status = formUtils.viewFormStatus(projectId, formListId);
        Optional<ImportantAccount> importantAccountOptional = importantAccountRepository
                .findImportantAccountByProject(project);

        if (!ObjectUtils.isEmpty(status) && status.equals(FormStatus.SAVED) && importantAccountOptional.isPresent()){

            Optional<ImportantAccountRedis> importantAccountRedisOptional = importantAccountRedisRepository
                    .findById(importantAccountOptional.get().getId());

            importantAccountDto = importantAccountRedisOptional
                    .map(importantAccount -> conversionService.convert(importantAccount, ImportantAccountDto.class))
                    .orElse(new ImportantAccountDto());
        }
        else {

            if (!importantAccountOptional.isPresent())
                importantAccountOptional = Optional.ofNullable(initialSave(project));

            importantAccountDto = importantAccountOptional
                    .map(importantAccount -> conversionService.convert(importantAccount, ImportantAccountDto.class))
                    .orElse(new ImportantAccountDto());
        }

        importantAccountDto.setStatus(status);

        log.info("Fetch ImportantAccount by project-sam[id: {}] end", projectId);

        return importantAccountDto;
    }

    @Override
    public void save(Long projectId, ImportantAccountRequestDto importantAccountRequestDto, Long formListId, HttpServletRequest request) {

        log.info("Save new ImportantAccount start");

        if (ObjectUtils.isEmpty(importantAccountRequestDto))
            throw new ResourceNotFoundException("ImportantAccount to be saved is empty");

        Double performanceMateriality = essentialSizeUtils.getPerformanceMateriality(projectId);
        performanceMateriality = !ObjectUtils.isEmpty(performanceMateriality) ? performanceMateriality : 0.0;

        if (!ObjectUtils.isEmpty(importantAccountRequestDto.getStatus()) &&
                importantAccountRequestDto.getStatus().equals(FormStatus.SAVED)){

            ImportantAccountRedis importantAccount = ImportantAccountRedis.builder()
                    .id(importantAccountRequestDto.getId())
                    .perAmount(performanceMateriality)
                    .projectId(projectId)
                    .build();

            importantAccount.setImportantAccountAddList(buildImportantAccountAddList(importantAccountRequestDto));

            ImportantAccountRedis savedImportantAccount = importantAccountRedisRepository.save(importantAccount);

            log.info("Save new ImportantAccount[id: {}] end", savedImportantAccount.getId());
        }
        else {

            Project project = projectUtils.fetchProject(projectId);

            ImportantAccount importantAccount = ImportantAccount.builder()
                    .id(importantAccountRequestDto.getId())
                    .perAmount(performanceMateriality)
                    .project(project)
                    .build();

            importantAccount
                    .setImportantAccountAddList(buildImportantAccountAddList(importantAccountRequestDto, importantAccount));

            ImportantAccount savedImportantAccount = importantAccountRepository.save(importantAccount);

            log.info("Save new ImportantAccount[id: {}] end", savedImportantAccount.getId());
        }

        formUtils.changeFormStatus(request, projectId, formListId, importantAccountRequestDto.getStatus());
    }

    private ImportantAccount initialSave(Project project){

        Double performanceMateriality = essentialSizeUtils.getPerformanceMateriality(project.getId());

        ImportantAccount importantAccount = ImportantAccount.builder()
                .perAmount(!ObjectUtils.isEmpty(performanceMateriality) ? performanceMateriality : 0.0)
                .project(project)
                .build();

        List<BalanceSheetDictionary> lines = balanceSheetDictionaryRepository.findAll();

        importantAccount.setImportantAccountAddList(initializeImportantAccountAddList(lines, importantAccount));

        ImportantAccount savedImportantAccount = importantAccountRepository.save(importantAccount);

        if (!ObjectUtils.isEmpty(savedImportantAccount))
            initializeImportantAccountRedis(performanceMateriality, project.getId(), savedImportantAccount);

        return savedImportantAccount;
    }

    private void initializeImportantAccountRedis(Double performanceMateriality,
                                                 Long projectId,
                                                 ImportantAccount savedImportantAccount){

        ImportantAccountRedis importantAccount = ImportantAccountRedis.builder()
                .id(savedImportantAccount.getId())
                .perAmount(!ObjectUtils.isEmpty(performanceMateriality) ? performanceMateriality : 0.0)
                .projectId(projectId)
                .importantAccountAddList(initializeImportantAccountAddList(savedImportantAccount.getImportantAccountAddList()))
                .build();

        importantAccountRedisRepository.save(importantAccount);
    }

    private List<ImportantAccountAdd> initializeImportantAccountAddList(List<BalanceSheetDictionary> lines,
                                                                        ImportantAccount importantAccount){

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

    private List<ImportantAccountAddRedis> initializeImportantAccountAddList(List<ImportantAccountAdd> importantAccountAdds){

        return importantAccountAdds.stream()
                .filter(importantAccountAdd -> importantAccountAdd.getBalanceSheetDictionary().getAmount() != 0)
                .map(importantAccountAdd -> ImportantAccountAddRedis.builder()
                        .id(importantAccountAdd.getId())
                        .balanceSheetDictionary(buildBalanceSheetDictionaryRedis(importantAccountAdd.getBalanceSheetDictionary()))
                        .important(importantAccountAdd.getImportant())
                        .y(importantAccountAdd.getY())
                        .pd(importantAccountAdd.getPd())
                        .ak(importantAccountAdd.getAk())
                        .ap(importantAccountAdd.getAp())
                        .dd(importantAccountAdd.getDd())
                        .tp(importantAccountAdd.getTp())
                        .assessment(importantAccountAdd.getAssessment())
                        .isImportantRisk(importantAccountAdd.getIsImportantRisk())
                        .isImportantAssessment(importantAccountAdd.getIsImportantAssessment())
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

    private List<ImportantAccountAddRedis> buildImportantAccountAddList(ImportantAccountRequestDto importantAccountRequestDto){

        List<ImportantAccountAddRedis> importantAccountAddList = new ArrayList<>();

        List<ImportantAccountAddRequestDto> importantAccountAddRequestDtoList = importantAccountRequestDto
                .getImportantAccountAddRequestDtoList();

        if (ObjectUtils.isEmpty(importantAccountAddRequestDtoList) || importantAccountAddRequestDtoList.isEmpty())
            return null;

        importantAccountAddRequestDtoList.forEach(importantAccountAddRequestDto -> {

                    if (!ObjectUtils.isEmpty(importantAccountAddRequestDto.getId()) &&
                            !ObjectUtils.isEmpty(importantAccountAddRequestDto.getLineId())){

                        BalanceSheetDictionary balanceSheetDictionary = balanceSheetUtils
                                        .buildBalanceSheetDictionary(importantAccountAddRequestDto.getLineId());

                        ImportantAccountAddRedis importantAccountAdd = ImportantAccountAddRedis.builder()
                                .id(importantAccountAddRequestDto.getId())
                                .balanceSheetDictionary(buildBalanceSheetDictionaryRedis(balanceSheetDictionary))
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
                                .build();

                        importantAccountAddList.add(importantAccountAdd);
                    }
                });

        return importantAccountAddList;
    }

    private BalanceSheetDictionaryRedis buildBalanceSheetDictionaryRedis(BalanceSheetDictionary balanceSheetDictionary){

        return BalanceSheetDictionaryRedis.builder()
                .id(balanceSheetDictionary.getId())
                .line(balanceSheetDictionary.getLine())
                .language(balanceSheetDictionary.getLanguage())
                .type(balanceSheetDictionary.getType())
                .amount(balanceSheetDictionary.getAmount())
                .build();
    }
}
