package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the Sections database table.
 * 
 */
@Entity
@Table(name="Sections")
@NamedQuery(name="Section.findAll", query="SELECT s FROM Section s")
public class Section implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_section")
	private int idSection;

	private String name;

	//bi-directional many-to-one association to Cours
	@OneToMany(mappedBy="sectionBean")
	private List<Cours> courses;

	//bi-directional many-to-one association to Module
	@ManyToOne
	@JoinColumn(name="module")
	private Module moduleBean;

	public Section() {
	}

	public int getIdSection() {
		return this.idSection;
	}

	public void setIdSection(int idSection) {
		this.idSection = idSection;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Cours> getCourses() {
		return this.courses;
	}

	public void setCourses(List<Cours> courses) {
		this.courses = courses;
	}

	public Cours addCours(Cours cours) {
		getCourses().add(cours);
		cours.setSectionBean(this);

		return cours;
	}

	public Cours removeCours(Cours cours) {
		getCourses().remove(cours);
		cours.setSectionBean(null);

		return cours;
	}

	public Module getModuleBean() {
		return this.moduleBean;
	}

	public void setModuleBean(Module moduleBean) {
		this.moduleBean = moduleBean;
	}

}