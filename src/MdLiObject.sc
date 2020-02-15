MdLiObject : IdentityDictionary {
	classvar strict = true;
	var <> parent;

	/**
	 * Allows Event style pseudomethods.
	 */
	doesNotUnderstand {
		arg key, value;
		if (strict) {
			this.errorHandler().throw("Strict Error: " ++ key);
		};
		key = key.asString();
		if (key.endsWith("_")) {
			key = key.keep(key.size - 1);
			this.[key.asSymbol] = value;
			^ this;
		} {
			key = key;
			value = this.at(key.asSymbol);
			^ value;
		};
	}

	/**
	 * Finds among parents a given key.
	 */
	parentsKey {
		arg key, levels = inf;
		if (this.parent.isNil.not) {
			if (parent.hasKey(key)) {
				^ parent.at(key);
			};
			if (levels == 0) {
				^ nil;
			};
			^ this.parent.parentsKey(key, levels - 1);
		};
		^ nil;
	}

	/**
	 * Finds single ancestor at a given level. If levels don't go that high, find
	 * highest ancestor
	 */
	 ancestor {
	 	arg levels = inf;
		if (this.parent.isNil.not) {
			if (levels == 0) {
				^ this;
			};
			^ this.parent.ancestor(levels - 1);
		};
		^ this;
	 }

	/**
	 * Bubbles up through parents to find a given key.
	 */
	bubbleKey {
		arg key;
		if (this.hasKey(key)) {
			^ this.at(key);
		};
		^ this.parentsKey(key);
	}

	setAncestor {

	}

	attachObject {
		arg key, object;
		object.parent = this;
		^ this;
	}

	/**
	 * Finds the errorHandler; if none exists, create one.
	 */
	errorHandler {
		var object = this.bubbleKey(\errorHandler);
		var ancestor, handler;
		if (object.isNil.not) {
			^ object;
		};
		ancestor = this.ancestor();
		handler = MdLiErrorHandler();
		ancestor.attachObject(\errorHandler, handler);
		^ handler;
	}

	weight {
		^ this.at(\weight) ?? 0;
	}

	setWeight {
		arg weight;
		this.put(\weight, weight);
		if (this.parent.respondsTo(\invalidateSort)) {
			this.parent.invalidateSort;
		};
		^ this;
	}

	childrenKeys {
		^ [];
	}

	children {
		arg levels = inf;
		var children = MdLiWeightedList();
		this.childrenKeys.do {
			arg key;
			var list = this.at(key);
		};

	}

}
