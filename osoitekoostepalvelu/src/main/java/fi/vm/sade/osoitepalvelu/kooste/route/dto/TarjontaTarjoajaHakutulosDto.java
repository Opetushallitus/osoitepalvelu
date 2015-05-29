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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * User: simok
 * Date: 3/23/15
 * Time: 3:29 PM
 */
public class TarjontaTarjoajaHakutulosDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String oid;
    private Map<String, String> nimi;
    private List<TarjontaKoulutusHakutulosDto> tulokset;

    /**
     * @return the tulokset
     */
    public List<TarjontaKoulutusHakutulosDto> getTulokset() {
        return tulokset;
    }

    /**
     * @param tulokset the tulokset to set
     */
    public void setTulokset(List<TarjontaKoulutusHakutulosDto> tulokset) {
        this.tulokset = tulokset;
    }

    /**
     * @return the oid
     */
    public String getOid() {
        return oid;
    }

    /**
     * @param oid the oid to set
     */
    public void setOid(String oid) {
        this.oid = oid;
    }

    /**
     * @return the nimi
     */
    public Map<String, String> getNimi() {
        return nimi;
    }

    /**
     * @param nimi the nimi to set
     */
    public void setNimi(Map<String, String> nimi) {
        this.nimi = nimi;
    }

}
