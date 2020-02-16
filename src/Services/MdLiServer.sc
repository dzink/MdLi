MdLiServer : MdLiService {
	classvar configPath = "server.yaml";
	var server;
	var options;

	server {
		if (server.isNil) {
			server = Server.default;
		};
		^ server;
	}



}
