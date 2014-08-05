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

/**
 * User: ratamaa
 * Date: 3/26/14
 * Time: 2:13 PM
 */
public class HenkiloKieliDto implements Serializable {
    private static final long serialVersionUID = 2851690593549118608L;
    
    private String kieliKoodi; // esim. fi
    private String kieliTyyppi; // esim. suomi

    public String getKieliKoodi() {
        return kieliKoodi;
    }

    public void setKieliKoodi(String kieliKoodi) {
        this.kieliKoodi = kieliKoodi;
    }

    public String getKieliTyyppi() {
        return kieliTyyppi;
    }

    public void setKieliTyyppi(String kieliTyyppi) {
        this.kieliTyyppi = kieliTyyppi;
    }
}
