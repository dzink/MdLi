/**
 * A class to use the error handler for strictness errors.
 */
MdLiStrictException : MethodError {
	classvar <> strictness = 0;
	var <> level;

	*new {
		arg what, receiver, level = 0;
		var e = super.new(what, receiver);
		e.level = level;
		^ e;
	}

	throw {
		if (level >= strictness) {
			super.throw();
			thisThread.handleError(this);
		} {
			if (receiver.respondsTo(\logger)) {
				receiver.logger.post(what, level, \warn, this);
			};
		};
		^ this;
	}

}
