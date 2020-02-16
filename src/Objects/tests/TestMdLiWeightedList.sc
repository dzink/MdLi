TestMdLiWeightedList : TestMdLi {
	var list;

	setUp {
		list = MdLiWeightedList[].setId(\list);
		list[\b] = MdLiObject().setWeight(10);
		list[\a] = MdLiObject().setWeight(0);
		list[\c] = MdLiObject().setWeight(20);
	}

	tearDown {
		list.free;
	}

	test_at {
		this.assertEquals(list[\a].weight(), 0, "Gets properties of list members.");
		this.assertEquals(list[\b].weight(), 10, "Gets properties of list members.");
		this.assertEquals(list[\c].weight(), 20, "Gets properties of list members.");
	}

	test_sort {
		var sort = list.asArray();
		this.assertEquals(sort[0].id(), \a, "Sorts a first.");
		this.assertEquals(sort[1].id(), \b, "Sorts b second.");
		this.assertEquals(sort[2].id(), \c, "Sorts c third.");
	}

	test_invalidate {
		var sort = list.asArray();

		list[\a].setWeight(30);
		sort = list.asArray();
		this.assertEquals(sort[0].id(), \b, "Now b is first.");
		this.assertEquals(sort[1].id(), \c, "Now c is second.");
		this.assertEquals(sort[2].id(), \a, "Now a is third.");

		list[\d] = MdLiObject().setWeight(15).put(\id, \d);
		sort = list.asArray();
		this.assertEquals(sort[0].id(), \b, "Now b is first.");
		this.assertEquals(sort[1].id(), \d, "Now c is second.");
		this.assertEquals(sort[2].id(), \c, "Now a is third.");
		this.assertEquals(sort[3].id(), \a, "Now a is fourth.");
}

	test_keys {
		var keys = list.keys;
		this.assertEquals(keys[0], \a, "Key a is first.");
		this.assertEquals(keys[1], \b, "Key b is second.");
		this.assertEquals(keys[2], \c, "Key c is third.");
	}

	test_do {
		var l = list.weightedCollect {
			arg key, value, index;
			value.put(\order, index);
			(value.id() ++ \Meow).asSymbol;
		};
		this.assertEquals(l[0], \aMeow, "A's symbol was altered and collected");
		this.assertEquals(l[1], \bMeow, "B's symbol was altered and collected");
		this.assertEquals(l[2], \cMeow, "C's symbol was altered and collected");
		this.assertEquals(list[\a][\order], 0, "A's object was also altered");
		this.assertEquals(list[\b][\order], 1, "A's object was also altered");
		this.assertEquals(list[\c][\order], 2, "A's object was also altered");
	}

	test_weightedKeysCollect {
		var l = list.weightedKeysCollect {
			arg key, value, index;
			value.put(\order, index);
			(value.id() ++ \Meow).asSymbol;
		};
		this.assertEquals(l[\a], \aMeow, "A's symbol was altered and collected");
		this.assertEquals(l[\b], \bMeow, "B's symbol was altered and collected");
		this.assertEquals(l[\c], \cMeow, "C's symbol was altered and collected");
	}

	test_weightedGetProperty {
		var l;
		list[\b][\sound] = \arf;
		list[\a][\sound] = \woof;
		list[\c][\sound] = \ruff;
		l = list.weightedGetProperty(\sound);
		this.assertEquals(l[0], \woof, "Properties are sorted correctly");
		this.assertEquals(l[1], \arf, "Properties are sorted correctly");
		this.assertEquals(l[2], \ruff, "Properties are sorted correctly");
	}
}
