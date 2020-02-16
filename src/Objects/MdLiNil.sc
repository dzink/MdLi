MdLiNil {

	exists {
		^ false;
	}
	empty {
		^ true;
	}

	? {
		arg ...args;
		^ args[0];
	}

	?? {
		arg object;
		^ object.value();
	}

	asString {
		^ "\\nil";
	}

}

+ Object {
	exists {
		^ true;
	}

	empty {
		^ false;
	}
}

+ Nil {
	exists {
		^ false;
	}

	empty {
		^ true;
	}
}
