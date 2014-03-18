/**
 * Created by ratamaa on 12/3/13.
 */
angular.module("I18n", [], ["$provide", function($provider) {
    var defaultValues = {};
    defaultValues['fi'] = {
        loading: 'Ladataan...',
        search_title : 'Osoitehaku',
        results_title : 'Osoitteet',
        saved_searches : 'Tallennetut haut',

        confirm_yes: 'Kyllä',
        confirm_no: 'Ei',

        // Saves popup:
        saves_popup_title: 'Tallennetut haut',
        saves_popup_no_saves: 'Ei tallennettuja hakuja.',
        saves_popup_delete: 'poista',
        saves_popup_delete_confirm_title: 'Haluatko varmasti poistaa tallenteen {0}?',
        saves_popup_delete_confirm_yes: 'Poista',
        saves_popup_delete_confirm_no: 'Peruuta',
        saves_popup_close: 'Sulje',

        select_all: 'Kaikki',
        select_none: 'Tyhjennä',

        search_type: 'Haku',
        search_type_placeholder: 'Valitse mihin tarkoitukseen tarvitset yhteystietoja',
        address_fields: 'Esitettävät tiedot',
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
        receiver_fields: 'Vastaanottaja',
        receiver_field_organisaatio: 'Organisaatio',
        receiver_field_yhteyshenkilo: 'Yhteyshenkilö',

        email_search_type: 'Lähetä sähköpostia',
        send_letter_search_type: 'Kirjepostin lähetys',
        letter_search_type: 'Lähetä kirjepostia',
        contact_search_type: 'Hae lista yhteystiedoista',
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
        target_group_kouluta_kayttajat: 'Opintopolku-käyttäjät',
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
        search_term_organisaation_kieli: 'organisaation kieli',
        search_term_tutkintotoimikunta: 'tutkintotoimikunta',
        search_term_tutkintotoimikunta_rooli: 'rooli tutkintotoimikunnassa',
        search_term_kouluta_rooli: 'Opintopolun käyttöoikeus',
        search_term_aipal_rooli: 'AIPAL-rooli',
        search_term_avi: 'AVI',
        search_term_maakunta: 'maakunta',
        search_term_kunta: 'kunta',
        search_term_oppilaitostyyppi: 'oppilaitostyyppi',
        search_term_omistajatyyppi: 'omistajatyyppi',
        search_term_vuosiluokka: 'vuosiluokka',
        search_term_koultuksenjarjestaja: 'koulutuksen järjestäjä',

        show_more_terms: 'enemmän rajausehtoja',
        hide_extra_terms: 'vähemmän rajausehtoja',

        back_to_search_terms: 'hakuehdot',

        total_results: 'osoitetta',
        column_nimi: 'Organisaatio',
        column_organisaatioTunniste: 'Oppilaitoskoodi',
        column_yhteyshenkilonNimi: 'Yhteyshenkilö',
        column_email: 'Sähköpostiosoite',
        column_henkiloEmail: 'Sähköpostiosoite',
        column_postiosoite: 'Postiosoite',
        column_katuPostinumero: 'Katu ja postinumero',
        column_plPostinumero: 'PL ja postinumero',
        column_puhelinnumero: 'Puhelinnumero',
        column_faksinumero: 'Faksinumero',
        column_wwwOsoite: 'Internetosoite',
        column_viranomaistiedotuksenEmail: 'Viranomaistiedotuksen sähköpostiosoite',
        column_koulutusneuvonnanEmail: 'Koulutusneuvonnan sähköpostiosoite',
        column_kriisitiedotuksenEmail: 'Kriisitiedotuksen sähköpostiosoite',
        column_kotikunta: 'Organisaation sijaintikunta',

        no_results: 'Ei lötynyt osoitteita.',
        remove_selected: 'Poista valitut',
        save_excel: 'Tallenna excel',
        send_message: 'Lähetä viesti',

        new_save_popup_title: 'Tallenna haku',
        new_save_popup_save_as: 'Tallenteen nimi',
        new_save_popup_save: 'Tallenna',
        new_save_popup_cancel: 'Peruuta',
        save_name_copy_ending: ' - Kopio',

        overwrite_save_popup_title: 'Ylikirjoita haku {0}',
        overwrite_save_popup_save: 'Ylikirjoita',
        overwrite_save_popup_save_as: 'Tallenna nimellä'
    };
    $provider.value("i18nDefaults", defaultValues);
}]);
