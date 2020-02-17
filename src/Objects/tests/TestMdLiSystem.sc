TestMdLiSystem {
	test_deepId {
		// Create a circular reference.
		var o = MdLiObject().setId(\object);
		var s = MdLiSystem().setId(\system);
		o.attach(\system, s);
		s.attach(\object, o);
		this.assertEquals(s.ancestor, o);
		this.assertEquals(o.ancestor, s);
		this.assertSymbolEquals(s.deepId, "system");
		this.assertSymbolEquals(o.deepId, "system.object");
	}
}
