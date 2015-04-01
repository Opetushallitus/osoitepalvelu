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
 * User: simok
 * Date: 3/23/15
 * Time: 3:29 PM
 */
public class TarjontaKoulutusHakuResultDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Generic error codes.
     */
    public enum ResultStatus {

        /**
         * Indicates NO error.
         */
        OK,
        /**
         * Information for the user.
         */
        INFO,
        /**
         * Warning for the user.
         */
        WARNING,
        /**
         * Validation errors.
         */
        VALIDATION,
        /**
         * A real "error".
         */
        ERROR,

        /**
         * For those occasions requested resource is not found.
         */
        NOT_FOUND
    };

    /**
     * Default status for the result is OK.
     */
    private ResultStatus _status = ResultStatus.OK;

    /**
     * Actual result contained here.
     */
    private TarjontaKoulutusHakutuloksetDto _result;


    /**
     * @return the _status
     */
    public ResultStatus getStatus() {
        return _status;
    }

    /**
     * @param _status the _status to set
     */
    public void setStatus(ResultStatus _status) {
        this._status = _status;
    }

    /**
     * @return the _result
     */
    public TarjontaKoulutusHakutuloksetDto getResult() {
        return _result;
    }

    /**
     * @param _result the _result to set
     */
    public void setResult(TarjontaKoulutusHakutuloksetDto _result) {
        this._result = _result;
    }
}
