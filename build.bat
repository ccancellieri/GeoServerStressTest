REM mvn clean install eclipse:clean eclipse:eclipse -DdownloadSources=true -DdownloadJavadocs=true -Declipse.addVersionToProjectName=true -o 

mvn clean package integration-test pre-site -Djmeter.target.host=data.review.fao.org/map/gsrv/ -Djmeter.target.port=80 -Djmeter.thread.number=10 -Djmeter.loop.count=1