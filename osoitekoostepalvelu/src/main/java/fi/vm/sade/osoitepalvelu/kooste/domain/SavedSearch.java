/*
 * Copyright (c) 2013 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.osoitepalvelu.kooste.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;

import fi.vm.sade.osoitepalvelu.kooste.common.util.EqualsHelper;


public class SavedSearch implements Serializable, Comparable<SavedSearch> {
    private static final long serialVersionUID  =  4164512181923649287L;
    private static final int HASH_FACTOR = 31;

    public enum SaveType {
        EMAIL,
        SEND_LETTER,
        LETTER,
        CONTACT;
    }

    @Id
    private Long id;
    private String name;
    // index
    private String ownerUserOid;
    private SaveType searchType;
    private DateTime createdAt;
    private Set<String> addressFields;
    private Set<SearchTargetGroup> targetGroups;
    private Set<SearchTerm> terms;
    private String lang;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id  =  id;
    }

    public String getOwnerUserOid() {
        return ownerUserOid;
    }

    public void setOwnerUserOid(String ownerUserOid) {
        this.ownerUserOid  =  ownerUserOid;
    }

    public Set<String> getAddressFields() {
        return addressFields;
    }

    public void setAddressFields(Set<String> addressFields) {
        this.addressFields  =  addressFields;
    }

    public Set<SearchTargetGroup> getTargetGroups() {
        return targetGroups;
    }

    public void setTargetGroups(Set<SearchTargetGroup> targetGroups) {
        this.targetGroups  =  targetGroups;
    }

    public Set<SearchTerm> getTerms() {
        return terms;
    }

    public void setTerms(Set<SearchTerm> terms) {
        this.terms  =  terms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name  =  name;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt  =  createdAt;
    }

    public SaveType getSearchType() {
        return searchType;
    }

    public void setSearchType(SaveType searchType) {
        this.searchType  =  searchType;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public int compareTo(SavedSearch o) {
        return this.getCreatedAt().compareTo(o.getCreatedAt());
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = HASH_FACTOR * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = HASH_FACTOR * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {            
            return true;
        }
        if (obj == null) {            
            return false;
        }
        if (!(obj instanceof SavedSearch)) {
            return false;
        }
        SavedSearch other = (SavedSearch) obj;
        return EqualsHelper.areEquals(this.id, other.id)
                && EqualsHelper.areEquals(this.createdAt, other.createdAt);
    }
}
