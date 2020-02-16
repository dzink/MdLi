MdLiConfigBuilder : MdLiObject {

	*convertObject {
		arg object;
		if (object.isKindOf(MdLiConfig)) {
			^ object;
		};
		if (object.isKindOf(Dictionary)) {
			^ MdLiConfigBuilder.convertDictionary(object);
		};
		if (object.isKindOf(Collection) and: object.isKindOf(String).not) {
			^ MdLiConfigBuilder.convertArray(object);
		};

		// Really shouldn't get here...
		^ MdLiConfigBuilder.convertScalar(object);
	}

	*convertArray {
		arg array;
		var config = MdLiConfig[];
		config.type = \array;
		array.do {
			arg value, index;
			config[index.asSymbol] = value;
		};
		^ config;
	}

	*convertDictionary {
		arg dict;
		var config = MdLiConfig[];
		config.type = \dict;
		dict.keysValuesDo {
			arg key, value;
			config.put(key.asSymbol, value);
		};
		^ config;
	}

	*convertScalar {
		arg scalar;
		if (scalar.isNil()) {
			^ MdLiNil();
		};
		scalar = scalar.asString();
		if ("^\\s*[+-]?[0-9]+(\\.[0-9]+)?(e[0-9]+)?\\s*$".matchRegexp(scalar)) {
			^scalar.asFloat;
		};
		if (scalar.compare("\\nil") == 0) {
			^ MdLiNil();
		}
		^ scalar.asSymbol;
	}
}
