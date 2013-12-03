/**
 * Created by ratamaa on 12/3/13.
 */
angular.module("I18n", [], ["$provide", function($provider) {
    $provider.value("i18n", {
        search_title : 'Osoitehaku',
        results_title : 'Tulokset',
        saved_searches : 'Tallennetut haut',
        search_type: 'Haku',
        search_type_placeholder: 'Valitse mihin tarkoitukseen tarvitset yhteystietoja',
        email_search_type: 'Sähköpostin lähetys',
        letter_search_type: 'Etsi kirjeosoitteet',
        contact_search_type: 'Hae yhteystietoja',
        search_action: 'Hae',
        save_search_action: 'Tallenna haku',
        clear_action: 'Tyhjennä',

        // TargetGroups:
        target_group: 'Kohderyhmä',
        target_group_oppilaitos: 'Oppilaitos',
        target_group_oppilaitos_organisaatio: 'Organisaatio',
        target_group_oppilaitos_rehtori: 'Rehtori',
        target_group_oppilaitos_koulutusneuvonta: 'Koulutusneuvonta',


        first_name : 'Etunimi',
        last_name : 'Sukunimi'
    });
}]);
