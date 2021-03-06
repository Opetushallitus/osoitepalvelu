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

package fi.vm.sade.osoitepalvelu.service.mock;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KoodiDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KoodistoDto;
import fi.vm.sade.osoitepalvelu.kooste.route.dto.KoodistoVersioDto;
import fi.vm.sade.osoitepalvelu.kooste.route.KoodistoRoute;

import java.util.*;

/**
 * User: ratamaa
 * Date: 3/12/14
 * Time: 3:20 PM
 */
public class KoodistoServiceRouteMock implements KoodistoRoute {
    private Map<KoodistoVersioDto, List<KoodiDto>> koodis  =  new HashMap<KoodistoVersioDto, List<KoodiDto>>();
    private Map<KoodistoDto.KoodistoTyyppi, KoodistoDto> koodistosByTyyppis
             =  new HashMap<KoodistoDto.KoodistoTyyppi, KoodistoDto>();
    private Map<String, List<KoodiDto>> kuntasByMaakuntaUri  =  new HashMap<String, List<KoodiDto>>();

    public void add(KoodistoVersioDto versio, KoodiDto ...koodis) {
        add(versio, Arrays.asList(koodis));
    }

    public void add(KoodistoVersioDto versio, List<KoodiDto> koodis) {
        this.koodis.put(versio, koodis);
        if (!koodistosByTyyppis.containsKey(versio.getKoodistoTyyppi())) {
            KoodistoDto koodisto  =  new KoodistoDto();
            koodisto.setKoodistoUri(versio.getKoodistoTyyppi().getUri());
            koodistosByTyyppis.put(versio.getKoodistoTyyppi(), koodisto);
        }
        applyVersiotieto(versio, koodis);
    }

    private void applyVersiotieto(KoodistoVersioDto versio, List<KoodiDto> koodis) {
        KoodistoDto koodisto  =  koodistosByTyyppis.get(versio.getKoodistoTyyppi());
        for (KoodiDto koodi : koodis) {
            koodi.setKoodisto(koodisto);
            koodi.setTila(versio.getTila());
            koodi.setVoimassaAlkuPvm(versio.getVoimassaAlkuPvm());
            koodi.setVoimassaLoppuPvm(versio.getVoimassaLoppuPvm());
            koodi.setVersio(versio.getVersio());
        }
    }

    public void addKuntaByMaakunta(String maakuntaUri, KoodistoVersioDto versio, KoodiDto... koodis) {
        addKuntaByMaakunta(maakuntaUri, versio, Arrays.asList(koodis));
    }

    public void addKuntaByMaakunta(String maakuntaUri, KoodistoVersioDto versio, List<KoodiDto> koodis) {
        this.kuntasByMaakuntaUri.put(maakuntaUri, koodis);
        applyVersiotieto(versio, koodis);
    }

    @Override
    public List<KoodiDto> findKooditKoodistonTyyppilla(KoodistoDto.KoodistoTyyppi koodistoTyyppi) {
        return collect(findKoodistonVersiot(koodistoTyyppi));
    }

    @Override
    public List<KoodistoVersioDto> findKoodistonVersiot(final KoodistoDto.KoodistoTyyppi koodistoTyyppi) {
        return new ArrayList<KoodistoVersioDto>(Collections2.filter(koodis.keySet(), new Predicate<KoodistoVersioDto>() {
            public boolean apply(KoodistoVersioDto input) {
                return input.getKoodistoTyyppi() == koodistoTyyppi;
            }
        }));
    }

    @Override
    public List<KoodiDto> findKooditKoodistonVersiolleTyyppilla(final KoodistoDto.KoodistoTyyppi koodistoTyyppi,
                                                                final long versio) {
        return collect(Collections2.filter(koodis.keySet(), new Predicate<KoodistoVersioDto>() {
            public boolean apply(KoodistoVersioDto input) {
                return input.getVersio() == versio && input.getKoodistoTyyppi() == koodistoTyyppi;
            }
        }));
    }

    @Override
    public List<KoodiDto> findKoodisByParent(String koodiUri) {
        List<KoodiDto> kuntas  =  kuntasByMaakuntaUri.get(koodiUri);
        if (kuntas == null) {
            return new ArrayList<KoodiDto>();
        }
        return kuntas;
    }

    @Override
    public List<KoodiDto> findKoodisByChild(String koodiUri) {
        return new ArrayList<KoodiDto>();
    }

    protected List<KoodiDto> collect(Collection<KoodistoVersioDto> versions) {
        List<KoodiDto> result  =  new ArrayList<KoodiDto>();
        for (KoodistoVersioDto koodistoVersio : versions) {
            result.addAll(koodis.get(koodistoVersio));
        }
        return result;
    }

    @Override
    public boolean isFindCounterUsed() {
        return false;
    }

    @Override
    public void setFindCounterUsed(boolean findCounterUsed) {
    }

    @Override
    public long getFindCounterValue() {
        return 0;
    }
}
