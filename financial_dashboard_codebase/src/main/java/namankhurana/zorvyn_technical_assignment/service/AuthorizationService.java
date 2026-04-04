package namankhurana.zorvyn_technical_assignment.service;

import namankhurana.zorvyn_technical_assignment.entity.User;

public interface AuthorizationService {

    void checkAdmin(User user);

    void checkAnalystOrAdmin(User currentUser, Long ownerId);

}
