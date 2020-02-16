TestMdLiConfig : TestMdLi {
	var config;
	var other;

	setUp {
		config = MdLiConfig[
			"a" -> 1,
			"b" -> 2,
			"c" -> 3,
			"hash.aa" -> 4,
			"hash.bb" -> 5,
			"hash.cc" -> 6,
			"def.aaa" -> 7,
			"def.bbb" -> 7,
			"def.ccc" -> 7,
			"hash.d" -> nil,
		];

		other = MdLiConfig[
			"a" -> 9,
			"b" -> 8,
			"hash.aa" -> 6,
			"def" -> nil,
		];
	}

	tearDown {
		config.free();
	}

	test_assignment {
		config.removeAt("hash.d");
		this.assertEquals(config[\a], 1, "a is 1");
		this.assertEquals(config[\hash][\aa], 4, "hash.a is 4");
		this.assertEquals(config["hash.aa"], 4, "hash.a is 4");
	}

	test_setDefaults {
		other.importDefaults(config);
		this.assertFloatEquals(other[\a], 9, "a is not imported");
		this.assertFloatEquals(other[\b], 8, "b is not imported");
		this.assertFloatEquals(other[\c], 3, "c is imported");
		this.assertFloatEquals(other["hash.aa"], 6, "hash.aa is not imported");
		this.assertFloatEquals(other["hash.bb"], 5, "hash.bb is imported");
		this.assertFloatEquals(other["hash.cc"], 6, "hash.cc is imported");
		this.assert(other["hash.d"].empty(), "hash.d is nil");
		this.assert(other["def"].empty(), "def is nil");
	}

	test_circular{
		var c = MdLiConfig();
		c.put(\c, c);
		c.put(\b, MdLiConfig[\g -> 1]);
		this.assert(c["c.c.c.c"] === c, "C's are idempotent");
		this.assert(c["c.c.c.b.g"] == 1, "Appending a ton of c's with an object");
		this.assert(c["b.g"] == 1, "Dropping the c's entirely");
		this.assertEquals(c.addresses.size, 1, "Addresses should only be added once");
		this.assertSymbolEquals(c.addresses[0], "b.g", "Addresses should only have one repeated object");
	}

}
