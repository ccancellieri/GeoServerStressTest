GeoServerStressTest
===================

Set of tools to perform stress tests on GeoServer in CI

This project was developed for the data.fao.org UN FAO project to and was designed to be used in CI as Jenkins job.

The project is essentially a maven project wich leverages on a jmeter plugin.

It consists in a JMeter project (which can be customized via JMeter GUI) and parametrized via input parmeters:

Looking at the build.bat you see how this test can be parametrized and launched :

mvn clean package integration-test pre-site -Djmeter.target.host=GEOSERVER_INSTANCE_1.un.fao.org -Djmeter.target.port=8080 -Djmeter.thread.number=10 -Djmeter.loop.count=1

This will build a set of reports which can be shown in jenkins to understand the performance of the target geoserver.

The idea was to generate the report producing a feedback which can be (TODO: automatically) used to increase or reduce the number of the requests or parallel threads (using parameters) determining the maximum amount of requests to maximize the geoserver throughput, those parameters can than be used to configure the geoserver control_flow plugin.

The project contains a VERY simple java class which is used by JMeter to download the layer list from the target geoserver and generate a randomized set of valid parameters to generate WMS requests on the fly using all the existing layers so you will test all the layers actually configured.

This is an alpha version (proof of concept) and any contribution are very welcome.
