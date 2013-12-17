package fi.vm.sade.osoitepalvelu.service.dto;

public class OrganisaatioOid {
	private String value;

	public OrganisaatioOid() {
	}

	public OrganisaatioOid(String oid) {
		this.value = oid;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrganisaatioOid other = (OrganisaatioOid) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}	

	@Override
	public String toString() {
		return this.value;
	}
}