MdLiWeightedList : MdLiObject {
	var sorted = nil;
	var <> sortProperty = \weight;

	put {
		arg key, object, weight = nil;
		if (object.isKindOf(MdLiObject)) {
			this.attach(key, object);
			if (weight.exists()) {
				object.setWeight(weight);
			};
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
			var weight = (this.p_getProperty(this[key], property) ?? 0).asFloat;
			weight;
		};
		keys.select {
			arg key;
			this[key].isKindOf(Dictionary);
		};
		^ keys;
	}

	weightedDo {
		arg func;
		this.weightedCollect(func);
		^ this;
	}

	weightedCollect {
		arg func;
		var list = List[];
		this.sortedKeys().asArray.do {
			arg key, index;
			list.add(func.value(key, this.at(key), index));
		};
		^ list;
	}

	weightedKeysCollect {
		arg func;
		var list = MdLiWeightedList[];
		this.sortedKeys().asArray.do {
			arg key, index;
			list.put(key, func.value(key, this.at(key), index));
		};
		^ list;
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
		if (sorted.empty()) {
			sorted = this.sort();
		};
		^ sorted;
	}

	/**
	 * Get the weight of an object. First see if there's a method, then check
	 * dictionary. If not, just return 0.
	 */
	p_getProperty {
		arg object, property;
		if (object.respondsTo(property.asSymbol)) {
			^ (object.perform(property.asSymbol));
		} {
			if (object.isKindOf(Dictionary)) {
				^ (object[property]);
			} {
				^	nil;
			};
		};
	}

	weightedGetProperty {
		arg property;
		var list = List[];
		this.sortedKeys().asArray.do {
			arg key, index;
			var prop = this.p_getProperty(this.at(key), property);
			if (prop.exists()) {
				list.add(prop);
			};
		};
		^ list;
	}
}
