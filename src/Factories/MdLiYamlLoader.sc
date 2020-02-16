MdLiYamlLoader : MdLiFileLoader {

	readPath {
		arg path;
		var yamlConfig;

		// try {
			yamlConfig = path.parseYAMLFile();
		// } {
		// 	this.errorHandler.throw(Exception(path ++ " is not a valid yaml file."));
		// };
		^ yamlConfig;
	}

}
