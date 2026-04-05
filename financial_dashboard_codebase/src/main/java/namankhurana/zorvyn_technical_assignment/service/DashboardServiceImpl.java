package namankhurana.zorvyn_technical_assignment.service;

import namankhurana.zorvyn_technical_assignment.dto.CategoryWiseRecordDTO;
import namankhurana.zorvyn_technical_assignment.dto.DashboardSummaryDTO;
import namankhurana.zorvyn_technical_assignment.dto.TrendsDTO;
import namankhurana.zorvyn_technical_assignment.dto.entity.FinancialRecordDTO;
import namankhurana.zorvyn_technical_assignment.enums.CategoryEnum;
import namankhurana.zorvyn_technical_assignment.enums.DashboardSummaryTypeEnum;
import namankhurana.zorvyn_technical_assignment.enums.RecordTypeEnum;
import namankhurana.zorvyn_technical_assignment.enums.TrendTypeEnum;
import namankhurana.zorvyn_technical_assignment.mapper.FinancialRecordMapper;
import namankhurana.zorvyn_technical_assignment.repository.FinancialRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
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
        BigDecimal netBalance= financialRecordRepository.getTotalByType(RecordTypeEnum.INCOME)
                .subtract(financialRecordRepository.getTotalByType(RecordTypeEnum.EXPENSE));

        return DashboardSummaryDTO.builder()
                .summaryType(DashboardSummaryTypeEnum.NET_BALANCE)
                .value(netBalance)
                .build();

    }

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

    @Override
    public Page<FinancialRecordDTO> getRecentNActivities(Long limit) {
        // handle limit not passed :: Default 10
        limit = (limit == null ? 10L : limit);

        Pageable pageable = PageRequest.of(
                0,
                Math.toIntExact(limit),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return financialRecordRepository
                .findAll(pageable).map(financialRecordMapper::toDTO);


    }

    @Override
    public List<TrendsDTO> getTrends(TrendTypeEnum type, Integer maxLimit) {

        // default type -> Monthly
        type = (type == null ? TrendTypeEnum.MONTHLY : type);

        // default limit Monthly -> Last 12 Months , Weekly -> Last 4 Weeks
        if (maxLimit == null) {
            maxLimit = (type == TrendTypeEnum.MONTHLY ? 12 : 4);
        }

        Pageable pageable=PageRequest.of(0,maxLimit);

        List<TrendsDTO> trends;
        switch (type){
            case WEEKLY -> trends=financialRecordRepository.getWeeklyTrends(pageable);
            case MONTHLY -> trends=financialRecordRepository.getMonthlyTrends(pageable);
            default -> throw new IllegalArgumentException("Invalid Trend Type");
        }

        // reverse since it store most recent to least recent by default
        Collections.reverse(trends);
        return  trends;


    }

}
