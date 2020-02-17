MdLiAncestorFinder : MdLiAbstractRelativeFinder {

	at {
		arg index = 0;
		var relatives = this.all(index + 1);
		^ relatives.at(index);
	}

	oldest {
		arg levels = inf;
		var relatives = this.all(levels);
		^ relatives.pop();
	}

	firstWhere {
		arg select, levels = inf;
		var continue = {
			arg o, selected;
			selected.not;
		};
		^ this.selectBlocking(select, continue, levels).pop();
	}

	/**
	 * Find all ancestors, and optionall filter them;
	 */
	prRelatives {
		arg o, levels = inf, select = true, continue = true, list = nil;

		if (o.ancestor.exists()) {
			var ancestor = o.ancestor;
			if (ancestor.isKindOf(MdLiObject)
					and: { list.includes(ancestor).not }
			) {
				var selected = select.value(ancestor);
				if (selected) {
					list.add(ancestor);
				};
				if (levels > 0 and: { continue.value(ancestor, selected) }) {
					this.prRelatives(ancestor, levels - 1, select, continue, list);
				};
			};
		};
		^ list;
	}

}
