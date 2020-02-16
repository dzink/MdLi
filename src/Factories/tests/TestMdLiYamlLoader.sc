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
			object = MdLiConfig.fromObject(contents[0]);
			// this.assertFloatEquals(object[\a], 1.5, "Basic dictionary functionality");
			// this.assertFloatEquals(object[\array][1], 5, "Basic array functionality");
			// this.assertEquals(object[\parent][\c3], \nine, "Basic nested dictionary functionality");
			// this.assertEquals(object["parent.c3"], \nine, "Dot-style selection");
		};
	}
}
