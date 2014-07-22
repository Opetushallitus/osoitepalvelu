describe("Helpers Test", function() {

    var EqualsHelper, ExtractHelper,
        ArrayHelper, FilterHelper, KoodiHelper,
        $filter;

    beforeEach(module("Helpers"));
    beforeEach(function() {
        inject(function($injector, $rootScope) {
            scope = $rootScope;
            EqualsHelper = $injector.get("EqualsHelper");
            ExtractHelper = $injector.get("ExtractHelper");
            ArrayHelper = $injector.get("ArrayHelper");
            FilterHelper = $injector.get("FilterHelper");
            KoodiHelper = $injector.get("KoodiHelper");
            $filter = $injector.get("$filter");
        });
    });

    describe("EqualsHelper", function() {
        it("equal should work with objects", function() {
            expect(EqualsHelper.equal(
                {a: "a", b: {c: "d"}},
                {a: "a", b: {c: "d"}}
            )).toEqual(true);

            expect(EqualsHelper.equal(
                {a: "a", b: {c: ["d"]}},
                {b: {c: ["d"]}, a: "a"}
            )).toEqual(true);

            expect(EqualsHelper.equal(
                {a: "a", b: {c: ["d"]}},
                {a: "a", b: {c: ["e"]}}
            )).toEqual(false);

            expect(EqualsHelper.equal(
                {a: "a"},
                {}
            )).toEqual(false);

            expect(EqualsHelper.equal(
                {},
                {a: "a"}
            )).toEqual(false);
        });

        it("equal should work with arrays", function() {
            expect(EqualsHelper.equal(
                [1,2,3],
                [1,2,3]
            )).toEqual(true);

            expect(EqualsHelper.equal(
                [1,2,3],
                [1,2,4]
            )).toEqual(false);

            expect(EqualsHelper.equal(
                [1,2,3],
                [1,2]
            )).toEqual(false);
        });

        it("equal should work with basic types", function() {
            expect(EqualsHelper.equal("a","a")).toEqual(true);
            expect(EqualsHelper.equal(1,2)).toEqual(false);
            expect(EqualsHelper.equal(0,"")).toEqual(false);
        });

        it("equal should work with nulls", function() {
            expect(EqualsHelper.equal(null,null)).toEqual(true);
            expect(EqualsHelper.equal("a",null)).toEqual(false);
            expect(EqualsHelper.equal(null, "")).toEqual(false);
            expect(EqualsHelper.equal({},null)).toEqual(false);
            expect(EqualsHelper.equal(null,[])).toEqual(false);
        });
    });

    var testArr = [
        {name: 'a', value: 3},
        {name: 'b', value: 5},
        {name: 'c', value: 0},
        {name: 'a', value: 2}
    ];

    describe("ExtractHelper", function() {
        it("extract should work with single field", function() {
            expect(ExtractHelper.extract(testArr, "name")).toEqual(["a","b","c","a"]);
        });

        it("extract should work with multipe fields", function() {
            expect(ExtractHelper.extract(testArr, ["name"])).toEqual([{name:"a"},{name:"b"},{name:"c"},{name:"a"}]);
            expect(ExtractHelper.extract(testArr, ["name","value"])).toEqual(testArr);
            expect(ExtractHelper.extract(testArr, [])).toEqual([{},{},{},{}]);
        });
    });

    describe("ArrayHelper", function() {
        it("ensureArray should work", function() {
            expect(ArrayHelper.ensureArray("a")).toEqual(["a"]);
            expect(ArrayHelper.ensureArray(["b"])).toEqual(["b"]);
            expect(ArrayHelper.ensureArray(null)).toEqual([null]);
        });

        it("forAll should work", function() {
            expect(ArrayHelper.forAll(testArr, function(v) {
                return v.value >= 0;
            })).toEqual(true);
            expect(ArrayHelper.forAll(testArr, function(v) {
                return v.value > 0;
            })).toEqual(false);
            expect(ArrayHelper.forAll([], function(v) { return v == 'foo'; })).toEqual(true);
        });

        it("forAny should work", function() {
            expect(ArrayHelper.forAny(testArr, function(v) {
                return v.value > 0;
            })).toEqual(true);
            expect(ArrayHelper.forAny(testArr, function(v) {
                return v.value < 0;
            })).toEqual(false);
            expect(ArrayHelper.forAny([], function() { return true; })).toEqual(false);
        });

        it("containsAny should work", function() {
            expect(ArrayHelper.containsAny(testArr, [{name: 'a', value: 1}, {name: 'a', value:2}] )).toEqual(true);
            expect(ArrayHelper.containsAny(testArr, [{name: 'a', value: 1}] )).toEqual(false);
            expect(ArrayHelper.containsAny(testArr, testArr)).toEqual(true);
            expect(ArrayHelper.containsAny(testArr, {name: 'a', value:2})).toEqual(true);
            expect(ArrayHelper.containsAny(1,1)).toEqual(true);
            expect(ArrayHelper.containsAny(1,[1])).toEqual(true);
            expect(ArrayHelper.containsAny(1,[2,3])).toEqual(false);
        });
    });

    describe("FilterHelper", function() {
        it("notInArray should work", function() {
            expect(
                $filter('filter')([1,2,3,4,5], FilterHelper.notInArray([2,4]))
            ).toEqual([1,3,5]);
        });
        it("extractedFieldNotInArray should work", function() {
            expect(
                $filter('filter')(testArr, FilterHelper.extractedFieldNotInArray([3,0], "value"))
            ).toEqual( [{name:'b', value:5}, {name:'a', value:2}] );
        });
        it("extractedFieldInArray should work", function() {
            expect(
                $filter('filter')(testArr, FilterHelper.extractedFieldInArray(["a"], "name"))
            ).toEqual( [{name: 'a', value: 3}, {name: 'a', value: 2}] );
        });
    });

    describe("KoodiHelper", function() {
        it("koodiValue should return koodi value without possible koodistonimi or version", function() {
            expect(KoodiHelper.koodiValue("koodisto_1234")).toEqual("1234");
            expect(KoodiHelper.koodiValue("koodi_arvo#1")).toEqual("arvo");
            expect(KoodiHelper.koodiValue("arvo#123")).toEqual("arvo");
            expect(KoodiHelper.koodiValue("123")).toEqual("123");
        });
    });

});