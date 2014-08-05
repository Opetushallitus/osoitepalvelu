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

package fi.vm.sade.osoitepalvelu.kooste.route.dto;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: ratamaa
 * Date: 3/26/14
 * Time: 1:48 PM
 */
public class HenkiloDetailsDto extends HenkiloListResultDto implements Serializable {
    private static final long serialVersionUID = 501895048885418517L;
    
    private Long id;
    private HenkiloKieliDto asiointiKieli;
    private List<HenkiloYhteystietoRyhmaDto> yhteystiedotRyhma = new ArrayList<HenkiloYhteystietoRyhmaDto>();
    private List<OrganisaatioHenkiloDto> organisaatioHenkilos = new ArrayList<OrganisaatioHenkiloDto>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HenkiloKieliDto getAsiointiKieli() {
        return asiointiKieli;
    }

    public void setAsiointiKieli(HenkiloKieliDto asiointiKieli) {
        this.asiointiKieli = asiointiKieli;
    }

    public List<HenkiloYhteystietoRyhmaDto> getYhteystiedotRyhma() {
        return yhteystiedotRyhma;
    }

    public void setYhteystiedotRyhma(List<HenkiloYhteystietoRyhmaDto> yhteystiedotRyhma) {
        this.yhteystiedotRyhma = yhteystiedotRyhma;
    }

    public List<OrganisaatioHenkiloDto> getOrganisaatioHenkilos() {
        return organisaatioHenkilos;
    }

    public void setOrganisaatioHenkilos(List<OrganisaatioHenkiloDto> organisaatioHenkilos) {
        this.organisaatioHenkilos = organisaatioHenkilos;
    }

    public Optional<String> findFirstAktiivinenOrganisaatioOid() {
        for (OrganisaatioHenkiloDto organisaatioHenkiloDto : this.organisaatioHenkilos) {
            if (!organisaatioHenkiloDto.isPassivoitu()) {
                return Optional.fromNullable(organisaatioHenkiloDto.getOrganisaatioOid());
            }
        }
        return Optional.absent();
    }

    public Set<String> getAktiivinenOrganisaatioOids() {
        return new HashSet<String>(Collections2.transform(Collections2.filter(this.organisaatioHenkilos,
                new Predicate<OrganisaatioHenkiloDto>() {
            public boolean apply(OrganisaatioHenkiloDto organisaatiohenkilo) {
                return !organisaatiohenkilo.isPassivoitu();
            }
        }), new Function<OrganisaatioHenkiloDto, String>() {
            public String apply(OrganisaatioHenkiloDto organisatiohenkilo) {
                return organisatiohenkilo.getOrganisaatioOid();
            }
        }));
    }

    public String getNimi() {
        String kutsumanimi = getKutsumanimi();
        if (kutsumanimi == null) {
            return this.getEtunimet() + " " + this.getSukunimi();
        } else {
            return kutsumanimi + " " + this.getSukunimi();
        }
    }

    public List<HenkiloYhteystietoRyhmaDto> getTyoOsoitees() {
        List<HenkiloYhteystietoRyhmaDto> tyoOsoittees = new ArrayList<HenkiloYhteystietoRyhmaDto>();
        for (HenkiloYhteystietoRyhmaDto ryhma: yhteystiedotRyhma) {
            if (HenkiloYhteystietoRyhmaDto.TYOOSOITE_KUVAUS.equals(ryhma.getRyhmaKuvaus())) {
                tyoOsoittees.add(ryhma);
            }
        }
        return tyoOsoittees;
    }
}
