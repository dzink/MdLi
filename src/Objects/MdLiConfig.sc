MdLiConfig : MdLiObject {
	classvar <> rootDirectory = nil;
	var <> type;

	*fromObject {
		arg object;
		var config = MdLiConfig[];
		object.keysValuesDo {
			arg key, value;
			config.put(key, value);
		};
		^ config;
	}

	at {
		arg key;
		key = key.asString.split($.);
		if (key.size == 1) {
			^ super.at(key[0].asSymbol);
		} {
			var remainingKey = key.copyRange(1, key.size - 1).join($.);
			^ this.at(key[0]).at(remainingKey);
		}
	}

	put {
		arg key, value;
		var keys = key.asString.split($.);

		// It's a single object.
		if (keys.size == 1) {
			var object = MdLiConfigBuilder.convertObject(value);
			if (object.isKindOf(MdLiObject)) {
				this.attach(key.asSymbol, object);
			} {
				if (object.isKindOf(MdLiNil)) {
					super.put(key.asSymbol, MdLiNil());
				} {
					super.put(key.asSymbol, object);
				};
			};

		// It's a long concatenated key.
		} {
			var remainingKey = keys.copyRange(1, keys.size - 1).join($.);
			if (this.hasKey(keys[0]).not) {
				this.put(keys[0], MdLiConfig[]);
			};
			this.at(keys[0]).put(remainingKey, value);
		}

		^ this;
	}

	removeAt {
	arg key, value;
	var keys = key.asString.split($.);

	// It's a single object.
	if (keys.size == 1) {
		var value = this.at(key);
		super.removeAt(key.asSymbol);

	// It's a long concatenated key.
	} {
		var remainingKey = keys.copyRange(1, keys.size - 1).join($.);
		this.at(keys[0]).removeAt(remainingKey);
	}
	}

	easyRead {
		var list = this.prEasyRead("", List[]);
		list.do {
			arg line;
			line.postln;
		};
	}

	prEasyRead {
		arg prefix = "", list;
		this.configDo {
			arg key, value;
			key = prefix ++ key;
			if (value.isKindOf(MdLiObject)) {
				list = value.prEasyRead(key ++ ".", list);
			} {
				list.add(key ++ " -> " ++ value ++ " (" ++ value.class ++ ")");
			};
		};
		^ list;
	}

	configDo {
		arg func = {};
		var keys = this.keys();
		keys.do {
			arg key, index;
			var value = this.at(key);
			func.value(key, value, index);
		};
	}

	keys {
		var keys = super.keys;
		if (this.type == \array) {
			^ keys.asArray().asInteger().sort();
		};
		^ keys.asArray().sort();
	}


}
