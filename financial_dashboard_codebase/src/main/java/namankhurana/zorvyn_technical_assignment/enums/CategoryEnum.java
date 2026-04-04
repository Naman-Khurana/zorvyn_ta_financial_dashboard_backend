package namankhurana.zorvyn_technical_assignment.enums;


public enum CategoryEnum {

    PRODUCT_SALES(RecordTypeEnum.INCOME),
    SERVICE_REVENUE(RecordTypeEnum.INCOME),
    SUBSCRIPTION_REVENUE(RecordTypeEnum.INCOME),
    CONSULTING_REVENUE(RecordTypeEnum.INCOME),
    LICENSING_REVENUE(RecordTypeEnum.INCOME),
    INVESTMENT_INCOME(RecordTypeEnum.INCOME),
    INTEREST_INCOME(RecordTypeEnum.INCOME),
    OTHER_INCOME(RecordTypeEnum.INCOME),

    SALARIES(RecordTypeEnum.EXPENSE),
    OFFICE_RENT(RecordTypeEnum.EXPENSE),
    UTILITIES(RecordTypeEnum.EXPENSE),
    SOFTWARE_SUBSCRIPTIONS(RecordTypeEnum.EXPENSE),
    MARKETING(RecordTypeEnum.EXPENSE),
    ADVERTISING(RecordTypeEnum.EXPENSE),
    TRAVEL_EXPENSE(RecordTypeEnum.EXPENSE),
    EQUIPMENT(RecordTypeEnum.EXPENSE),
    MAINTENANCE(RecordTypeEnum.EXPENSE),
    TAX(RecordTypeEnum.EXPENSE),
    INSURANCE(RecordTypeEnum.EXPENSE),
    LEGAL_FEES(RecordTypeEnum.EXPENSE),
    CONSULTING_FEES(RecordTypeEnum.EXPENSE),
    TRAINING(RecordTypeEnum.EXPENSE),
    OFFICE_SUPPLIES(RecordTypeEnum.EXPENSE),
    OTHER_EXPENSE(RecordTypeEnum.EXPENSE);

    private final RecordTypeEnum type;

    CategoryEnum(RecordTypeEnum type) {
        this.type = type;
    }

    public RecordTypeEnum getType() {
        return type;
    }
}
