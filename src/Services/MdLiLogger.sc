MdLiLogger : MdLiService {

	var stdout = true;
	var disk = nil;
	var minLevel = 0;

	post {
		arg message, level = 0, type, object;
		if (this.willPost(level)) {
			this.stdout(message, level, type);
			// this.disk(message, level, type);
		};
		^ this;
	}

	stdout {
		arg message, level = 0, type = \status, object;
		message = this.prepend(message, type, object);

		switch (type.asSymbol)
			{\warn} { message.warn }
			{ message.postln };
		^ this;
	}

	disk {
		arg message, level = 0, type = \status, object;

		// @TODO
	}

	prepend {
		arg message, type, error;
		var prepend = switch (type.asSymbol)
			{ \error } { "ERROR: "}
			{ \warn } { ""}
			{ "STATUS: " };
		^ prepend ++ message;
	}

	willPost {
		arg level;
		^ level >= minLevel;
	}
}
