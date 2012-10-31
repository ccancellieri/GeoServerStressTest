REM mvn clean install eclipse:clean eclipse:eclipse -DdownloadSources=true -DdownloadJavadocs=true -Declipse.addVersionToProjectName=true -o 

mvn clean package integration-test pre-site -DargLine="-Djmeter.target.host=hqltstcdrgeo1.hq.un.fao.org"