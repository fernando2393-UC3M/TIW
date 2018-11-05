package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ADMIN database table.
 * 
 */
@Entity
@Table(name="ADMIN")
@NamedQuery(name="Admin.findAll", query="SELECT a FROM Admin a")
public class Admin implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ADMIN_EMAIL")
	private String adminEmail;

	public Admin() {
	}

	public String getAdminEmail() {
		return this.adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

}