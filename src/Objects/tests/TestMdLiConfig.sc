TestMdLiConfig : TestMdLi {
	var config;

	setUp {
		config = MdLiConfig[
			"a" -> 1,
			"b" -> 2,
			"c" -> 3,
			"hash.a" -> 4,
			"hash.b" -> 5,
			"hash.c" -> 6,
			"hash.d" -> nil,
		];
	}

	tearDown {
		config.free();
	}

	test_assignment {
		config.removeAt("hash.d");
		config.easyRead;
		this.assertEquals(config[\a], 1, "a is 1");
		this.assertEquals(config[\hash][\a], 4, "hash.a is 4");
		this.assertEquals(config["hash.a"], 4, "hash.a is 4");
	}
}
