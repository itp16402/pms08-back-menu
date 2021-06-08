package org.pms.sammenu.services.forms;

import lombok.extern.slf4j.Slf4j;
import org.pms.sammenu.domain.Project;
import org.pms.sammenu.domain.forms.BalanceSheetDictionary;
import org.pms.sammenu.domain.forms.essential_size.EssentialSize;
import org.pms.sammenu.domain.forms.essential_size.EssentialSizeOverall;
import org.pms.sammenu.domain.forms.essential_size.EssentialSizePerformance;
import org.pms.sammenu.domain.forms.essential_size.base.Base;
import org.pms.sammenu.domain.forms.essential_size.base.MaterialityBase;
import org.pms.sammenu.domain.forms.important_accounts.ImportantAccount;
import org.pms.sammenu.dto.forms.essential_size.base.BaseDto;
import org.pms.sammenu.dto.forms.essential_size.base.BaseResponseDto;
import org.pms.sammenu.dto.forms.essential_size.base.MaterialityBaseDto;
import org.pms.sammenu.dto.forms.essential_size.requests.EssentialSizeOverallRequestDto;
import org.pms.sammenu.dto.forms.essential_size.requests.EssentialSizePerformanceRequestDto;
import org.pms.sammenu.dto.forms.essential_size.requests.EssentialSizeRequestDto;
import org.pms.sammenu.dto.forms.essential_size.responses.EssentialSizeDto;
import org.pms.sammenu.dto.forms.essential_size.responses.EssentialSizePerformanceDto;
import org.pms.sammenu.enums.BalanceSheetType;
import org.pms.sammenu.enums.FormType;
import org.pms.sammenu.enums.Locale;
import org.pms.sammenu.exceptions.ResourceNotFoundException;
import org.pms.sammenu.exceptions.UnacceptableActionException;
import org.pms.sammenu.redis.ImportantAccountRedis;
import org.pms.sammenu.redis.ImportantAccountRedisRepository;
import org.pms.sammenu.repositories.essential_size.*;
import org.pms.sammenu.repositories.important_accounts.ImportantAccountRepository;
import org.pms.sammenu.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class EssentialSizeServiceImpl implements EssentialSizeService {

    private static final String MIN_LIMIT = "min-limit";
    private static final String MAX_LIMIT = "max-limit";

    private EssentialSizeRepository essentialSizeRepository;
    private EssentialSizePerformanceRepository essentialSizePerformanceRepository;
    private EssentialSizeOverallRepository essentialSizeOverallRepository;
    private BaseRepository baseRepository;
    private MaterialityBaseRepository materialityBaseOrderRepository;
    private ImportantAccountRepository importantAccountRepository;
    private ImportantAccountRedisRepository importantAccountRedisRepository;
    private ConversionService conversionService;
    private BalanceSheetUtils balanceSheetUtils;
    private FormUtils formUtils;
    private ProjectUtils projectUtils;
    private BasicInfoUtils basicInfoUtils;

    @Autowired
    public EssentialSizeServiceImpl(EssentialSizeRepository essentialSizeRepository,
                                    EssentialSizePerformanceRepository essentialSizePerformanceRepository,
                                    EssentialSizeOverallRepository essentialSizeOverallRepository,
                                    BaseRepository baseRepository,
                                    MaterialityBaseRepository materialityBaseOrderRepository,
                                    ImportantAccountRepository importantAccountRepository,
                                    ImportantAccountRedisRepository importantAccountRedisRepository,
                                    ConversionService conversionService, BalanceSheetUtils balanceSheetUtils,
                                    FormUtils formUtils, ProjectUtils projectUtils,
                                    BasicInfoUtils basicInfoUtils) {
        this.essentialSizeRepository = essentialSizeRepository;
        this.essentialSizePerformanceRepository = essentialSizePerformanceRepository;
        this.essentialSizeOverallRepository = essentialSizeOverallRepository;
        this.baseRepository = baseRepository;
        this.materialityBaseOrderRepository = materialityBaseOrderRepository;
        this.importantAccountRepository = importantAccountRepository;
        this.importantAccountRedisRepository = importantAccountRedisRepository;
        this.conversionService = conversionService;
        this.balanceSheetUtils = balanceSheetUtils;
        this.formUtils = formUtils;
        this.projectUtils = projectUtils;
        this.basicInfoUtils = basicInfoUtils;
    }

    @Override
    public EssentialSizeDto fetchEssentialSizeByProjectId(Long projectId, Long formListId, Locale locale) {

        log.info("Fetch EssentialSize by project-sam[id: {}] start", projectId);

        Project project = projectUtils.fetchProject(projectId);

        Optional<EssentialSize> optionalEssentialSize = essentialSizeRepository.findEssentialSizeByProject(project);

        if (!optionalEssentialSize.isPresent())
            optionalEssentialSize = Optional.of(initialSave(project, locale));

        EssentialSizeDto essentialSizeDto = optionalEssentialSize
                .map(essentialSize -> conversionService.convert(essentialSize, EssentialSizeDto.class))
                .orElse(null);

        if (!ObjectUtils.isEmpty(essentialSizeDto))
            essentialSizeDto.setStatus(formUtils.viewFormStatus(projectId, formListId));

        log.info("Fetch EssentialSize by project-sam[id: {}] end", projectId);

        return essentialSizeDto;
    }

    @Override
    public List<BaseResponseDto> fetchBaseList(Long projectId, Locale locale) {

        log.info("Fetch Base-list by order-sam[id: {}] start", projectId);

        BalanceSheetType balanceSheetType = balanceSheetUtils.getBalanceSheetType(projectId);

        FormType formType = basicInfoUtils.getFormTypeByProjectId(projectId);

        List<Base> baseList = baseRepository.findByBalanceSheetType(balanceSheetType.description(), locale.code());

        List<BaseResponseDto> baseDtoList = baseList.stream()
                .filter(base -> !(formType.equals(FormType.OTA) && base.getId() == 5))
                .map(base -> BaseResponseDto.builder()
                        .id(base.getId())
                        .name(base.getName())
                        .language(base.getLanguage())
                        .amount(buildAmount(base.getMaterialityBaseList()))
                        .build())
                .sorted(Comparator.comparing(BaseResponseDto::getId))
                .collect(Collectors.toList());

        log.info("Fetch Base-list by order-sam[id: {}] end", projectId);

        return baseDtoList;
    }

    @Override
    public BaseDto fetchBase(Integer baseId, Long projectId, Locale locale) {

        log.info("Fetch Base [id: {}] start", baseId);

        Optional<Base> baseOptional = baseRepository.findById(baseId);

        BalanceSheetType balanceSheetType = balanceSheetUtils.getBalanceSheetType(projectId);

        List<MaterialityBase> materialityBaseList = materialityBaseOrderRepository
                .findByBalanceSheetTypeAndBaseId(balanceSheetType.description(), locale.code(), baseId);

        BaseDto baseDto = baseOptional
                .map(base -> BaseDto.builder()
                        .id(base.getId())
                        .name(base.getName())
                        .materialityBaseDtoList(buildMaterialityBaseDtoList(materialityBaseList))
                        .build())
                .orElse(null);

        log.info("Fetch Base [id: {}] end", baseId);

        return baseDto;
    }

    @Override
    public void save(Long projectId, EssentialSizeRequestDto essentialSizeDto, Long formListId, HttpServletRequest request) {

        log.info("Save new EssentialSize start");

        if (ObjectUtils.isEmpty(essentialSizeDto))
            throw new ResourceNotFoundException("EssentialSize to be saved is empty");

        EssentialSize essentialSize = conversionService.convert(essentialSizeDto, EssentialSize.class);

        Project project = projectUtils.fetchProject(projectId);

        essentialSize.setProject(project);

        EssentialSize savedEssentialSize = essentialSizeRepository.save(essentialSize);

        formUtils.changeFormStatus(request, projectId, formListId, essentialSizeDto.getStatus());

        log.info("Save new EssentialSize[id: {}] end", savedEssentialSize.getId());
    }

    @Override
    public void save(Long projectId, EssentialSizeOverallRequestDto essentialSizeOverallRequestDto) {

        log.info("Save new EssentialSizeOverall[id: {}] process begins", essentialSizeOverallRequestDto.getId());

        EssentialSize essentialSize = buildEssentialSize(projectId);

        EssentialSizeOverall essentialSizeOverall = conversionService
                .convert(essentialSizeOverallRequestDto, EssentialSizeOverall.class);

        if (essentialSizeOverall.getOverAmount() < essentialSizeOverall.getMinLimit() ||
                essentialSizeOverall.getOverAmount() > essentialSizeOverall.getMaxLimit())
            throw new  UnacceptableActionException("OverAmount outside the limits");

        essentialSizeOverall.setOverAmount(NumericUtils.roundNumberToCentimeters(
                essentialSizeOverall.getOverAmount() * essentialSizeOverall.getPercentage() / 100));

        essentialSizeOverall.setEssentialSize(essentialSize);

        saveOverAmount(essentialSizeOverall, essentialSize);

        log.info("Save new EssentialSizeOverall[id: {}] process end", essentialSizeOverallRequestDto.getId());
    }

    @Override
    public void save(Long projectId, EssentialSizePerformanceRequestDto essentialSizePerformanceRequestDto) {

        log.info("Save new EssentialSizePerformance[id: {}] process begins", essentialSizePerformanceRequestDto.getId());

        EssentialSize essentialSize = buildEssentialSize(projectId);

        EssentialSizePerformance essentialSizePerformance = conversionService
                .convert(essentialSizePerformanceRequestDto, EssentialSizePerformance.class);

        essentialSizePerformance.setEssentialSize(essentialSize);

        essentialSizePerformanceRepository.save(essentialSizePerformance);

        log.info("Save new EssentialSizePerformance[id: {}] process end", essentialSizePerformanceRequestDto.getId());
    }

    @Override
    public List<EssentialSizePerformanceDto> fetchPerformanceMateriality(Long projectId) {

        log.info("Fetch Performance Materiality for order[id:{}] process begins", projectId);

        List<EssentialSizePerformance> essentialSizePerformanceList = essentialSizePerformanceRepository
                .findByEssentialSize_Project_Id(projectId);

        List<EssentialSizePerformanceDto> essentialSizePerformanceDtoList = essentialSizePerformanceList.stream()
                .map(essentialSizePerformance -> conversionService.convert(essentialSizePerformance,
                        EssentialSizePerformanceDto.class))
                .collect(Collectors.toList());

        log.info("Fetch Performance Materiality for order[id:{}] process end", projectId);

        return essentialSizePerformanceDtoList;
    }

    private EssentialSize initialSave(Project project, Locale locale) {

        EssentialSize essentialSize = EssentialSize.builder()
                .project(project)
                .build();

        List<BaseResponseDto> baseResponseDtoList = fetchBaseList(project.getId(), locale);

        List<EssentialSizeOverall> essentialSizeOverallList = baseResponseDtoList.stream()
                .map(baseResponseDto -> {

                    Base base = fetchBase(baseResponseDto.getId());

                    double amount = baseResponseDto.getAmount();

                    Map<String, Double> limits = getLimits(base.getId(), amount);

                    return EssentialSizeOverall.builder()
                            .base(base)
                            .interimBaseAmount(NumericUtils.roundNumberToCentimeters(baseResponseDto.getAmount()))
                            .minLimit(NumericUtils.roundNumberToCentimeters(limits.get(MIN_LIMIT)))
                            .maxLimit(NumericUtils.roundNumberToCentimeters(limits.get(MAX_LIMIT)))
                            .essentialSize(essentialSize)
                            .build();
                })
                .collect(Collectors.toList());

        List<EssentialSizePerformance> essentialSizePerformanceList = Stream
                .of(EssentialSizePerformance.builder()
                        .year(project.getYear())
                        .percentage(65.0)
                        .essentialSize(essentialSize)
                        .build())
                .collect(Collectors.toList());

        essentialSize.setEssentialSizeOverallList(essentialSizeOverallList);
        essentialSize.setEssentialSizePerformanceList(essentialSizePerformanceList);

        return essentialSizeRepository.save(essentialSize);
    }

    private void saveOverAmount(EssentialSizeOverall essentialSizeOverall, EssentialSize essentialSize){

        essentialSizeOverallRepository.save(essentialSizeOverall);

        EssentialSizePerformance essentialSizePerformance = buildEssentialSizePerformance(essentialSize);
        essentialSizePerformance.setOverAmount(essentialSizeOverall.getOverAmount());
        if (!ObjectUtils.isEmpty(essentialSizePerformance.getPercentage())){

            essentialSizePerformance.setPerAmount(NumericUtils.roundNumberToCentimeters(
                    essentialSizePerformance.getOverAmount() * essentialSizePerformance.getPercentage() / 100));
            essentialSizePerformance = essentialSizePerformanceRepository.save(essentialSizePerformance);
            Optional<ImportantAccount> importantAccountOptional = importantAccountRepository
                    .findImportantAccountByProject(essentialSize.getProject());
            EssentialSizePerformance finalEssentialSizePerformance = essentialSizePerformance;
            importantAccountOptional.ifPresent(importantAccount -> {

                importantAccount.setPerAmount(finalEssentialSizePerformance.getPerAmount());
                importantAccountRepository.save(importantAccount);
                Optional<ImportantAccountRedis> importantAccountRedisOptional = importantAccountRedisRepository
                        .findById(importantAccount.getId());
                importantAccountRedisOptional.ifPresent(importantAccountRedis -> {

                    importantAccountRedis.setPerAmount(finalEssentialSizePerformance.getPerAmount());
                    importantAccountRedisRepository.save(importantAccountRedis);
                });
            });
        }

        essentialSize.setOverAmount(essentialSizeOverall.getOverAmount());
        essentialSizeRepository.save(essentialSize);
    }

    private Map<String, Double> getLimits(Integer baseId, Double amount){

        double minimumLimit = 0.0;
        double maximumLimit = 0.0;

        switch (baseId){

            case 1:

            case 4:

            case 3:
                minimumLimit = amount * 0.01;
                maximumLimit = amount * 0.03;
                break;

            case 5:
                minimumLimit = amount * 0.05;
                maximumLimit = amount * 0.1;
                break;

            case 2:
                minimumLimit = amount * 0.03;
                maximumLimit = amount * 0.05;
                break;
        }

        return Collections.unmodifiableMap(Stream
                .of(new AbstractMap.SimpleEntry<>(MIN_LIMIT, minimumLimit),
                        new AbstractMap.SimpleEntry<>(MAX_LIMIT, maximumLimit))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));
    }

    private List<MaterialityBaseDto> buildMaterialityBaseDtoList(List<MaterialityBase> materialityBaseList){

        List<MaterialityBaseDto> materialityBaseDtoList = new ArrayList<>();

        materialityBaseList.forEach(materialityBaseOrder -> {

            BalanceSheetDictionary balanceSheetDictionary = !ObjectUtils
                    .isEmpty(materialityBaseOrder.getAdd()) ?
                    materialityBaseOrder.getAdd() : materialityBaseOrder.getSubtract();

            Double amount = balanceSheetDictionary.getAmount();

            if (!ObjectUtils.isEmpty(amount))
                materialityBaseDtoList.add(MaterialityBaseDto.builder()
                        .materialityBaseId(materialityBaseOrder.getId())
                        .lineId(balanceSheetDictionary.getId())
                        .lineDescription(balanceSheetDictionary.getLine())
                        .amount(amount)
                        .build());
        });

        return materialityBaseDtoList;
    }

    private double buildAmount(List<MaterialityBase> materialityBaseOrderList){

        double amount = 0.0;

        for (MaterialityBase materialityBase: materialityBaseOrderList){

            if (!ObjectUtils.isEmpty(materialityBase.getAdd()))
                amount += materialityBase.getAdd().getAmount();

            else
                amount -= materialityBase.getSubtract().getAmount();

        }

        return amount;
    }

    private Base fetchBase(Integer id){

        Optional<Base> base = baseRepository
                .findById(id);

        if (!base.isPresent())
            throw new ResourceNotFoundException("There is not base with id: " + id);

        return base.get();
    }

    private EssentialSize buildEssentialSize(Long projectId){

        Optional<EssentialSize> essentialSize = essentialSizeRepository
                .findEssentialSizeByProject(Project.builder().id(projectId).build());

        if (!essentialSize.isPresent())
            throw new ResourceNotFoundException("There is not essential-size in order: " + projectId);

        return essentialSize.get();
    }

    private EssentialSizePerformance buildEssentialSizePerformance(EssentialSize essentialSize){

        Optional<EssentialSizePerformance> essentialSizePerformance = essentialSizePerformanceRepository
                .findByEssentialSize(essentialSize);

        if (!essentialSizePerformance.isPresent())
            throw new ResourceNotFoundException("There is not essential-size-performance in order: " +
                    essentialSize.getProject().getId());

        return essentialSizePerformance.get();
    }
}
