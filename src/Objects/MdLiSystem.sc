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

	bootServers {
		var servers = this.descendants(1, {
			arg d;
			d.isKindOf(MdLiServer);
		});
		servers.do {
			arg server;
			server.postln;
		};
	}

	/**
	 * Indicate when building deepId's it ends here (in case of circular
	 * references).
	 */
	endsId {
		^ true;
	}

}
