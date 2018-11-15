package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the Modules database table.
 * 
 */
@Entity
@Table(name="Modules")
@NamedQuery(name="Module.findAll", query="SELECT m FROM Module m")
public class Module implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_module")
	private int idModule;

	private String name;

	//bi-directional many-to-one association to Section
	@OneToMany(mappedBy="moduleBean")
	private List<Section> sections;

	public Module() {
	}

	public int getIdModule() {
		return this.idModule;
	}

	public void setIdModule(int idModule) {
		this.idModule = idModule;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Section> getSections() {
		return this.sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	public Section addSection(Section section) {
		getSections().add(section);
		section.setModuleBean(this);

		return section;
	}

	public Section removeSection(Section section) {
		getSections().remove(section);
		section.setModuleBean(null);

		return section;
	}

}