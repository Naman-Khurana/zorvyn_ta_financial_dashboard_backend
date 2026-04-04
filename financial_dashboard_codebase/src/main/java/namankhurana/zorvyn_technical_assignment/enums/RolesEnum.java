package namankhurana.zorvyn_technical_assignment.enums;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RolesEnum {
    ADMIN("ADMIN"),
    ANALYST("ANALYST"),
    VIEWER("VIEWER");

    private final String value;
}
