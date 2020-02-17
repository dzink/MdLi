TestMdLiObject : TestMdLi {
	var object;

	setUp {
		object = MdLiObject[].setId(\object);
		object.attach(\sub, MdLiObject[]);
		object.attach(\sub2, MdLiObject[].setId(\otherId));
		object[\sub].attach(\subsub, MdLiObject[]);
	}

	tearDown {
		object.free;
	}

	test_at {
		this.assertEquals(object[\sub].id(), \sub, "Sub objects are properly attached");
		this.assertEquals(object[\sub2].id(), \otherId, "Id can be customized");
	}

	// test_strict {
	// 	var strict = MdLiObject.strict, verbosity, rethrow;
	// 	verbosity = object.errorHandler().verbosity;
	// 	rethrow = object.errorHandler().rethrow;
	//
	// 	object.errorHandler().verbosity = 0;
	// 	object.errorHandler().rethrow = true;
	//
	// 	MdLiObject.strict = true;
	// 	this.assertException({object.noOp()}, DoesNotUnderstandError, "Exception on noOp function when strict.");
	// 	MdLiObject.strict = false;
	// 	this.assertNoException({object.noOp()}, "No exception when strict is turned off.");
	// 	MdLiObject.strict = strict;
	// 	object.errorHandler().verbosity = verbosity;
	// 	object.errorHandler().rethrow = rethrow;
	// }

	test_parent {
		this.assertEquals(object[\sub].ancestor().id(), \object, "Object is first ancestor to sub");
		this.assertEquals(object[\sub2].ancestor().id(), \object, "Object is first ancestor to sub2");
		this.assertEquals(object[\sub][\subsub].ancestors(1).id(), \sub, "Sub is first ancestor to subsub");
		this.assertEquals(object[\sub][\subsub].ancestors(inf).id(), \object, "Object is oldest ancestor to subsub");
	}

	test_bubbleProperty {
		object[\p1] = \p1;
		this.assertEquals(object[\sub][\subsub].bubbleProperty(\p1), \p1, "Properties bubble down to descendents.");
	}

	test_bubblePropertyDo {
		var obj, self, ancestor, result;
		var p = MdLiObject[];
		object.attach(\p, p);
		result = object[\sub][\subsub].bubblePropertyDo(\p, {
			arg o, t, a;
			obj = o;
			self = t;
			ancestor = a;
			120
		});
		this.assertEquals(obj.id(), \p, "bubblePropertyDo gets the object.");
		this.assertEquals(self.id(), \subsub, "bubblePropertyDo gets this.");
		this.assertEquals(ancestor.id(), \object, "bubblePropertyDo gets ancestor.");
		this.assertEquals(result, 120, "bubblePropertyDo gets the correct result.");
	}

	test_errorHandler {
		this.assert(object[\sub][\subsub].errorHandler().isKindOf(MdLiErrorHandler), "Error Handler is automatically built.");
		this.assert(object[\sub][\subsub].errorHandler() === object.errorHandler(), "Descendants share an errorHandler with object.");
	}

	test_logger {
		this.assert(object[\sub][\subsub].logger().isKindOf(MdLiLogger), "Logger is automatically built.");
		this.assert(object[\sub][\subsub].logger() === object.logger(), "Descendants share a logger with object.");
	}

	test_descendants {
		var d;
		d = object.descendants().collect { arg c; c.id() };
		this.assert(d.includes(\sub), "Sub is in descendants");
		this.assert(d.includes(\otherId), "OtherId is in descendants");
		this.assert(d.includes(\subsub), "Sub-sub is in descendants");

		d = object.descendantsSelect({
			arg d; d.id() != \sub;
		}).collect { arg c; c.id() };
		this.assert(d.includes(\sub).not, "Sub is not in selected descendants");
		this.assert(d.includes(\otherId), "OtherId is in selected descendants");
		this.assert(d.includes(\subsub).not, "Sub-sub is not in selected descendants");
	}

	test_addresses {
		this.assertEquals(object[\sub][\subsub].deepId(), "object.sub.subsub", "addresses are generated correctly.");
	}

	test_circularAddresses {
		var o1 = MdLiObject().setId(\o1);
		var o2 = MdLiObject().setId(\o2);
		o1.attach(\o2, o2);
		o2.attach(\o1, o1);

		this.assertEquals(o1.deepId(), "o2.o1", "Resolves circular addresses");
		this.assertEquals(o2.deepId(), "o1.o2", "Resolves circular addresses");
	}

}
