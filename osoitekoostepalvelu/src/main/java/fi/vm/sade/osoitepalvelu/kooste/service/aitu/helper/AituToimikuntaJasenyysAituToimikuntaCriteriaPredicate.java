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

package fi.vm.sade.osoitepalvelu.kooste.service.aitu.helper;

import com.google.common.base.Predicate;
import fi.vm.sade.osoitepalvelu.kooste.dao.aitu.criteria.AituToimikuntaCriteria;
import fi.vm.sade.osoitepalvelu.kooste.service.route.dto.AituJasenyysDto;

/**
 * User: ratamaa
 * Date: 4/16/14
 * Time: 5:31 PM
 */
public class AituToimikuntaJasenyysAituToimikuntaCriteriaPredicate implements Predicate<AituJasenyysDto> {
    private AituToimikuntaCriteria criteria;

    public AituToimikuntaJasenyysAituToimikuntaCriteriaPredicate(AituToimikuntaCriteria criteria) {
        this.criteria = criteria;
    }

    public boolean isUsed() {
        return criteria.isJasenRoolisUsed();
    }

    @Override
    public boolean apply(AituJasenyysDto jasenyys) {
        if (criteria.isJasenRoolisUsed()) {
            if (!criteria.getJasensInRoolis().contains(jasenyys.getRooli())) {
                return false;
            }
        }
        if (criteria.isJasenKielisyysUsed()) {
            if (!criteria.getJasenKielisyysIn().contains(jasenyys.getAidinkieli())) {
                return false;
            }
        }
        return true;
    }
}
