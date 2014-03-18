/**
 * Created by ratamaa on 12/4/13.
 */
OsoiteKoostepalvelu.service('EmailService', function($log, $http, EmailConverter, commonErrorHandler) {
    var _el = function(tagName, inElement) {
        var el = document.createElement(tagName);
        if( inElement ) {
            inElement.appendChild(el);
        }
        return el;
    };

    this.sendEmail = function(searchResults) {
        $http.get("api/email/send.settings.json").success(function(sendSettings) {
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
});

OsoiteKoostepalvelu.service('EmailConverter', function($log, $http) {
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
});
