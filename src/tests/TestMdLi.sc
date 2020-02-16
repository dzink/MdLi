TestMdLi : UnitTest {
	assertSymbolEquals {
		arg object, test, message;
		^ this.assertEquals(object.asSymbol, test.asSymbol, message);
	}
}
