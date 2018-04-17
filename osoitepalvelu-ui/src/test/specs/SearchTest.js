describe("Search Test", function() {
    // mock url props
    window.url = function(url, param) {
        return 'api/search/' + param;
    }

    var dummyResults = {
       "rows":[
          {
             "rivinumero":1,
             "nimi":"Testiorganisaatio 1",
             "organisaatioOid":null,
             "kotikunta":"Tammerfors",
             "toimipistekoodi":null,
             "oppilaitosKoodi":"15184",
             "wwwOsoite":"http://www.oppilaitos.fi/",
             "tyypit":null,
             "puhelinnumero":"03 123 4567",
             "emailOsoite":"yleinen@oppilaitos.fi",
             "viranomaistiedotuksenEmail":null,
             "koulutusneuvonnanEmail":null,
             "kriisitiedotuksenEmail":null,
             "henkiloOid":null,
             "yhteystietoNimi":null,
             "nimike":null,
             "henkiloEmail":null,
             "kayntiosoite":{
                "kieli":null,
                "tyyppi":"kaynti",
                "yhteystietoOid":"1.2.3.4.5.6",
                "osoite":"Tampereen tie 5",
                "postilokero":null,
                "postinumero":"TAMPERE",
                "postitoimipaikka":"TAMPERE",
                "extraRivi":null
             },
             "postiosoite":{
                "kieli":null,
                "tyyppi":null,
                "yhteystietoOid":null,
                "osoite":"PL 15550",
                "postilokero":null,
                "postinumero":"33821",
                "postitoimipaikka":"TAMPERE",
                "extraRivi":null
             },
             "oidAndTyyppiPair":{
                "oidTyyppi":"organisaatio",
                "oid":null,
                "rivinumero":1
             }
          },
          {
             "rivinumero":2,
             "nimi":"Testiorganisaatio 2",
             "organisaatioOid":null,
             "kotikunta":"Tammerfors",
             "toimipistekoodi":null,
             "oppilaitosKoodi":"15118",
             "wwwOsoite":"http://www.oppilaitos2.fi",
             "tyypit":null,
             "puhelinnumero":"03 1215 1564",
             "emailOsoite":"yleinen@oppilaitos2.fi",
             "viranomaistiedotuksenEmail":null,
             "koulutusneuvonnanEmail":null,
             "kriisitiedotuksenEmail":null,
             "henkiloOid":null,
             "yhteystietoNimi":null,
             "nimike":null,
             "henkiloEmail":null,
             "kayntiosoite":{
                "kieli":null,
                "tyyppi":"kaynti",
                "yhteystietoOid":"1.2.3.4.5.7",
                "osoite":"Rantatie 1",
                "postilokero":null,
                "postinumero":"33520",
                "postitoimipaikka":"TAMPERE",
                "extraRivi":null
             },
             "postiosoite":{
                "kieli":null,
                "tyyppi":null,
                "yhteystietoOid":null,
                "osoite":"Rantatie 1",
                "postilokero":null,
                "postinumero":"33520",
                "postitoimipaikka":"TAMPERE",
                "extraRivi":null
             },
             "oidAndTyyppiPair":{
                "oidTyyppi":"organisaatio",
                "oid":null,
                "rivinumero":2
             }
          }
       ],
       "presentation":{
          "organisaationNimiIncluded":true,
          "organisaatiotunnisteIncluded":true,
          "yhteyshenkiloIncluded":false,
          "yhteyshenkiloEmailIncluded":false,
          "positosoiteIncluded":true,
          "kayntiosoiteIncluded":true,
          "puhelinnumeroIncluded":true,
          "wwwOsoiteIncluded":true,
          "viranomaistiedotuksenSahkopostiosoiteIncluded":false,
          "koulutusneuvonnanSahkopostiosoiteIncluded":false,
          "kriisitiedotuksenSahkopostiosoiteIncluded":false,
          "organisaationSijaintikuntaIncluded":true,
          "organisaatioEmailIncluded":true,
          "nayttotutkinnonJarjestajaOrganisaatiosIncluded":true,
          "nayttotutkinnonJarjestajaVastuuhenkilosIncluded":false,
          "locale":"sv"
       },
       "sourceRegisters": ["opintopolku"]
    };

    beforeEach(module("OsoiteKoostepalvelu"));

    describe("Search shows results", function() {
        var $httpBackend, $timeout, $rootScope, SearchService,
            createSearchController, createResultsController;

        beforeEach(inject(function($injector) {
            $httpBackend = $injector.get('$httpBackend');
            $httpBackend.when('GET').respond([]);
            $httpBackend.when('POST','api/search/list.json?lang=fi').respond(dummyResults);

            $timeout = $injector.get("$timeout");
            $rootScope = $injector.get('$rootScope');
            var $controller = $injector.get('$controller');
            SearchService = $injector.get("SearchService");

            createSearchController = function() {
                return $controller('SearchController', {'$scope' : $rootScope });
            };
            createResultsController = function() {
                return $controller('ResultsController', {'$scope' : $rootScope });
            }
        }));

        it("results found", function() {
            var searchController = createSearchController();
            $httpBackend.flush();
            $rootScope.searchType = 'CONTACT';
            $rootScope.search();
            expect(SearchService.isSearchReady()).toEqual(true);

            var resultsController = createResultsController();
            $timeout.flush();
            $httpBackend.flush();
            expect($rootScope.results.length).toEqual(2);
        });
    });
});
