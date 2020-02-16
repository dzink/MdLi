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




}
