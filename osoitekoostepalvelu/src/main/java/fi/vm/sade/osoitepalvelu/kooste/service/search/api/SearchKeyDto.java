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

package fi.vm.sade.osoitepalvelu.kooste.service.search.api;

import java.io.Serializable;

public class SearchKeyDto implements Serializable, Comparable<SearchKeyDto> {

    private static final long serialVersionUID  =  -7866357718772030725L;
    
    private String koodistoUri;
    private String operaattori;
    
    public SearchKeyDto() {
    }
    
    public SearchKeyDto(String koodistoUri, String operaattori) {
        this.koodistoUri  =  koodistoUri;
        this.operaattori  =  operaattori;
    }
    
    public String getKoodistoUri() {
        return koodistoUri;
    }
    
    public void setKoodistoUri(String koodistoUri) {
        this.koodistoUri  =  koodistoUri;
    }
    
    public String getOperaattori() {
        return operaattori;
    }
    
    public void setOperaattori(String operaattori) {
        this.operaattori  =  operaattori;
    }

    @Override
    public int compareTo(SearchKeyDto o) {
        return koodistoUri.compareTo(o.getKoodistoUri());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof SearchKeyDto) {
            return (this.koodistoUri  +  this.operaattori).equals(((SearchKeyDto) obj).getKoodistoUri()
                     +  ((SearchKeyDto) obj).getOperaattori());
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return (this.koodistoUri + this.operaattori).hashCode();
    }    
}
