export MAVEN_OPTS="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=4200,server=y,suspend=n -Xmx512m -XX:MaxPermSize=128m"
cd ../osoitepalvelu-ui && mvn3 install \
	&& cd ../osoitekoostepalvelu \
	&& mvn3 clean -Djetty.port=9099 jetty:run -Dfile.encoding=UTF8 # -Pstartstack -Dfile.encoding=UTF8
