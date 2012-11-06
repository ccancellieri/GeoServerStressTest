REM mvn clean install eclipse:clean eclipse:eclipse -DdownloadSources=true -DdownloadJavadocs=true -Declipse.addVersionToProjectName=true -o 

mvn clean package integration-test pre-site -Djmeter.target.host=hqltstcdrgeo1.hq.un.fao.org -Djmeter.target.port=8080 -Djmeter.thread.number=10 -Djmeter.loop.count=1