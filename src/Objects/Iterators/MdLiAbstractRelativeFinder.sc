MdLiAbstractRelativeFinder {
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
	 * Finds all relatives.
	 */
	all {
		arg levels = inf;
		^ this.prRelatives(object, levels, select: true, list: List[]);
	}

	/**
	 * Selects relatives. If a child does not match the criteria, do not look
	 * at its children.
	 */
	selectBlocking {
		arg select, continue, levels = inf;

		// If continue is empty, use this function which will just use selected.
		continue = continue ?? {{
			arg child, selected;
			selected;
		}};
		^ this.prRelatives(object, levels, select: select, continue: continue, list: List[]);
	}

	/**
	 * Gets all relatives, and then selects from them.
	 */
	selectFromAll {
		arg filter, levels = inf;
		var d = this.prRelatives(object, levels, list: List[]);
		^ d.select(filter);
	}

	selectFirst {
		arg select, levels = inf;
	}

	/**
	 * This should not be called.
	 */
	prRelatives {
		MdLiStrictException.throw("Should not call prRelatives directly on MdLiAbstractRelativeFinder.", 50);
		^ List[];
	}
}
