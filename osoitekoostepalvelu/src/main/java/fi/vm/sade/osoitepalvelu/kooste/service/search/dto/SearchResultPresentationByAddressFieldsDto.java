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

package fi.vm.sade.osoitepalvelu.kooste.service.search.dto;

import fi.ratamaa.dtoconverter.reflection.Property;
import fi.vm.sade.osoitepalvelu.kooste.domain.SearchTargetGroup;
import fi.vm.sade.osoitepalvelu.kooste.service.search.SearchResultPresentation;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;
import java.util.*;

/**
 * User: ratamaa
 * Date: 2/17/14
 * Time: 10:31 AM
 */
public class SearchResultPresentationByAddressFieldsDto implements SearchResultPresentation, Serializable {
    private static final long serialVersionUID  =  -9202420166871480974L;
    
    private static MultiValueMap<String, String> fieldMappings;
    static {
        fieldMappings  =  new LinkedMultiValueMap<String, String>();
        fieldMappings.add("ORGANIAATIO_NIMI", "organisaationNimiIncluded");
        fieldMappings.add("ORGANIAATIO_TUNNISTE", "organisaatiotunnisteIncluded");
        fieldMappings.add("YHTEYSHENKILO", "yhteyshenkiloIncluded");
        fieldMappings.add("POSTIOSOITE", "positosoiteIncluded");
        fieldMappings.add("KAYNTIOSOITE", "kayntiosoiteIncluded");
        fieldMappings.add("PUHELINNUMERO", "puhelinnumeroIncluded");
        fieldMappings.add("FAXINUMERO", "faksinumeroIncluded");
        fieldMappings.add("INTERNET_OSOITE", "wwwOsoiteIncluded");
        fieldMappings.add("EMAIL_OSOITE", "organisaatioEmailIncluded");
        fieldMappings.add("VIRANOMAISTIEDOTUS_EMAIL", "viranomaistiedotuksenSahkopostiosoiteIncluded");
        fieldMappings.add("KOULUTUSNEUVONNAN_EMAIL", "koulutusneuvonnanSahkopostiosoiteIncluded");
        fieldMappings.add("KRIISITIEDOTUKSEN_EMAIL", "kriisitiedotuksenSahkopostiosoiteIncluded");
        fieldMappings.add("ORGANISAATIO_SIJAINTIKUNTA", "organisaationSijaintikuntaIncluded");
    }
    public static List<String> fieldMappingKeys() {
        return new ArrayList<String>(fieldMappings.keySet());
    }

    private boolean organisaationNimiIncluded;
    private boolean organisaatiotunnisteIncluded;
    private boolean yhteyshenkiloIncluded;
    private boolean yhteyshenkiloEmailIncluded;
    private boolean positosoiteIncluded;
    private boolean kayntiosoiteIncluded;
    private boolean puhelinnumeroIncluded;
    private boolean faksinumeroIncluded;
    private boolean wwwOsoiteIncluded;
    private boolean viranomaistiedotuksenSahkopostiosoiteIncluded;
    private boolean koulutusneuvonnanSahkopostiosoiteIncluded;
    private boolean kriisitiedotuksenSahkopostiosoiteIncluded;
    private boolean organisaationSijaintikuntaIncluded;
    private boolean organisaatioEmailIncluded;
    private Locale locale;
    private Set<OidAndTyyppiPair> nonIncludedOids;
    private SearchTermsDto.SearchType searchType;

    public SearchResultPresentationByAddressFieldsDto(SearchTermsDto searchTerms) {
        if(searchTerms.getSearchType() != null) {
            this.searchType = searchTerms.getSearchType();
            switch (searchTerms.getSearchType()) {
                case CONTACT:
                    setOrganisaationNimiIncluded(searchTerms.containsAnyTargetGroup(SearchTargetGroup.GroupType.getAnyOrganisaatioTypes()));
                    setYhteyshenkiloIncluded(searchTerms.containsAnyTargetGroup(SearchTargetGroup.GroupType.getAnyHenkiloTypes()));
                    includeAddressFields(searchTerms.getAddressFields());
                    break;
                case EMAIL:
                    setOrganisaationNimiIncluded(searchTerms.containsAnyTargetGroup(SearchTargetGroup.GroupType.getAnyOrganisaatioTypes()));
                    setYhteyshenkiloIncluded(searchTerms.containsAnyTargetGroup(SearchTargetGroup.GroupType.getAnyHenkiloTypes()));
                    setYhteyshenkiloEmailIncluded(searchTerms.containsAnyTargetGroup(SearchTargetGroup.GroupType.getAnyHenkiloTypes()));
                    setOrganisaatioEmailIncluded(searchTerms.containsAnyTargetGroup(SearchTargetGroup.GroupType.getAnyOrganisaatioTypes(),
                                    SearchTargetGroup.TargetType.ORGANISAATIO));
                    break;
                case LETTER:
                case SEND_LETTER:
                    setOrganisaationNimiIncluded(searchTerms.containsAnyTargetGroup(SearchTargetGroup.GroupType.getAnyOrganisaatioTypes()));
                    setYhteyshenkiloIncluded(searchTerms.containsAnyTargetGroup(SearchTargetGroup.GroupType.getAnyHenkiloTypes()));
                    setPositosoiteIncluded(true);
                    break;
                default: break;
            }
        }
        this.locale  =  searchTerms.getLocale();
    }

    public SearchResultPresentationByAddressFieldsDto(SearchTermsDto searchTerms,
                                                       Set<OidAndTyyppiPair> nonIncludedOrganisaatioOidsStrings) {
        this(searchTerms);
        this.nonIncludedOids  =  nonIncludedOrganisaatioOidsStrings;
    }

