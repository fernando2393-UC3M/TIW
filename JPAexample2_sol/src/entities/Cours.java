package entities;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the Courses database table.
 * 
 */
@Entity
@Table(name="Courses")
@NamedQuery(name="Cours.findAll", query="SELECT c FROM Cours c")
public class Cours implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_course")
	private int idCourse;

	private String name;

	//bi-directional many-to-one association to Section
	@ManyToOne
	@JoinColumn(name="section")
	private Section sectionBean;

	//bi-directional many-to-many association to Object
	@ManyToMany(mappedBy="courses", cascade = {CascadeType.PERSIST})
	private List<LObject> objects;

	public Cours() {
	}

	public int getIdCourse() {
		return this.idCourse;
	}

	public void setIdCourse(int idCourse) {
		this.idCourse = idCourse;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Section getSectionBean() {
		return this.sectionBean;
	}

	public void setSectionBean(Section sectionBean) {
		this.sectionBean = sectionBean;
	}

	public List<LObject> getObjects() {
		return this.objects;
	}

	public void setObjects(List<LObject> objects) {
		this.objects = objects;
	}

}