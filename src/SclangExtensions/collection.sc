+ Collection {
	/**
	 * Two functions that
	 */
	collectMethod {
		arg method ...args;
		^ this.collect {
			arg n;
			if (n.respondsTo(method)) {
				n.perform(method, *args);
			} {
				nil;
			};
		};
	}

	id {
		^ this.collectMethod(\id);
	}

	deepId {
		^ this.collectMethod(\deepId);
	}
	
}
