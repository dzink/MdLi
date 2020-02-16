MdLiObject : Dictionary {
	classvar <> strict = true;

	var < ancestor;
	var id;

	var errorHandler;
	var logger;
	var config;

	// /**
	//  * Allows Event style pseudomethods.
	//  */
	// doesNotUnderstand {
	// 	arg key, value;
	// 	if (strict) {
	// 		this.errorHandler().throw(DoesNotUnderstandError.new("Strict Error: " ++ key));
	// 	};
	// 	key = key.asString();
	// 	if (key.endsWith("_")) {
	// 		key = key.keep(key.size - 1);
	// 		this.[key.asSymbol] = value;
	// 		^ this;
	// 	} {
	// 		key = key;
	// 		value = this.at(key.asSymbol);
	// 		^ value;
	// 	};
	// }

	hasKey {
		arg key;
		^ this.at(key).isKindOf(Nil).not;
	}

	/**
	 * Finds single ancestor at a given level. If levels don't go that high, find
	 * highest ancestor
	 */
	 ancestors {
	 	arg levels = 1;
		if (ancestor.isNil.not) {
			if (levels == 0) {
				^ this;
			};
			^ ancestor.ancestors(levels - 1);
		};
		^ this;
	 }

	attach {
		arg key, object;
		if (object.isKindOf(MdLiObject)) {
			object.setId(key);
			object.setAncestor(this);
		} {
			this.errorHandler.throw("Tried to attach a non-MdLiObject.")
		};
		super.put(key, object);
		^ this;
	}

	setAncestor {
		arg a_ancestor;
		if (a_ancestor.isKindOf(MdLiObject)) {
			ancestor = a_ancestor;
		} {
			this.errorHandler.throw("Tried to make a non-MdLiObject a ancestor.")
		};
		^ this;
	}

	descendants {
		arg levels = inf, select = nil, previous = nil;
		var descendants = previous ?? List[];
		select = select ?? true;
		this.do {
			arg child;
			if (child.isKindOf(MdLiObject) and: select.value(child)) {
				descendants.add(child);
				descendants.addAll(child.descendants(levels - 1, select, previous));
			};
		};
		^ descendants;
	}


	errorHandler {
		if (errorHandler.isKindOf(Nil)) {
			var func = { MdLiErrorHandler() };
			errorHandler = this.bubbleDefaultProperty(\errorHandler, func);
		}
		^ errorHandler;
	}

	logger {
		if (logger.isNil) {
			var func = { MdLiLogger() };
			logger = this.bubbleDefaultProperty(\logger, func);
		}
		^ logger;
	}

	config {
		if (config.isNil) {
			config = MdLiConfig();
		}
		^ config;
	}


	/**
	 * Searches for a property as in bubbleProperty. If none is found, evals the
	 * function and sets that on the ancestor.
	 */
	bubbleDefaultProperty {
		arg key, func, levels = inf;
		var bubble = this.bubbleProperty(key, levels);
		if (bubble.isKindOf(Nil).not) {
			^ bubble;
		} {
			var a = this.ancestors(levels);
			var object = func.value(this, a);
			a.attach(key, object);
			this.attach(key, object);
			^ object;
		};
	}


	/**
	 * Finds among ancestors a given key.
	 */
	ancestorProperty {
		arg key, levels = inf;
		if (ancestor.isNil.not) {
			if (ancestor.hasKey(key)) {
				^ ancestor.at(key);
			};
			if (levels == 0) {
				^ nil;
			};
			^ ancestor.ancestorProperty(key, levels - 1);
		};
		^ nil;
	}


	/**
	 * Bubbles up through ancestors to find a given key.
	 */
	bubbleProperty {
		arg key, levels = inf;
		if (this.hasKey(key)) {
			^ this.at(key);
		};
		^ this.ancestorProperty(key, levels - 1);
	}

	/**
	 * Bubble up and find a property. If found, perform the function on it.
	 */
	bubblePropertyDo {
		arg key, func, levels = inf;
		var o = this.bubbleProperty(key, levels);
		if (o.isKindOf(Nil).not) {
			var parent = if (o.isKindOf(MdLiObject)) { o.ancestor } { nil };
			^ func.value(o, this, parent);
		}
		^ nil;
	}

	//
	setAncestorProperty {
		arg key, object, levels = inf;
		var a = this.ancestors(levels);
		a[key] = object;
		^ this;
	}


	weight {
		^ this.at(\weight) ?? 0;
	}

	setWeight {
		arg weight;
		this.put(\weight, weight);
		if (this.ancestor.respondsTo(\invalidateSort)) {
			this.ancestor.invalidateSort;
		};
		^ this;
	}



	// childrenKeys {
	// 	^ [];
	// }
	//
	// children {
	// 	arg levels = inf;
	// 	var children = MdLiWeightedList();
	// 	this.childrenKeys.do {
	// 		arg key;
	// 		var list = this.at(key);
	// 	};
	//
	// }

	id {
		^ id;
	}

	setId {
		arg a_id;
		if (id.isNil) {
			id = a_id;
		};
		^ this;
	}

}
