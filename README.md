CA_Praktikum
============

This project was created by Daniel Michalke and Ingo Rößner as part of  "Computational Advertising Praktikum" at the University of Leipzig.

It is planed as a simulator of crawling strategies.


##Build

* install maven (with mvn in PATH)
* go to project directory (with pom.xml)
* build with `mvn package`

##Run
* go to the subdirectory 'traget'
* call: `java -jar .\crawlingsim-1.jar SEED_FILE WEB_GRAPH QUALITY_MAPPING MAX_STEPS TAKES_PER_STEP STEP_QUALITY`
* optional with more memory: `java -Xms2048m -Xmx2048m -jar .\crawlingsim-1.jar...`

| Parameter       |   Type        | Description |
| ----------------|---------------| -------------|
| SEED_FILE       | Path to File  | the file containing the seed urls |
| WEB_GRAPH       | Path to File  | the file containing the webgraph |
| QUALITY_MAPPING | Path to File  | the file containing the url to quality mapping|
| MAX_STEPS       | integer       | limits the runtime, determines how much steps would be teken, if MAX_STEPS = -1 the simulator runs until all urls are downloadet |
| TAKES_PER_STEP  | integer       | determines how much urls would be downloaded by each step |
| STEP_QUALITY    | Path to File  | path to the output file |

###Example 1
`java -jar .\crawlingsim-1.jar ..\test_resources\seed_small_1.txt ..\test_resources\webgraph_small.txt ..\test_resources\quality_small.txt -1 1 ..\test_resources\out.txt`

runs until all files are downloaded, the output file will have the same count of lines as accessable urls exist

###Example 2
`java -jar .\crawlingsim-1.jar ..\test_resources\seed_small_1.txt ..\test_resources\webgraph_small.txt ..\test_resources\quality_small.txt 1 1 ..\test_resources\out.txt`

runs exactly one step with one download, the output file will have exactly one line
 
###Example 3
`java -jar .\crawlingsim-1.jar ..\test_resources\seed_small_1.txt ..\test_resources\webgraph_small.txt ..\test_resources\quality_small.txt 1 2 ..\test_resources\out.txt`

runs exactly one step with two downloads (if possible), the output file will have exactly one line

###Example 4
`java -jar .\crawlingsim-1.jar ..\test_resources\seed_small_1.txt ..\test_resources\webgraph_small.txt ..\test_resources\quality_small.txt 2 2 ..\test_resources\out.txt`

runs two steps (if possible) with two downloads (if possible) for each step, the output file will have two line

##Project Structure

###Tests
Resources: ./test_resources
UnitTests: uni.ca.crawlingsim.tests

###Classes
uni.ca.crawlingsim.webgraph.WebGraph.java :
parses and contains web graph structure

uni.ca.crawlingsim.quality.QualityInfo.java :
parses and contains quality informations

uni.ca.crawlingsim.crawler.Crawler.java :
creates WebGraph and QualityInfo, crawls and prints results through StepQualityOut

uni.ca.crawlingsim.crawler.StepQualityOut.java
prints the results into file