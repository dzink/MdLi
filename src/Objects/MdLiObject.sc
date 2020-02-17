MdLiObject : Dictionary {
	classvar <> strict = true;

	var ancestor;
	var id;

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
		^ this.at(key).notNil();
	}

	attach {
		arg key, object;
		if (object.isKindOf(MdLiObject)) {
			object.setId(key);
			object.setAncestor(this);
		} {
			MdLiStrictException.throw("Tried to attach a non-MdLiObject.", 50);
		};
		super.put(key, object);
		^ this;
	}

	setAncestor {
		arg a_ancestor;
		if (a_ancestor.isKindOf(MdLiObject)) {
			ancestor = a_ancestor;
		} {
			MdLiStrictException.throw("Tried to attach a non-MdLiObject.", 50);
		};
		^ this;
	}

	/**
	 * Get a descendants iterator for this.
	 */
	descendantFinder {
		^ MdLiDescendantFinder(this);
	}

	descendants {
		arg levels = inf;
		^ this.descendantFinder().all(levels);
	}

	descendantsSelect {
		arg select, levels = inf;
		^ this.descendantFinder().select(select, levels);
	}

	descendantsSelectFromAll {
		arg select, levels = inf;
		^ this.descendantFinder().selectFromAll(select, levels);
	}

	/**
	 * Find descendants that are of a certain class type (or subclass of that).
	 */
	descendantsOfKind {
		arg kind, levels = inf;
		^ this.descendantsSelectFromAll({
			arg d; d.isKindOf(kind);
		}, levels);
	}

	ancestorFinder {
		^ MdLiAncestorFinder(this);
	}

	ancestors {
		arg levels = inf;
		^ this.ancestorFinder().all(levels);
	}

	ancestor {
		^ ancestor;
	}

	ancestorSelect {
		arg select, levels = inf;
		^ this.ancestorFinder().selectFromAll(select, levels);
	}

	ancestorAt {
		arg level = 0;
		^ this.ancestorFinder().at(level);
	}

	oldestAncestor {
		arg levels = inf;
 		^ this.ancestorFinder().oldest(levels);
	}

	firstAncestorWhere {
		arg select = true, levels = inf;
		^ this.ancestorFinder().firstWhere(select, levels);
	}

	bubbleUpFinder {
		^ MdLiBubbleUpFinder(this);
	}

	oldestBubbleUp {
		arg levels = inf;
		^ this.bubbleUpFinder().oldest(levels);
	}

	bubbleUpFirstWithProperty {
		arg key, levels = inf;
		^ this.bubbleUpFinder().firstWhere({
			arg o;
			o.hasKey(key);
		}, levels);
	}

	bubbleProperty {
		arg key, levels = inf;
		var object = this.bubbleUpFirstWithProperty(key, levels);
		if (object.notNil()) {
			^ object.at(key);
		};
		^ nil;
	}

	/**
	 * Searches for a property as in bubbleProperty. If none is found, evals the
	 * function and sets that on the ancestor.
	 */
	bubbleDefaultProperty {
		arg key, func, levels = inf;
		var value = this.bubbleProperty(key, levels);
		// if (value.exists()) {
		// 	^ value;
		// } {
			var a = this.oldestBubbleUp(levels);
			// value = func.value(this, a);
			// a.attach(key, value);
			// this.attach(key, value);
			^ value;
		// };

	}

	/**
	 * Gets (or creates) a logger.
	 */
	logger {
		// if (logger.empty()) {
		// 	var func = { MdLiLogger() };
		// 	logger = this.bubbleDefaultProperty(\logger, func);
		// }
		// ^ logger;
	}

	config {
		if (config.empty()) {
			config = MdLiConfig();
		}
		^ config;
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



	id {
		^ id;
	}

	setId {
		arg a_id;
		if (id.empty()) {
			id = a_id;
		};
		^ this;
	}

	/**
	 * Gets a long address for this object, which is all its ancestors
	 * concatenated.
	 * Uses a list to ensure no circular references.
	 */
	deepId {
		arg list;
		list = list ?? { List[] };
		^ if (ancestor.exists()
				and: {this.endsId().not()
					and: {list.includes(ancestor).not()}
				}) {
			list.add(this);
			ancestor.deepId(list) ++ ".";
		} ++ this.id();
	}

	/**
	 * Indicate when building deepId's it ends here (in case of circular
	 * references).
	 */
	endsId {
		^ false;
	}

	post {
		arg message, level = 0, type, object;
		this.logger().post(\message, level, type, object);
	}

}
