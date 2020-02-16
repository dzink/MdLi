TestMdLiNil : TestMdLi {
	test_nil {
		var n = MdLiNil();
		this.assert(n.empty(), "MdLiNil is empty.");
		this.assert(nil.empty(), "Nil is empty.");
		this.assert(1.exists(), "1 is not empty.");
		this.assert(n.exists().not, "MdLiNil does not exist.");
		this.assert(nil.exists().not, "Nil does not exist.");
		this.assert(1.exists(), "1 exists.");
		this.assert(n.empty(), "N is nil.");
	}
}
