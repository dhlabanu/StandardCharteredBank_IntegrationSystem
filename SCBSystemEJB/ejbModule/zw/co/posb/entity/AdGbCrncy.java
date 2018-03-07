package zw.co.posb.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "ad_gb_crncy")
@NamedQuery(name = "AdGbCrncy.findAll", query = "SELECT a FROM AdGbCrncy a")
public class AdGbCrncy implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "crncy_id")
	private short crncyId;

	@Column(name = "iso_code")
	private String isoCode;

	public AdGbCrncy() {
	}

	public short getCrncyId() {
		return crncyId;
	}

	public void setCrncyId(short crncyId) {
		this.crncyId = crncyId;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

}