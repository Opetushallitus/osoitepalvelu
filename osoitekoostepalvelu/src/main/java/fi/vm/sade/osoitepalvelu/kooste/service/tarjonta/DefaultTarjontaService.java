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

//import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.PartialCacheKey;
import fi.vm.sade.osoitepalvelu.kooste.common.route.CamelRequestContext;
import fi.vm.sade.osoitepalvelu.kooste.route.TarjontaServiceRoute;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KoulutusCriteriaDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.TarjontaKoulutusHakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.TarjontaKoulutusHakutulosDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.TarjontaTarjoajaHakutulosDto;

import java.util.ArrayList;
//import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
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

//    @Autowired(required = false)
//    private TarjontaCacheRepository tarjontaCacheRepository;

    @Autowired(required = false)
    private TarjontaServiceRoute tarjontaServiceRoute;

//    @Value("${tarjonta.cache.livetime.seconds}")
//    private long cacheTimeoutSeconds;

    @Override
//    @Cacheable(cacheName = "koulutusHakuCache")
    public List<String> findOrganisaatios(
            @PartialCacheKey KoulutusCriteriaDto criteria, CamelRequestContext requestContext) {

        logger.info("***************** findOrganisaatios criteria: " + ToStringBuilder.reflectionToString(criteria));

        TarjontaKoulutusHakuResultDto tarjontaKoulutusHakuResult =
                tarjontaServiceRoute.findKoulutukset(criteria, requestContext);

        List<String> oids = new ArrayList<String>();

        // Tarjonta palautti virheen!!!
        if (tarjontaKoulutusHakuResult.getStatus() != TarjontaKoulutusHakuResultDto.ResultStatus.OK) {
            return oids;
        }

        // Otetaan kaikkien organisaatioiden oidit
        // - jotka tarjoavat kriteerien mukaista koulutusta
        // - joiden koulutus on tilassa JULKAISTU tai VALMIS
        for (TarjontaTarjoajaHakutulosDto tarjoajaHakutulos :
                tarjontaKoulutusHakuResult.getResult().getTulokset()) {

            logger.info("***************** findOrganisaatios tarjoaja: " + tarjoajaHakutulos.getOid());

            for (TarjontaKoulutusHakutulosDto koulutusHakutulos : tarjoajaHakutulos.getTulokset()) {

                logger.info("***************** findOrganisaatios koulutus: " + koulutusHakutulos.getOid());

                if (koulutusHakutulos.getTila() == TarjontaKoulutusHakutulosDto.TarjontaTila.JULKAISTU ||
                        koulutusHakutulos.getTila() == TarjontaKoulutusHakutulosDto.TarjontaTila.VALMIS) {
                    oids.add(tarjoajaHakutulos.getOid());
                    break;
                }
            }
        }

        logger.info("***************** findOrganisaatios:" + oids.toString());

        return oids;
    }

    public void setTarjontaServiceRoute(TarjontaServiceRoute tarjontaServiceRoute) {
        this.tarjontaServiceRoute = tarjontaServiceRoute;
    }

//    private boolean isCacheUsable(DateTime updatedAt) {
//        return updatedAt.plus(cacheTimeoutSeconds * MILLIS_IN_SECOND).compareTo(new DateTime()) > 0;
//    }
//
//    private boolean isCacheUsed() {
//        return tarjontaCacheRepository != null && cacheTimeoutSeconds >= 0;
//    }
//
//    public long getCacheTimeoutSeconds() {
//        return cacheTimeoutSeconds;
//    }
//
//    public void setCacheTimeoutSeconds(long cacheTimeoutSeconds) {
//        this.cacheTimeoutSeconds = cacheTimeoutSeconds;
//    }
}
