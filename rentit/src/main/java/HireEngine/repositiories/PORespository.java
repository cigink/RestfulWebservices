package HireEngine.repositiories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import HireEngine.Models.PurchaseOrder;

@Repository
public interface PORespository extends JpaRepository<PurchaseOrder, Long> {

//	// @Override
//	// @Query("select po from PurchaseOrder po where po.id = :id")
//	// PurchaseOrder findOne(@Param("id") Long id);
//
//	@Modifying
//	@Query("select p from PurchaseOrder p where p.startDate <:date2 and p.endDate > :date1")
//	List<PurchaseOrder> inBetween(@Param("date1") Date date1,
//			@Param("date2") Date date2);
}