    public void includeAddressFields(Collection<String> addressFields) {
        if (addressFields == null) {
            return;
        }
        for(String addressField : addressFields) {
            List<String> fields  =  fieldMappings.get(addressField);
            if (fields == null) {
                throw new IllegalStateException("Unknown field "  +  addressField);
            }
            for(String field : fields) {
                Property.getForName(getClass(), field).set(this, true);
            }
        }
    }

    @Override
    public boolean isOrganisaationNimiIncluded() {
        return organisaationNimiIncluded;
    }

    public void setOrganisaationNimiIncluded(boolean organisaationNimiIncluded) {
        this.organisaationNimiIncluded  =  organisaationNimiIncluded;
    }

    @Override
    public boolean isOrganisaatiotunnisteIncluded() {
        return organisaatiotunnisteIncluded;
    }

    public void setOrganisaatiotunnisteIncluded(boolean organisaatiotunnisteIncluded) {
        this.organisaatiotunnisteIncluded  =  organisaatiotunnisteIncluded;
    }

    @Override
    public boolean isYhteyshenkiloIncluded() {
        return yhteyshenkiloIncluded;
    }

    public void setYhteyshenkiloIncluded(boolean yhteyshenkiloIncluded) {
        this.yhteyshenkiloIncluded  =  yhteyshenkiloIncluded;
    }

    @Override
    public boolean isYhteyshenkiloEmailIncluded() {
        return yhteyshenkiloEmailIncluded;
    }

    public void setYhteyshenkiloEmailIncluded(boolean yhteyshenkiloEmailIncluded) {
        this.yhteyshenkiloEmailIncluded  =  yhteyshenkiloEmailIncluded;
    }

    @Override
    public boolean isPositosoiteIncluded() {
        return positosoiteIncluded;
    }

    public void setPositosoiteIncluded(boolean positosoiteIncluded) {
        this.positosoiteIncluded  =  positosoiteIncluded;
    }

    @Override
    public boolean isKayntiosoiteIncluded() {
        return kayntiosoiteIncluded;
    }

    public void setKayntiosoiteIncluded(boolean kayntiosoiteIncluded) {
        this.kayntiosoiteIncluded = kayntiosoiteIncluded;
    }

    @Override
    public boolean isPuhelinnumeroIncluded() {
        return puhelinnumeroIncluded;
    }

    public void setPuhelinnumeroIncluded(boolean puhelinnumeroIncluded) {
        this.puhelinnumeroIncluded  =  puhelinnumeroIncluded;
    }

    @Override
    public boolean isFaksinumeroIncluded() {
        return faksinumeroIncluded;
    }

    public void setFaksinumeroIncluded(boolean faksinumeroIncluded) {
        this.faksinumeroIncluded  =  faksinumeroIncluded;
    }

    @Override
    public boolean isWwwOsoiteIncluded() {
        return wwwOsoiteIncluded;
    }

    public void setWwwOsoiteIncluded(boolean wwwOsoiteIncluded) {
        this.wwwOsoiteIncluded  =  wwwOsoiteIncluded;
    }

    @Override
    public boolean isViranomaistiedotuksenSahkopostiosoiteIncluded() {
        return viranomaistiedotuksenSahkopostiosoiteIncluded;
    }

    public void setViranomaistiedotuksenSahkopostiosoiteIncluded(boolean viranomaistiedotuksenSahkopostiosoiteIncluded) {
        this.viranomaistiedotuksenSahkopostiosoiteIncluded  =  viranomaistiedotuksenSahkopostiosoiteIncluded;
    }

    @Override
    public boolean isKoulutusneuvonnanSahkopostiosoiteIncluded() {
        return koulutusneuvonnanSahkopostiosoiteIncluded;
    }

    public void setKoulutusneuvonnanSahkopostiosoiteIncluded(boolean koulutusneuvonnanSahkopostiosoiteIncluded) {
        this.koulutusneuvonnanSahkopostiosoiteIncluded  =  koulutusneuvonnanSahkopostiosoiteIncluded;
    }

    @Override
    public boolean isKriisitiedotuksenSahkopostiosoiteIncluded() {
        return kriisitiedotuksenSahkopostiosoiteIncluded;
    }

    public void setKriisitiedotuksenSahkopostiosoiteIncluded(boolean kriisitiedotuksenSahkopostiosoiteIncluded) {
        this.kriisitiedotuksenSahkopostiosoiteIncluded  =  kriisitiedotuksenSahkopostiosoiteIncluded;
    }

    @Override
    public boolean isOrganisaationSijaintikuntaIncluded() {
        return organisaationSijaintikuntaIncluded;
    }

    public void setOrganisaationSijaintikuntaIncluded(boolean organisaationSijaintikuntaIncluded) {
        this.organisaationSijaintikuntaIncluded  =  organisaationSijaintikuntaIncluded;
    }

    @Override
    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public boolean isOrganisaatioEmailIncluded() {
        return organisaatioEmailIncluded;
    }

    public void setOrganisaatioEmailIncluded(boolean organisaatioEmailIncluded) {
        this.organisaatioEmailIncluded = organisaatioEmailIncluded;
    }

    @Override
    public boolean isResultRowIncluded(SearchResultRowDto row) {
        if (this.searchType != null && this.searchType == SearchTermsDto.SearchType.EMAIL) {
            if ((row.getHenkiloEmail() == null || "".equals(row.getHenkiloEmail().trim()))
                    && (row.getEmailOsoite() == null || "".equals(row.getEmailOsoite().trim()))) {
                return false;
            }
        }
        return this.nonIncludedOids == null || !this.nonIncludedOids.contains(row.getOidAndTyyppiPair());
    }

    public void setLocale(Locale locale) {
        this.locale  =  locale;
    }
}