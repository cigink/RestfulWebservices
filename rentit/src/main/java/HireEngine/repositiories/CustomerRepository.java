package HireEngine.repositiories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import HireEngine.Models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
