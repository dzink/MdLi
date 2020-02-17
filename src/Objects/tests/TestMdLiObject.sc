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

	test_descendants_basic {
		this.assert(object.descendants.includes(object[\sub]), "Object's descendants includes sub.");
		this.assert(object.descendants.includes(object[\sub][\subsub]), "Object's descendants includes subsub.");
		this.assert(object.descendants.includes(object[\sub2]), "Object's descendants includes sub2.");
		this.assert(object.descendants.includes(object).not, "Object's descendants do not include object.");
	}

	test_descendantsSelect {
		var d;
		object[\sub2].attach(\subsub, MdLiObject());
		d = object.descendantsSelect({
			arg o;
			o.id() == \sub or: { o.id() == \subsub};
		});
		this.assert(d.includes(object[\sub]), "Select finds sub and continues.");
		this.assert(d.includes(object[\sub][\subsub]), "Select finds subsub");
		this.assert(d.includes(object[\sub2]).not, "Select does not find sub2, and stops.");
		this.assert(d.includes(object[\sub2][\subsub]).not, "Select does not find sub2's child.");
	}

	test_descendantsOfKind {
		var d;
		object[\sub2].attach(\subsub, MdLiServer());
		d = object.descendantsOfKind(MdLiServer);
		this.assert(d.includes(object[\sub2][\subsub]), "Select finds the MdLiServer.");
		this.assertEquals(d.size, 1, "Select only found the MdLiServer");
	}

	test_descendantsSelectFromAll {
		var d;
		object[\sub2].attach(\subsub, MdLiObject());
		d = object.descendantsSelectFromAll({
			arg o;
			o.id() == \subsub;
		});
		this.assert(d.includes(object[\sub]).not, "Select does not match sub.");
		this.assert(d.includes(object[\sub][\subsub]), "Select finds sub's child subsub");
		this.assert(d.includes(object[\sub2]).not, "Select does not match sub2.");
		this.assert(d.includes(object[\sub2][\subsub]), "Select finds sub2's child subsub");
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
			o.id == \object;
		});
		this.assert(a === object, "Object is the first selected ancestor.");
		a = object[\sub][\subsub].firstAncestorWhere({
			arg o;
			o.id == \sub;
		});
		this.assert(a === object[\sub], "Sub is now the first selected ancestor and we stop looking.");
	}

	test_bubbleProperty {
		object[\p1] = \p1;
		this.assertEquals(object[\sub][\subsub].bubbleProperty(\p1), \p1, "Bubble up finds the property.");
	}

	test_bubbleDefaultProperty {
		object[\p1] = \p1;
		this.assertEquals(object[\sub][\subsub].bubbleDefaultProperty(\p1), \p1, "Bubble up finds the property.");
		this.assertEquals(object[\sub][\subsub].bubbleDefaultProperty(\p2, { \bloop }), \bloop, "Bubble up sets the property.");
		this.assertEquals(object[\p2], \bloop, "The property is now set on the oldest object.");
		this.assertEquals(object[\sub][\subsub][\p2], \bloop, "The property is now set on the dscendant object as well.");
	}

	test_oldestBubbleUp {
		this.assert(object.oldestBubbleUp() === object, "Self bubble up works.");
		this.assert(object[\sub].oldestBubbleUp() === object, "Single level bubble up works.");
		this.assert(object[\sub][\subsub].oldestBubbleUp() === object, "Multi level bubble up works.");
	}

	test_isDescendantOf {
		this.assert(object[\sub].isDescendantOf(object), "Sub is a descendant of object.");
		this.assert(object[\sub][\subsub].isDescendantOf(object), "Subsub is a descendant of object.");
		this.assert(object[\sub2].isDescendantOf(object[\sub]).not, "Sub2 is not a descendant of Sub.");
	}

	test_logger {
		this.assert(object[\sub][\subsub].logger().isKindOf(MdLiLogger), "Logger is automatically built.");
		this.assert(object[\sub][\subsub].logger() === object.logger(), "Descendants share a logger with object.");
	}

	test_addresses {
		this.assertEquals(object[\sub][\subsub].deepId(), "object.sub.subsub", "addresses are generated correctly.");
	}

	test_circular {
		var o1 = MdLiObject().setId(\o1);
		var o2 = MdLiObject().setId(\o2);
		o1.attach(\o2, o2);
		o2.attach(\o1, o1);

		this.assertEquals(o1.deepId(), "o2.o1", "Resolves circular addresses");
		this.assertEquals(o2.deepId(), "o1.o2", "Resolves circular addresses");
		this.assertEquals(o2.ancestors().size, 1, "Only 1 ancestor");
		this.assertEquals(o2.descendants().size, 1, "Only 1 descendant");
		this.assert(o2.oldestBubbleUp() == o1, "o1 is o2's oldest Bubble up");
	}

}
