package namankhurana.zorvyn_technical_assignment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecordTypeEnum {
    EXPENSE("EXPENSE"),
    INCOME("INCOME");

    private final String value;


}
