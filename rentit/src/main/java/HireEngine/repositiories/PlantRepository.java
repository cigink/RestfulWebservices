package HireEngine.repositiories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import HireEngine.Models.Plant;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {

	List<Plant> findByNameLike(String name);

	@Query("select p from Plant p "
			+ "where LOWER(p.name) like LOWER(:name) and p.price between :minimum and :maximum")
	List<Plant> finderMethod(@Param("name") String name,
			@Param("minimum") Float minimum, @Param("maximum") Float maximum);

	@Query("select p from Plant p "
			+ "where LOWER(p.name) like LOWER(:name) and p NOT in (select po.plant from PurchaseOrder po"
			+ " where po.startDate between date(:date1) and  date(:date2) or po.endDate between date(:date1) and date(:date2))")
	List<Plant> isPlantAvailable(@Param("name") String name,
			@Param("date1") Date date, @Param("date2") Date date2);
	
	@Query("select p from Plant p "
			+ "where LOWER(p.name) like LOWER(:name) and p NOT in (select po.plant from PurchaseOrder po"
			+ " where po.startDate between date(:date1) and  date(:date2) or po.endDate between date(:date1) and date(:date2))")
	Plant findPlant(@Param("name") String name,
			@Param("date1") Date date, @Param("date2") Date date2);
}