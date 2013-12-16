export MAVEN_OPTS="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=4000,server=y,suspend=n"
cd ../osoitepalvelu-ui && mvn3 install \
	&& cd ../osoitekoostepalvelu \
	&& mvn clean -Djetty.port=9090 jetty:run -Pstartstack
