describe("Application Components Test", function() {

    var EqualsHelper, ExtractHelper,
        ArrayHelper, FilterHelper, KoodiHelper;

    var SearchService, OptionsService,
        SavesService, EmailService,
        commonErrorHandler;

    var MyRolesModel, AuthService, Config,
        loadingService,
        LocalisationService, Localisations;

    var SearchTypes, EmptyTerms, Osoitekielis,
        Aitukielis, TutkintotoimikuntaToimikausis,
        AddressFields, TutkintotoimikuntaRoolis,
        TargetGroups;

    beforeEach(module("OsoiteKoostepalvelu"));
    beforeEach(function() {
        inject(function($injector, $rootScope) {
            scope = $rootScope;
            EqualsHelper = $injector.get("EqualsHelper");
            ExtractHelper = $injector.get("ExtractHelper");
            ArrayHelper = $injector.get("ArrayHelper");
            FilterHelper = $injector.get("FilterHelper");
            KoodiHelper = $injector.get("KoodiHelper");
        });

        inject(function($injector, $rootScope) {
            scope = $rootScope;
            SearchService = $injector.get("SearchService");
            OptionsService = $injector.get("OptionsService");
            SavesService = $injector.get("SavesService");
            EmailService = $injector.get("EmailService");
            commonErrorHandler = $injector.get("commonErrorHandler");
        });

        inject(function($injector, $rootScope) {
            scope = $rootScope;
            MyRolesModel = $injector.get("MyRolesModel");
            AuthService = $injector.get("AuthService");
            Config = $injector.get("Config");
            loadingService = $injector.get("loadingService");
            LocalisationService = $injector.get("LocalisationService");
            Localisations = $injector.get("Localisations");
        });

        inject(function($injector, $rootScope) {
            scope = $rootScope;
            SearchTypes = $injector.get("SearchTypes");
            EmptyTerms = $injector.get("EmptyTerms");
            Osoitekielis = $injector.get("Osoitekielis");
            Aitukielis = $injector.get("Aitukielis");
            TutkintotoimikuntaToimikausis = $injector.get("TutkintotoimikuntaToimikausis");
            AddressFields = $injector.get("AddressFields");
            TutkintotoimikuntaRoolis = $injector.get("TutkintotoimikuntaRoolis");
            TargetGroups = $injector.get("TargetGroups");
        });

    });

    it("controllers are defined", function() {
       expect(SearchController).toBeDefined();
       expect(SavesPopupController).toBeDefined();
       expect(ConfirmPopupController).toBeDefined();
       expect(NewSavePopupController).toBeDefined();
       expect(ResultsController).toBeDefined();
    });

    it("util services are defined", function() {
        expect(EqualsHelper).toBeDefined();
        expect(ExtractHelper).toBeDefined();
        expect(ArrayHelper).toBeDefined();
        expect(FilterHelper).toBeDefined();
        expect(KoodiHelper).toBeDefined();
    });

    it("internal services are defined", function() {
        expect(SearchService).toBeDefined();
        expect(OptionsService).toBeDefined();
        expect(SavesService).toBeDefined();
        expect(EmailService).toBeDefined();
        expect(commonErrorHandler).toBeDefined();
    });

    it("shared services are defined", function($injector) {
        expect(MyRolesModel).toBeDefined();
        expect(AuthService).toBeDefined();
        expect(Config).toBeDefined();
        expect(loadingService).toBeDefined();
        expect(LocalisationService).toBeDefined();
        expect(Localisations).toBeDefined();
    });

    it("static options are defined", function($injector) {
        expect(SearchTypes).toBeDefined();
        expect(EmptyTerms).toBeDefined();
        expect(Osoitekielis).toBeDefined();
        expect(Aitukielis).toBeDefined();
        expect(TutkintotoimikuntaToimikausis).toBeDefined();
        expect(AddressFields).toBeDefined();
        expect(TutkintotoimikuntaRoolis).toBeDefined();
        expect(TargetGroups).toBeDefined();
    });
});