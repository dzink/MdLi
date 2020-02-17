MdLiDescendantFinder {
	var < object;

	*new {
		arg object;
		var f = super.new();
		f.setObject(object);
		^ f;
	}

	setObject {
		arg a_object;
		object = a_object;
		^ this;
	}

	/**
	 * Finds all descendants.
	 */
	all {
		arg levels = inf;
		^ this.prDescendants(object, levels, select: true);
	}

	/**
	 * Selects descendants. If a child does not match the criteria, do not look
	 * at its children.
	 */
	select {
		arg select, levels = inf;
		^ this.prDescendants(object, levels, select: select);
	}

	/**
	 * Gets all descendants, and then selects from them.
	 */
	selectFromAll {
		arg filter, levels = inf;
		var d = this.prDescendants(object, levels, select: true);
		^ d.select(filter);
	}

	/**
	 * Find all descendants, and optionall filter them;
	 */
	prDescendants {
		arg o, levels = inf, select = nil, previous = nil;
		var descendants = previous ?? List[];
		select = select ?? true;
		o.do {
			arg child;
			if (child.isKindOf(MdLiObject) and: select.value(child)) {
				descendants.add(child);
				this.prDescendants(child, levels - 1, select, descendants);
			};
		};
		^ descendants;
	}
}
