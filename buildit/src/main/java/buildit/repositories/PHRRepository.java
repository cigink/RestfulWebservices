package buildit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import buildit.models.PlantHireRequest;

@Repository
public interface PHRRepository extends
		JpaRepository<PlantHireRequest, Long> {

}
