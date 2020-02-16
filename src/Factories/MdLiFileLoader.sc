MdLiFileLoader : MdLiObject {
	classvar <> rootDirectory;
	var defaultPaths;

	addDefaultPath {
		arg path, weight = 0;
		this.defaultPaths().attach(path, MdLiObject[].put(\path, path).setWeight(weight));
		^ this;
	}

	defaultPaths {
		if (defaultPaths.empty()) {
			defaultPaths = MdLiWeightedList[];
			if (rootDirectory.exists()) {
				defaultPaths.put(rootDirectory, rootDirectory);
			};
		};
		^ defaultPaths;
	}

	findFiles {
		arg path;
		var paths = this.defaultPaths().weightedCollect {
			arg key, defaultPath;
			(defaultPath +/+ path).asSymbol();
		};
		paths = paths.select {
			arg testPath;
			File.exists(testPath.asString());
		};
		^ paths;
	}

	loadFiles {
		arg paths;
		var contents = List[];
		paths.do {
			arg path, index;
			contents.add(this.readPath(path.asString()));
		};
		^ contents;
	}

	readPath {
		var path;
		var contents;
		try {
			contents = File.readAllString(path);
		} {
			this.errorHandler.throw(Exception(path ++ " is not a valid yaml file."));
		};
		^ contents;
	}

}
