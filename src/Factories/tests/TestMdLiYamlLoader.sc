TestMdLiYamlLoader : TestMdLiFileLoader {
	var loader;

	setUp {
		loader = MdLiYamlLoader[];
	}

	tearDown {
		loader.free;
	}


	test_findFiles {
		if (enabled) {
			var file, files, contents, object;
			file = "test.yaml";
			files = loader.findFiles(file);
			this.assert(files.includes((MdLiFileLoader.rootDirectory +/+ file).asSymbol), "File is found.");

			contents = loader.loadFiles(files);

			// Get and test the first (only) file.
			object = (contents[0]);
			this.assertSymbolEquals(object["parent"]["c2"], \eight);
			this.assertSymbolEquals(object["a"], 1.5);
			this.assertSymbolEquals(object["array"][2], 6);
		};
	}
}
