MdLiDescendantFinder : MdLiAbstractRelativeFinder {
	var < object;

	/**
	 * Find all descendants, and optionall filter them;
	 */
	prRelatives {
		arg o, levels = inf, select = true, continue = true, list = nil;

		o.do {
			arg child;
			if (child.isKindOf(MdLiObject)
					and: { list.includes(child).not }
			) {
				var selected = select.value(child);
				if (selected) {
					list.add(child);
				};
				if (levels > 0 and: {continue.value(child, selected)}) {
					this.prRelatives(child, levels - 1, select, continue, list);
				};
			};
		};
		^ list;
	}
}
