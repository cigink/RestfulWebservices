package buildit.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import buildit.models.Plant;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {

	List<Plant> findByNameLike(String name);

	// /*@Query("select p from Plant p "
	// +
	// "where LOWER(p.name) like LOWER(:name) and p.price between :minimum and :maximum")
	// List<Plant> finderMethod(@Param("name") String name,
	// @Param("minimum") Float minimum, @Param("maximum") Float maximum);*/

	/*
	 * @Query("select p from Plant p " +
	 * "where LOWER(p.name) like LOWER(:name) and p NOT in (select phr.plant from PlantHireRequest phr "
	 * +
	 * "where phr.startDate >= :date1 and phr.startDate <= :date2  or phr.endDate >= :date1 and phr.endDate <= :date2)"
	 * ) List<Plant> findAllAvailable(@Param("name") String name,
	 * 
	 * @Param("date1") Date date1, @Param("date2") Date date2);
	 */

	/*
	 * @Query("select p from Plant p " +
	 * "where LOWER(p.name) like LOWER(:name) and p NOT in (select phr.plant from PlantHireRequest phr"
	 * +
	 * " where phr.startDate between date(:date1) and  date(:date2) or phr.endDate between date(:date1) and date(:date2))"
	 * ) List<Plant> isPlantAvailable(@Param("name") String name,
	 *
	 * @Param("date1") Date date, @Param("date2") Date date2);
	 */
}
