MdLiLogger : MdLiService {

	var stdout = true;
	var disk = nil;
	var minLevel = 0;

	post {
		arg message, level = 0;
		if (level >= minLevel) {
			this.stdout(message, level);
			this.disk(message, level);
		};
	}

	stdout {
		arg message, level = 0;
		if (stdout) {
			message.postln;
		};
	}

	disk {
		arg message, level = 0;
		
		// @TODO
	}
}
