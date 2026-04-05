package namankhurana.zorvyn_technical_assignment.service;

import namankhurana.zorvyn_technical_assignment.dto.CategoryWiseRecordDTO;
import namankhurana.zorvyn_technical_assignment.dto.DashboardSummaryDTO;
import namankhurana.zorvyn_technical_assignment.dto.TrendsDTO;
import namankhurana.zorvyn_technical_assignment.dto.entity.FinancialRecordDTO;
import namankhurana.zorvyn_technical_assignment.enums.CategoryEnum;
import namankhurana.zorvyn_technical_assignment.enums.DashboardSummaryTypeEnum;
import namankhurana.zorvyn_technical_assignment.enums.RecordTypeEnum;
import namankhurana.zorvyn_technical_assignment.enums.TrendTypeEnum;
import namankhurana.zorvyn_technical_assignment.exception.BadRequestException;
import namankhurana.zorvyn_technical_assignment.mapper.FinancialRecordMapper;
import namankhurana.zorvyn_technical_assignment.repository.FinancialRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
public class DashboardServiceImpl implements DashboardService {

    private final UserService userService;
    private final FinancialRecordService financialRecordService;
    private final FinancialRecordRepository financialRecordRepository;
    private final FinancialRecordMapper financialRecordMapper;

    @Autowired
    public DashboardServiceImpl(UserService userService, FinancialRecordService financialRecordService, FinancialRecordRepository financialRecordRepository, FinancialRecordMapper financialRecordMapper) {
        this.userService = userService;
        this.financialRecordService = financialRecordService;

        this.financialRecordRepository = financialRecordRepository;
        this.financialRecordMapper = financialRecordMapper;
    }

    @Override
    public DashboardSummaryDTO getTotalIncome() {

        return DashboardSummaryDTO.builder()
                .summaryType(DashboardSummaryTypeEnum.TOTAL_INCOME)
                .value(financialRecordRepository.getTotalByType(RecordTypeEnum.INCOME))
                .build();

    }

    @Override
    public DashboardSummaryDTO getTotalExpenses() {

        return DashboardSummaryDTO.builder()
                .summaryType(DashboardSummaryTypeEnum.TOTAL_EXPENSE)
                .value(financialRecordRepository.getTotalByType(RecordTypeEnum.INCOME))
                .build();

    }

    @Override
    public DashboardSummaryDTO getNetBalance() {
        BigDecimal netBalance = financialRecordRepository.getTotalByType(RecordTypeEnum.INCOME)
                .subtract(financialRecordRepository.getTotalByType(RecordTypeEnum.EXPENSE));

        return DashboardSummaryDTO.builder()
                .summaryType(DashboardSummaryTypeEnum.NET_BALANCE)
                .value(netBalance)
                .build();

    }

    // get category wise income / earnings
    @Override
    public List<CategoryWiseRecordDTO> getCategoryWiseTotal() {


        Map<CategoryEnum, BigDecimal> categoryWiseTotals =
                financialRecordRepository.getCategorySummary()
                        .stream()
                        .collect(Collectors.toMap(
                                CategoryWiseRecordDTO::getCategory,
                                CategoryWiseRecordDTO::getTotalAmount
                        ));


        return Arrays.stream(CategoryEnum.values())
                .map(category -> {
                    BigDecimal amount = categoryWiseTotals.getOrDefault(category, BigDecimal.ZERO);

                    CategoryWiseRecordDTO dto =
                            new CategoryWiseRecordDTO(category, amount);

                    dto.setType(category.getType());

                    return dto;
                })
                .toList();

    }

    // get most recent n financial records
    @Override
    public List<FinancialRecordDTO> getRecentNActivities(Long limit) {
        // handle limit not passed :: Default 10
        limit = (limit == null ? 10L : limit);

        Pageable pageable = PageRequest.of(
                0,
                Math.toIntExact(limit),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return financialRecordRepository
                .findAll(pageable).map(financialRecordMapper::toDTO)
                .stream()
                .toList();


    }


    // get last n weeks / months trends (income,expenses)
    @Override
    public List<TrendsDTO> getTrends(TrendTypeEnum type, Integer maxLimit) {

        type = (type == null ? TrendTypeEnum.MONTHLY : type);

        // default values
        if (maxLimit == null) {
            maxLimit = (type == TrendTypeEnum.MONTHLY ? 12 : 4);
        }
        // handle n < 1
        else if(maxLimit<1){
            throw new BadRequestException("'n' should have value of atleast 1 ");
        }


        // Get all aggregated data (NOT paged)
        List<TrendsDTO> trends;
        switch (type) {
            case WEEKLY -> trends = financialRecordRepository.getWeeklyTrends(PageRequest.of(0, maxLimit));
            case MONTHLY -> trends = financialRecordRepository.getMonthlyTrends(PageRequest.of(0, maxLimit));
            default -> throw new IllegalArgumentException("Invalid Trend Type");
        }

        Map<String, TrendsDTO> trendsMap = trends.stream()
                .collect(Collectors.toMap(TrendsDTO::getPeriod, t -> t));

        List<TrendsDTO> finalTrends = new ArrayList<>();

        if (type == TrendTypeEnum.MONTHLY) {
            YearMonth now = YearMonth.now();
            for (int i = 0; i < maxLimit; i++) {
                YearMonth month = now.minusMonths(i);
                String key = month.toString();
                finalTrends.add(
                        trendsMap.getOrDefault(key, new TrendsDTO(key, BigDecimal.ZERO, BigDecimal.ZERO)));
            }

        } else { // for weekly trends
            LocalDate now = LocalDate.now();
            for (int i = 0; i < maxLimit; i++) {
                LocalDate weekDate = now.minusWeeks(i);
                int week = weekDate.get(WeekFields.ISO.weekOfWeekBasedYear());
                int year = weekDate.getYear();
                String key = year + "-W" + week;

                finalTrends.add(
                        trendsMap.getOrDefault(key, new TrendsDTO(key, BigDecimal.ZERO, BigDecimal.ZERO)));
            }
        }

        Collections.reverse(finalTrends);


        return finalTrends;
    }

}
