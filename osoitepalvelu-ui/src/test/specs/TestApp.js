describe("Application Test", function() {

    beforeEach(module("OsoiteKoostepalvelu"));

    it("controllers are defined", function() {
       expect(SearchController).toBeDefined();
       expect(SavesPopupController).toBeDefined();
       expect(ConfirmPopupController).toBeDefined();
       expect(NewSavePopupController).toBeDefined();
       expect(ResultsController).toBeDefined();
    });


});