MdLiErrorHandler : MdLiService {

	var <> errorVerbosity = 100;
	var <> backtraceVerbosity = 50;
	var <> warningVerbosity = 25;
	var <> rethrowDefault = true;

	throw {
		arg error, rethrow = nil;
		this.post(error, errorVerbosity, \error, error);
		this.post(error.protectedBacktrace, backtraceVerbosity, \error, error);
		if (rethrow ?? { rethrowDefault } ) {
			error.throw();
		};
	}

	warn {
		arg message, error;
		this.post(message, warningVerbosity, type: \warn, object: error);
		^ this;
	}
}
