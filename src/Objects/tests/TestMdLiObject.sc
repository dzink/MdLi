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

	test_ancestors_basic {
		this.assert(object[\sub].ancestor === object, "Object is first ancestor to sub");
		this.assert(object[\sub2].ancestor === object, "Object is first ancestor to sub2");
		this.assert(object[\sub][\subsub].ancestorAt(0) === object[\sub], "Sub is first ancestor to subsub");
		this.assert(object[\sub][\subsub].oldestAncestor(inf) === object, "Object is oldest ancestor to subsub");
		this.assert(object[\sub][\subsub].ancestors.id.includes(\sub), "Sub is in ancestors list");
		this.assert(object[\sub][\subsub].ancestors.id.includes(\object), "Object is in ancestors list");
		this.assert(object[\sub][\subsub].ancestors(0).id.includes(\sub), "Sub is in shortened ancestors list");
		this.assert(object[\sub][\subsub].ancestors(0).id.includes(\object).not, "Object is not in shortened ancestors list");
	}

	test_ancestorSelect {
		var a = object[\sub][\subsub].ancestorSelect({
			arg o;
			o.id == \object;
		});
		this.assert(a.includes(object), "Object is a selected ancestor.");
		this.assert(a.includes(object[\sub]).not, "Sub is not a selected ancestor.");
	}

	test_firstAncestorWhere {
		var a;
		a = object[\sub][\subsub].firstAncestorWhere({
			arg o;
			[\id, o.id].postln;
			o.id == \object;
		});
		this.assert(a === object, "Object is the first selected ancestor.");
		a.postln;
		a = object[\sub][\subsub].firstAncestorWhere({
			arg o;
			o.id == \sub;
		});
		this.assert(a === object[\sub], "Sub is now the first selected ancestor and we stop looking.");
	}

	test_bubbleProperty {
		// object[\p1] = \p1;
		// this.assertEquals(object[\sub][\subsub].bubbleProperty(\p1), \p1, "Properties bubble down to descendents.");
	}

	test_bubblePropertyDo {
		// var obj, self, ancestor, result;
		// var p = MdLiObject[];
		// object.attach(\p, p);
		// result = object[\sub][\subsub].bubblePropertyDo(\p, {
		// 	arg o, t, a;
		// 	obj = o;
		// 	self = t;
		// 	ancestor = a;
		// 	120
		// });
		// this.assertEquals(obj.id(), \p, "bubblePropertyDo gets the object.");
		// this.assertEquals(self.id(), \subsub, "bubblePropertyDo gets this.");
		// this.assertEquals(ancestor.id(), \object, "bubblePropertyDo gets ancestor.");
		// this.assertEquals(result, 120, "bubblePropertyDo gets the correct result.");
	}

	test_errorHandler {
		// this.assert(object[\sub][\subsub].errorHandler().isKindOf(MdLiErrorHandler), "Error Handler is automatically built.");
		// this.assert(object[\sub][\subsub].errorHandler() === object.errorHandler(), "Descendants share an errorHandler with object.");
	}

	test_logger {
		// this.assert(object[\sub][\subsub].logger().isKindOf(MdLiLogger), "Logger is automatically built.");
		// this.assert(object[\sub][\subsub].logger() === object.logger(), "Descendants share a logger with object.");
	}

	test_descendants {
		// var d;
		// d = object.descendants().collect { arg c; c.id() };
		// this.assert(d.includes(\sub), "Sub is in descendants");
		// this.assert(d.includes(\otherId), "OtherId is in descendants");
		// this.assert(d.includes(\subsub), "Sub-sub is in descendants");
		//
		// d = object.descendantsSelect({
		// 	arg d; d.id() != \sub;
		// }).collect { arg c; c.id() };
		// this.assert(d.includes(\sub).not, "Sub is not in selected descendants");
		// this.assert(d.includes(\otherId), "OtherId is in selected descendants");
		// this.assert(d.includes(\subsub).not, "Sub-sub is not in selected descendants");
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
