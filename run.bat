set M2_REPO=C:\Users\Cancellieri\.m2\repository\
java -Dfile.encoding=UTF-8 -classpath "%CLASSPATH%";.\target\classes\*;.\target\*;%M2_REPO%\commons-io\commons-io\2.1\commons-io-2.1.jar;%M2_REPO%\org\freemarker\freemarker\2.3.18\freemarker-2.3.18.jar;%M2_REPO%\org\apache\commons\commons-lang3\3.1\commons-lang3-3.1.jar; it.geosolutions.geoserver.StressTest %1 %2 %3 %4 %5
