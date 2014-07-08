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

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 12/10/13
 * Time: 1:23 PM
 */
@Document(collection  =  "savedSearches")
public class SavedSearch implements Serializable, Comparable<SavedSearch> {
    private static final long serialVersionUID  =  4164512181923649287L;

    public enum SaveType {
        EMAIL,
        SEND_LETTER,
        LETTER,
        CONTACT;
    }

    @Id
    private Long id;
    private String name;
    @Indexed
    private String ownerUserOid;
    private SaveType searchType;
    private DateTime createdAt = new DateTime();
    private List<String> addressFields  =  new ArrayList<String>();
    private List<String> receiverFields  =  new ArrayList<String>();
    private List<SearchTargetGroup> targetGroups  =  new ArrayList<SearchTargetGroup>();
    private List<SearchTerm> terms  =  new ArrayList<SearchTerm>();
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

    public List<String> getAddressFields() {
        return addressFields;
    }

    public void setAddressFields(List<String> addressFields) {
        this.addressFields  =  addressFields;
    }

    public List<SearchTargetGroup> getTargetGroups() {
        return targetGroups;
    }

    public void setTargetGroups(List<SearchTargetGroup> targetGroups) {
        this.targetGroups  =  targetGroups;
    }

    public List<SearchTerm> getTerms() {
        return terms;
    }

    public void setTerms(List<SearchTerm> terms) {
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

    public List<String> getReceiverFields() {
        return receiverFields;
    }

    public void setReceiverFields(List<String> receiverFields) {
        this.receiverFields  =  receiverFields;
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
}
