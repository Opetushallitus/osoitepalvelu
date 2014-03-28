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

/**
 * Created by ratamaa on 12/4/13.
 */
OsoiteKoostepalvelu.service('EmailService', ["$log", "$http", "EmailConverter", "commonErrorHandler", "MyRolesModel",
 function($log, $http, EmailConverter, commonErrorHandler, MyRolesModel) {
    var _el = function(tagName, inElement) {
        var el = document.createElement(tagName);
        if( inElement ) {
            inElement.appendChild(el);
        }
        return el;
    };

    this.sendEmail = function(searchResults) {
        $http.post("api/email/send.settings.json", MyRolesModel.userinfo).success(function(sendSettings) {
            var form = _el("form", $("body").get(0)),
                $form = $(form),
                dataInput = _el("input", form),
                $dataInput = $(dataInput),
                submit = _el("input", form),
                $submit = $(submit);
            $form
                .attr("method", "post")
                .attr("action", sendSettings.endpointUrl)
                .css("display", "none");
            $dataInput
                .attr("name", "emailData")
                .attr("value", angular.toJson(
                    EmailConverter.searchResultToViestipalveluEmail(
                        searchResults,
                        sendSettings.email
                    )));
            $form.get(0).submit();
        }).error(commonErrorHandler);
    }
}]);

OsoiteKoostepalvelu.service('EmailConverter', ["$log", "$http", function($log, $http) {
    this.searchResultToViestipalveluEmail = function(searchResults, emailData) {
        var recipients = [];
        angular.forEach(searchResults, function(result) {
            if( result.henkiloOid ) {
                recipients.push({
                    oid: result.henkiloOid,
                    oidType: 'henkilo',
                    email: result.henkiloEmail,
                    languageCode: "FI" /* <- TODO */
                });
            } else {
                recipients.push({
                    oid: result.organisaatioOid,
                    oidType: 'organisaatio',
                    email: result.emailOsoite,
                    languageCode: (result.osoiteKieli || "FI").toUpperCase()
                });
            }
        });
        return {
            email: emailData,
            recipient: recipients
        };
    }
}]);
