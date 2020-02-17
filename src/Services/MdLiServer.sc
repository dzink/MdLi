MdLiServer : MdLiService {
	classvar configPath = "server.yaml";
	var server;
	var options;

	server {
		if (server.empty()) {
			server = Server.default;
		};
		^ server;
	}

	setServer {
		arg a_server;
		if (a_server.isKindOf(Server)) {
			server = a_server;
			options = server.options;
		} {
			this.errorHandler.throw("Tried to add a server that is not a kind of Server.");
		};
		^ this;
	}

	getOptionsFromConfig {
		if (this.config.hasKey(\serverOptions)) {
			var o = this.options();
			this.config.serverOptions.keysValuesDo {
				arg param, value;
				var method = (param.asString ++ '_').asSymbol;
				if (o.hasMethod(method)) {
					o.perform(method, value);
				} {
					this.errorHandler.warn("Server doesn't have a method for " ++ method);
				};
			};
		}
	}

	setOption {
		arg param, value;
		var method = (param.asString ++ '_').asSymbol;
		var o = this.options();
		if (o.hasMethod(method)) {
			o.perform(method, value);
		} {
			this.errorHandler.warn("Server doesn't have a method for " ++ method);
		};
		^ this;
	}

	options {
		if (options.empty()) {
			options = this.server().options();
		};
		^ options;
	}




}
