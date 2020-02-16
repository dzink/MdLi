MdLiWeightedList : MdLiObject {
	var sorted = nil;
	var <> sortProperty = \weight;

	put {
		arg key, object;
		if (object.isKindOf(MdLiObject)) {
			this.attach(key, object);
		};
		this.invalidateSort();
		^ super.put(key, object);
	}

	invalidateSort {
		sorted = nil;
		^ this;
	}

	sort {
		arg property;
		var keys = super.keys;
		property = property ?? sortProperty;
		keys = keys.asArray.sortMap {
			arg key;
			var weight = this.p_getWeight(this[key], property).asFloat;
			weight;
		};
		keys.select {
			arg key;
			this[key].isKindOf(Dictionary);
		};
		^ keys;
	}

	doWeighted {
		arg property;
		property = sortProperty;
	}

	asArray {
		^ this.asList().asArray();
	}

	asList {
		var list = List[];
		this.sortedKeys(sortProperty).asArray.do {
			arg key, index;
			list.add(this.at(key));
		};
		^ list;
	}

	keys {
		^ this.sortedKeys;
	}
	/**
	 * Returns the cached sorted list. If empty, generates it first.
	 */
	sortedKeys {
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
				^	false;
			};
		};
	}
}
