MdLiWeightedList : MdLiObject {
	var sorted = nil;
	var <> sortProperty = \weight;

	put {
		arg key, object;
		this.invalidateSort();
		^ parent.put(key, object);
	}

	invalidateSort {
		sorted = nil;
		^ this;
	}

	sort {
		arg property;
		var keys = this.keys;
		property = property ?? sortProperty;
		keys = keys.asArray.sortMap {
			arg key;
			var weight = this.p_getWeight(this[key], property).asFloat;
			weight;
		};
		^ keys;
	}

	/**
	 * Performs a function for each member of the list in order.
	 * @return MdLiWeightedList
	 *   A list with the results of the function.
	 */
	do {
		arg func;
		var list = MdLiWeightedList();
		this.sorted(sortProperty).asArray.do {
			arg key, index;
			list[key] = func.value(this.at(key), index);
		};
		^ list;
	}

	/**
	 * Performs a function for each member of the list in order.
	 * @return MdLiWeightedList
	 *   A list with the results of the function.
	 */
	keysValuesDo {
		arg func;
		var list = MdLiWeightedList();
		this.sorted(sortProperty).asArray.do {
			arg key, index;
			list[key] = func.value(key, this.at(key), index);
		};
		^ list;
	}

	/**
	 * Performs a function for each member of the list in order.
	 * @return MdLiWeightedList
	 *   A list with the results of the function.
	 */
	keysDo {
		arg func;
		var list = MdLiWeightedList();
		this.sorted(sortProperty).asArray.do {
			arg key, index;
			list[key] = func.value(key, index);
		};
		^ list;
	}

	asArray {
		^ this.asList().asArray();
	}

	asList {
		var list = List();
		this.sorted(sortProperty).asArray.do {
			arg key, index;
			list.add(this.at(key));
		};
		^ list;
	}

	/**
	 * Returns the cached sorted list. If empty, generates it first.
	 */
	sorted {
		if (sorted.isNil) {
			sorted = this.sort();
		};
		^ sorted;
	}

	/**
	 * Get the weight of an object. First see if there's a method, then check
	 * dictionary. If not, just return 0.
	 */
	p_getWeight {
		arg object, property;
		if (object.respondsTo(property.asSymbol)) {
			^ (object.perform(property.asSymbol) ?? 0).asFloat;
		} {
			if (object.isKindOf(Dictionary)) {
				^ (object[property] ?? 0).asFloat;
			} {
				^	0;
			};
		};
	}


}
