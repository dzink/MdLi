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
		arg address;
		var keys = address.asString.split($.);
		var key = keys[0].asSymbol;
		if (keys.size == 1) {
			^ super.at(key);
		} {
			var remainingKey = keys.copyRange(1, keys.size - 1).join($.);
			^ this.at(key).at(remainingKey);
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

	/**
	 * Gets a list of full-length dot-separated addresses for this and all
	 * sub-configs.
	 */
	addresses {
		arg levels = inf, prefix = "", existing;
		var list = List[];
		var keys = this.keys;
		existing = existing ?? { List[] };
		keys.do {
			arg key, index;
			var value = this.at(key);
			var base = prefix ++ key;
			if (value.isKindOf(MdLiConfig) and: (levels > 0)) {

				// Check for circular reference before adding all levels.
				if (existing.includes(value).not) {
					list.addAll(value.addresses(levels - 1, (base ++ ".").asSymbol));
					existing.add(this);
				};
			} {
				list.add(base.asSymbol);
			};
		}
		^ list;
	}

	/**
	 * Imports defaults from another config. They will not overwrite existing
	 * config.
	 */
	importDefaults {
		arg other;
		var otherKeys, myAddresses, defaultKeys;
		otherKeys = other.addresses();
		myAddresses = this.addresses();

		defaultKeys = otherKeys.select {
			arg address;
			this.isAddressOpen(address, myAddresses);
		};

		defaultKeys.do {
			arg key;
			this[key] = other[key];
		};
		this.easyRead;
		^ this;

	}

	/**
	 * Finds out if an address will overwrite existing config data if set.
	 */
	isAddressOpen {
		arg address, keys;
		var addresses = address.asString.split($.);
		addresses.size.do {
			var testAddress = addresses.join(".").asSymbol();
			if (keys.includes(testAddress)) {
				^ false;
			};
			addresses.pop();
		};
		^ true;
	}

}
