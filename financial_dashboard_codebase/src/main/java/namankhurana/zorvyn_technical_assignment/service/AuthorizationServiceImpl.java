package namankhurana.zorvyn_technical_assignment.service;

import namankhurana.zorvyn_technical_assignment.entity.User;
import namankhurana.zorvyn_technical_assignment.enums.RolesEnum;
import namankhurana.zorvyn_technical_assignment.exception.ForbiddenResourceException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements AuthorizationService{



    @Override
    public void checkAdmin(User user) {
        if (user.getRole().getName() != RolesEnum.ADMIN) {
            throw new ForbiddenResourceException("Admin access required");
        }
    }


    @Override
    // if not admin or analyst itself ->  forbidden
    public void checkAnalystOrAdmin(User currentUser, Long ownerId) {
        boolean isAdmin = currentUser.getRole().getName() == RolesEnum.ADMIN;
        boolean isAnalyst = currentUser.getRole().getName() == RolesEnum.ANALYST;

        if (!isAdmin && !isAnalyst) {
            throw new ForbiddenResourceException("User not allowed to access this resource");
        }
    }

}




