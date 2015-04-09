package buildit.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class WorksEngineer {
	@Id
	@GeneratedValue
	Long id;
	String name;
}
