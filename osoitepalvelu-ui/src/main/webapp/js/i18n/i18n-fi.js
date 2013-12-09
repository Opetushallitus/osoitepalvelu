/**
 * Created by ratamaa on 12/3/13.
 */
angular.module("I18n", [], ["$provide", function($provider) {
    $provider.value("i18n", {
        search_title : 'Osoitehaku',
        results_title : 'Osoitteet',
        saved_searches : 'Tallennetut haut',

        // Saves popup:
        saves_popup_title: 'Tallennetut haut',
        saves_popup_delete: 'poista',
        saves_popup_close: 'Sulje',

        select_all: 'Kaikki',
        select_none: 'Tyhjennä',

        search_type: 'Haku',
        search_type_placeholder: 'Valitse mihin tarkoitukseen tarvitset yhteystietoja',
        address_fields: 'Osoitteet',
        address_field_organisaatio_nimi: 'Organisaation nimi',
        address_field_organisaatio_tunniste: 'Organisaatiotunniste',
        address_field_yhteyshenkilo: 'Yhteyshenkilö',
        address_field_postiosoite: 'Postiosoite',
        address_field_katu_postinumero: 'Katuosoite ja postinumero',
        address_field_pl_postinumero: 'PL ja postinumero',
        address_field_puhelinnumero: 'Puhelinnumero',
        address_field_faxinumero: 'Faksinumero',
        address_field_internet_osoite: 'Internetosoite',
        address_field_viranomaistiedotus_email: 'Viranomaistiedotuksen sähköpostiosoite',
        address_field_koulutusneuvonnan_email: 'Koulutusneuvonnan sähköpostiosoite',
        address_field_kriisitiedotuksen_email: 'Kriisitiedotuksen sähköpostiosoite',
        address_field_organisaatio_sijaintikunta: 'Organisaation sijaintikunta',
        address_field_opasjakelumaarat: 'Opasjakelumäärät',

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

        search_terms: 'Rajaus',
        search_term_avi: 'AVI',
        search_term_maakunta: 'maakunta',
        search_term_kunta: 'kunta',
        search_term_oppilaitostyyppi: 'oppilaitostyyppi',
        search_term_omistajatyyppi: 'omistajatyyppi',
        search_term_vuosiluokka: 'vuosiluokka',
        search_term_koultuksenjarjestaja: 'koulutuksen järjestäjä',

        show_more_terms: 'enemmän rajaushetoja',
        hide_extra_terms: 'vähemmän rajaushetoja',

        back_to_search_terms: 'hakuehdot',

        total_results: 'osoitetta',
        column_identifier: 'Tunniste',
        column_targetGroup: 'Kohderyhmä',

        no_results: 'Ei lötynyt osoitteita.',
        remove_selected: 'Poista valitut',
        save_excel: 'Tallenna excel',
        send_message: 'Lähetä viesti'
    });
}]);
