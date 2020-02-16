MdLiSystem {
	var configLoader;

	configLoader {
		if (configLoader.empty) {
			configLoader = this.bubbleDefaultProperty(\configLoader, {
				MdLiConfigLoader[];
			});
		};
		^ configLoader;
	}

}
