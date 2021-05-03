package acme.features.authenticated.manager;


import acme.entities.roles.Manager;
import acme.framework.entities.UserAccount;
import acme.framework.repositories.AbstractRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticatedManagerRepository extends AbstractRepository {

    @Query("select ua from UserAccount ua where ua.id = ?1")
    UserAccount findOneUserAccountById(int id);

    @Query("select m from Manager m where m.userAccount.id = ?1")
    Manager findOneManagerByUserAccountId(int id);
}
