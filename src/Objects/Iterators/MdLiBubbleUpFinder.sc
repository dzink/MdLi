/**
 * The BubbleUpFinder is like the ancestor finder, only it will also include the original object.
 */

MdLiBubbleUpFinder : MdLiAncestorFinder {

	/**
	 * Find all ancestors, and optionall filter them;
	 */
	prRelatives {
		arg o, levels = inf, select = true, continue = true, list = nil;

		var selected;
		if (o.isKindOf(MdLiObject) and: { list.includes(o).not }) {
			selected = select.value(o);
			if (selected) {
				list.add(o);
			};
			if (levels > 1
				and: { o.ancestor.exists()
					and: { continue.value(o, selected)
			}}) {
				this.prRelatives(o.ancestor, levels - 1, select, continue, list);
			};
		}
		^ list;
	}

}
