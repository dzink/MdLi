TestMdLiWeightedList : TestMdLi {
	var list;

	setUp {
		list = MdLiWeightedList[];
		list[\a] = MdLiObject().setWeight(0);
		list[\b] = MdLiObject().setWeight(1);
		list[\c] = MdLiObject().setWeight(2);
	}

	tearDown {
		list.free;
	}

	test_at {
		this.assertEquals(list[\a].weight(), 0, "Gets properties of list members.");
		this.assertEquals(list[\b].weight(), 1, "Gets properties of list members.");
		this.assertEquals(list[\c].weight(), 2, "Gets properties of list members.");
	}
}
