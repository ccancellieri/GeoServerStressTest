set M2_REPO=C:\Users\Cancellieri\.m2\repository\
java -Dfile.encoding=UTF-8 -classpath "%CLASSPATH%";.\target\classes\*;.\target\*;%M2_REPO%\org\freemarker\freemarker\2.3.18\freemarker-2.3.18.jar it.geosolutions.geoserver.StressTest %1 %2 %3 %4
