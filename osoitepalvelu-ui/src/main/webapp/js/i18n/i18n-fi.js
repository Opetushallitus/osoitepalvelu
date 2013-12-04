/**
 * Created by ratamaa on 12/3/13.
 */
angular.module("I18n", [], ["$provide", function($provider) {
    $provider.value("i18n", {
        search_title : 'Osoitehaku',
        results_title : 'Tulokset',
        saved_searches : 'Tallennetut haut',

        // Saves popup:
        saves_popup_title: 'Tallennetut haut',
        saves_popup_delete: 'poista',
        saves_popup_close: 'Sulje',

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
        target_group_jarjestajat_yllapitajat: 'Koulutuksen järjestäjät ja oppilaitosten ylläpitäjät',
        target_group_oppilaitos: 'Oppilaitos',
        target_group_opetuspisteet: 'Opetuspisteet',
        target_group_oppisopimustoimipisteet: 'Oppisopimustoimipisteet',
        target_group_muut_organisaatiot: 'Muut organisaatiot',
        target_group_tutkintotoimikunnat: 'Tutkintotoimikunnat',
        target_group_kouluta_kayttajat: 'KOULUTA-käyttäjät',
        target_group_aipal_kayttajat: 'AIPAL-käyttäjät',

        target_group_option_organisaatio: 'organisaatio',
        target_group_option_yhteyshenkilo: 'yhteyshenkilö',
        target_group_option_kriisitiedotus: 'Kriisitiedotus (vain valmiusryhmän käyttöön)',
        target_group_option_koulutusneuvonta: 'koulutusneuvonta',
        target_group_option_rehtori: 'rehtori',
        target_group_option_puheenjohtaja: 'puheenjohtaja',
        target_group_option_sihteeri: 'sihteeri',
        target_gorup_option_jasenet: 'jäsenet',
        target_group_option_tunnuksenhaltijat: 'tunnuksenhaltijat',


        first_name : 'Etunimi',
        last_name : 'Sukunimi'
    });
}]);
