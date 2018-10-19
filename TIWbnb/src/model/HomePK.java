package model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the HOME database table.
 * 
 */
@Embeddable
public class HomePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="HOME_NAME")
	private String homeName;

	@Column(name="HOME_EMAIL", insertable=false, updatable=false)
	private String homeEmail;

	public HomePK() {
	}
	public String getHomeName() {
		return this.homeName;
	}
	public void setHomeName(String homeName) {
		this.homeName = homeName;
	}
	public String getHomeEmail() {
		return this.homeEmail;
	}
	public void setHomeEmail(String homeEmail) {
		this.homeEmail = homeEmail;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof HomePK)) {
			return false;
		}
		HomePK castOther = (HomePK)other;
		return 
			this.homeName.equals(castOther.homeName)
			&& this.homeEmail.equals(castOther.homeEmail);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.homeName.hashCode();
		hash = hash * prime + this.homeEmail.hashCode();
		
		return hash;
	}
}