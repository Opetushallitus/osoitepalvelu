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


angular.module("I18n", [], ["$provide", function($provider) {
    var defaultValues = {};
    defaultValues['fi'] = {
        loading: 'Ladataan...',
        loading_results: 'Ladataan tuloksia...',
        search_title : 'Osoitehaku',
        results_title : 'Osoitteet',
        saved_searches : 'Tallennetut haut',
        saved_searches_placeholder: 'Valitse tallennettu haku',

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
        address_field_ytunnus: 'Y-tunnus',
        address_field_yritysmuoto: 'Yritysmuoto',
        address_field_yhteyshenkilo: 'Yhteyshenkilö',
        address_field_postiosoite: 'Postiosoite',
        address_field_kayntiosoite: 'Käyntiosoite',
        address_field_puhelinnumero: 'Puhelinnumero',
        address_field_internet_osoite: 'Internetosoite',
        address_field_email_osoite: 'Sähköpostiosoite',
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
        target_group_placeholder: 'Valitse lisättävä kohderyhmä',
        target_group_jarjestajat_yllapitajat: 'Koulutuksen järjestäjät ja oppilaitosten ylläpitäjät',
        target_group_oppilaitos: 'Oppilaitos',
        target_group_opetuspisteet: 'Opetuspisteet',
        target_group_oppisopimustoimipisteet: 'Oppisopimustoimipisteet',
        target_group_oppisopimustoimistot: 'Oppisopimustoimistot',
        target_group_muut_organisaatiot: 'Muut organisaatiot',
        target_group_tutkintotoimikunnat: 'Tutkintotoimikunnat',
        target_group_nayttotutkinnon_jarjestajat: 'Näyttötutkinnon järjestäjät',
        target_group_kayttajat: 'Palveluiden käyttäjät',
        target_group_tyoelamapalvelut: 'Työelämäpalvelut',

        target_gorup_option_tutkintotoimikunta: 'tutkintotoimikunta',
        target_group_option_organisaatio: 'organisaatio',
        target_group_option_yhteyshenkilo: 'yhteyshenkilö',
        target_group_option_kriisitiedotus: 'Kriisitiedotus (vain valmiusryhmän käyttöön)',
        target_group_option_koulutusneuvonta: 'koulutusneuvonta',
        target_group_option_rehtori: 'rehtori',
        target_group_option_puheenjohtaja: 'puheenjohtaja',
        target_group_option_sihteeri: 'sihteeri',
        target_gorup_option_jasenet: 'jäsenet',
        target_group_option_viranomaissahkoposti: 'viranomaissähköposti',
        target_group_option_tunnuksenhaltijat: 'tunnuksenhaltijat',
        target_group_option_jarjestajaorganisaatio: 'koulutuksen järjestäjä',
        target_group_option_tutkintovastaava: 'tutkintovastaava',

        search_terms: 'Rajaus',
        search_term_organisaation_kieli: 'organisaation kieli',
        search_term_tutkintotoimikunta: 'tutkintotoimikunta',
        search_term_tutkintotoimikunta_toimikausi: 'tutkintotoimikunnan toimikausi',
        search_term_tutkintotoimikunta_kielis: 'tutkintotoimikunnan kielisyys',
        search_term_tutkintotoimikunta_jasen_kielis: 'tutkintotoimikunnan jäsenen kielisyys',
        search_term_tutkintotoimikunta_rooli: 'rooli tutkintotoimikunnassa',
        search_term_kayttooikeusryhma: 'käyttöoikeusryhmä',
        search_term_aipal_rooli: 'AIPAL-rooli',
        search_term_avi: 'AVI',
        search_term_maakunta: 'maakunta',
        search_term_kunta: 'kunta',
        search_term_oppilaitostyyppi: 'oppilaitostyyppi',
        search_term_omistajatyyppi: 'omistajatyyppi',
        search_term_vuosiluokka: 'vuosiluokka',
        search_term_koultuksenjarjestaja: 'koulutuksen järjestäjä',
        search_term_koulutusala: 'koulutusala',
        search_term_opintoala: 'opintoala',
        search_term_tutkinto: 'tutkinto',
        search_term_koulutustyyppi: 'tutkinnon tyyppi',

        show_more_terms: 'enemmän rajausehtoja',
        hide_extra_terms: 'vähemmän rajausehtoja',

        back_to_search_terms: 'Hakuehdot',

        total_results: 'osoitetta',
        column_nimi: 'Organisaatio',
        column_organisaatioTunniste: 'Oppilaitoskoodi',
        column_ytunnus: 'Y-tunnus',
        column_yritysmuoto: 'Yritysmuoto',
        column_yhteyshenkilonNimi: 'Yhteyshenkilö',
        column_henkiloEmail: 'Sähköpostiosoite (yhteyshenkilön)',
        column_emailOsoite: 'Sähköpostiosoite (organisaation)',
        column_postiosoite: 'Postiosoite',
        column_postiosoitePostinumero: 'Postinumero',
        column_postiosoitePostitoimipaikka: 'Postitoimipaikka',
        column_kayntiosoite: 'Käyntiosoite',
        column_kayntiosoitePostinumero: 'Postinumero',
        column_kayntiosoitePostitoimipaikka: 'Postitoimipaikka',
        column_puhelinnumero: 'Puhelinnumero',
        column_wwwOsoite: 'Internetosoite',
        column_viranomaistiedotuksenEmail: 'Viranomaistiedotuksen sähköpostiosoite',
        column_koulutusneuvonnanEmail: 'Koulutusneuvonnan sähköpostiosoite',
        column_kriisitiedotuksenEmail: 'Kriisitiedotuksen sähköpostiosoite',
        column_kotikunta: 'Organisaation sijaintikunta',

        no_results: 'Osoitteita ei löytynyt. Tarkista hakuehdot.',
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
        overwrite_save_popup_save_as: 'Tallenna nimellä',

        error_title: 'Virhetilanne',
        internal_error: 'Odottamaton järjestelmän sisäinen virhe tapahtui.',
        camel_http_error: 'Virhe kutsuttaessa ulkoista palvelua reitillä {0}: HTTP virhe {1} osoitteesta: {2}',
        camel_http_call_timeout_error: 'Virhe kutsuttaessa ulkoista palvelua reitillä {0}: kutsu kesti liian kauan.',
        camel_error: 'Odottamaton virhe kutsuttaessa ulkoista palvelua reitillä {0}.',
        not_found_error: 'Virhe: Etsittyä tietoa ei löytynyt.',
        not_authorized_error: 'Virhe: Ei käyttöoikeutta pyydettyyn resurssiin.',
        bad_request_error: 'Vihre: Käyttöliittymä suoritti virheellisen pyynnön: {1}',
        too_few_search_conditions_for_organisaatios: 'Hakua ei voida suorittaa, koska se johtaisi kaikkien organisaatioiden hakemiseen. Valitse vähintään yksi organisaatioita rajaava ehto.',
        too_few_search_conditions_for_henkilos: 'Hakua ei voida suorittaa, koska se johtaisi kaikkien Opintopolku-käyttäjien hakemiseen. Valitse vähintään yksi käyttäjiä tai organisaatioita koskeva rajausehto.',

        tutkintotoimikunta_rooli_jasen: 'Jäsen',
        tutkintotoimikunta_rooli_puheenjohtaja: 'Puheenjohtaja',
        tutkintotoimikunta_rooli_varapuheenjohtaja: 'Varapuheenjohtaja',
        tutkintotoimikunta_rooli_sihteeri: 'Sihteeri',
        tutkintotoimikunta_rooli_ulkopuolinensihteeri: 'Ulkopuolinen sihteeri',
        tutkintotoimikunta_rooli_asiantuntija: 'Asiantuntija',
        tutkintotoimikunta_rooli_null: 'Ei tiedossa',

        tutkintoimikunta_toimikausi_voimassa: 'voimassa',
        tutkintoimikunta_toimikausi_tuleva: 'tuleva',
        tutkintoimikunta_toimikausi_mennyt: 'mennyt',

        osoitteen_esityskieli: 'Osoitteen esityskieli',
        osoitteen_esityskieli_placeholder: 'osoitteen esityskieli',
        kieli_fi: 'suomi',
        kieli_sv: 'ruotsi',
        kieli_2k: 'suomi/ruotsi',
        kieli_en: 'englanti'
    };
    defaultValues['en'] = defaultValues['fi'];
    defaultValues['sv'] = defaultValues['fi'];
    $provider.value("i18nDefaults", defaultValues);
}]);
