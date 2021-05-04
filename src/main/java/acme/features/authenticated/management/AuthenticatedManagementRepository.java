package acme.features.authenticated.management;


import acme.entities.roles.Management;
import acme.framework.entities.UserAccount;
import acme.framework.repositories.AbstractRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticatedManagementRepository extends AbstractRepository {

    @Query("select ua from UserAccount ua where ua.id = ?1")
    UserAccount findOneUserAccountById(int id);

    @Query("select m from Management m where m.userAccount.id = ?1")
    Management findOneManagementByUserAccountId(int id);
}
