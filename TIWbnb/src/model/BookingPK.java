package model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the BOOKING database table.
 * 
 */
@Embeddable
public class BookingPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="BOOKING_EMAIL_USER", insertable=false, updatable=false)
	private String bookingEmailUser;

	@Column(name="BOOKING_NAME_HOME", insertable=false, updatable=false)
	private String bookingNameHome;

	@Column(name="BOOKING_EMAIL_HOME", insertable=false, updatable=false)
	private String bookingEmailHome;

	public BookingPK() {
	}
	public String getBookingEmailUser() {
		return this.bookingEmailUser;
	}
	public void setBookingEmailUser(String bookingEmailUser) {
		this.bookingEmailUser = bookingEmailUser;
	}
	public String getBookingNameHome() {
		return this.bookingNameHome;
	}
	public void setBookingNameHome(String bookingNameHome) {
		this.bookingNameHome = bookingNameHome;
	}
	public String getBookingEmailHome() {
		return this.bookingEmailHome;
	}
	public void setBookingEmailHome(String bookingEmailHome) {
		this.bookingEmailHome = bookingEmailHome;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BookingPK)) {
			return false;
		}
		BookingPK castOther = (BookingPK)other;
		return 
			this.bookingEmailUser.equals(castOther.bookingEmailUser)
			&& this.bookingNameHome.equals(castOther.bookingNameHome)
			&& this.bookingEmailHome.equals(castOther.bookingEmailHome);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.bookingEmailUser.hashCode();
		hash = hash * prime + this.bookingNameHome.hashCode();
		hash = hash * prime + this.bookingEmailHome.hashCode();
		
		return hash;
	}
}