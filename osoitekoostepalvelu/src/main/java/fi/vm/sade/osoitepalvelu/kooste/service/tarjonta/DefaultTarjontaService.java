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

package fi.vm.sade.osoitepalvelu.kooste.service.tarjonta;

import com.googlecode.ehcache.annotations.PartialCacheKey;
import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.route.TarjontaServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KoulutusCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.TarjontaKoulutusHakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.TarjontaKoulutusHakutulosDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.TarjontaTarjoajaHakutulosDto;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * User: simok
 * Date: 3/23/15
 * Time: 3:29 PM
 */
@Service
public class DefaultTarjontaService extends AbstractService implements TarjontaService {
    private static final long serialVersionUID = 1L;

    @Autowired(required = false)
    private TarjontaServiceRoute tarjontaServiceRoute;

    @Override
    public List<String> findOrganisaatios(
            @PartialCacheKey KoulutusCriteriaDto criteria, CamelRequestContext requestContext) {

        logger.info("Criteria for tarjonta koulutus search: " + ToStringBuilder.reflectionToString(criteria));

        TarjontaKoulutusHakuResultDto tarjontaKoulutusHakuResult =
                tarjontaServiceRoute.findKoulutukset(criteria, requestContext);

        List<String> oids = new ArrayList<String>();

        // Tarjonta palautti virheen!!!
        if (tarjontaKoulutusHakuResult.getStatus() != TarjontaKoulutusHakuResultDto.ResultStatus.OK) {
            throw new IllegalStateException("Result not OK from tarjonta koulutus search!");
        }

        // Otetaan kaikkien organisaatioiden oidit
        // - jotka tarjoavat kriteerien mukaista koulutusta
        // - joiden koulutus on tilassa JULKAISTU tai VALMIS
        for (TarjontaTarjoajaHakutulosDto tarjoajaHakutulos :
                tarjontaKoulutusHakuResult.getResult().getTulokset()) {
            for (TarjontaKoulutusHakutulosDto koulutusHakutulos : tarjoajaHakutulos.getTulokset()) {
                if (koulutusHakutulos.getTila() == TarjontaKoulutusHakutulosDto.TarjontaTila.JULKAISTU ||
                        koulutusHakutulos.getTila() == TarjontaKoulutusHakutulosDto.TarjontaTila.VALMIS) {
                    oids.add(tarjoajaHakutulos.getOid());
                    break;
                }
            }
        }

        logger.info("Number of organisaatio oids matching tarjonta koulutus search: " + oids.size());

        return oids;
    }

    public void setTarjontaServiceRoute(TarjontaServiceRoute tarjontaServiceRoute) {
        this.tarjontaServiceRoute = tarjontaServiceRoute;
    }
}
