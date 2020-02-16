MdLiErrorHandler : MdLiService {

	var <> verbosity = 100;
	var <> rethrow = true;

	throw {
		arg error;
		verbosity.postln;
		if (verbosity > 25) {this.logger.post('error');};
		if (verbosity > 50) {this.logger.post('error.protectedBacktrace');};
		if (rethrow) { error.throw };
	}
}
