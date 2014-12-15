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
* call: `java -jar .\crawlingsim-1.jar SEED_FILE WEB_GRAPH QUALITY_MAPPING MAX_STEPS TAKES_PER_STEP PAGE_LVL_STRAT SITE_LVL_STRAT BATCH_SIZE STEP_QUALITY`
* if you only want to read the WebGraph and QualityMaping you can also write: `java -jar .\crawlingsim-1.jar WEB_GRAPH QUALITY_MAPPING`
* if you only want to run the crawling without reading the WebGraph and QualityMaping you can also write: `java -jar .\crawlingsim-1.jar SEED_FILE MAX_STEPS TAKES_PER_STEP PAGE_LVL_STRAT SITE_LVL_STRAT BATCH_SIZE STEP_QUALITY`
* optional with more memory: `java -Xms2048m -Xmx2048m -jar .\crawlingsim-1.jar...`

| Parameter       |   Type        | Description |
| ----------------|---------------| -------------|
| `SEED_FILE`       | Path to File  | the file containing the seed urls |
| `WEB_GRAPH`       | Path to File  | the file containing the webgraph |
| `QUALITY_MAPPING` | Path to File  | the file containing the url to quality mapping|
| `MAX_STEPS`       | integer       | limits the runtime, determines how much steps would be teken, if `MAX_STEPS` = -1 the simulator runs until all urls are downloadet |
| `TAKES_PER_STEP`  | integer       | determines how much urls would be downloaded by each step |
| `STEP_QUALITY`    | Path to File  | path to the output file |
| `PAGE_LVL_STRAT`    | string (BacklinkCount | OPIC)  | which Page-Level-Strategy should be used. If chosen is unknown BacklinkCount will be used |
| `SITE_LVL_STRAT`    | string (MaxPagePriority | RoundRobin)  | which Site-Level-Strategy should be used. If chosen is unknown MaxPagePriority will be used |
| `BATCH_SIZE`    | integer | new ranks would be determined by the chosen strategies after each `BATCH_SIZE` steps |

###Example 1
`java -jar -Xmx4096m -Xms4096m .\crawlingsim-1.jar ..\test_resources\prod\seeds.txt ..\test_resources\prod\linkgraph_ids.txt ..\test_resources\prod\quality_mapping.txt 5000 200 plstrat slvlstrat 100 ..\test_resources\prod\backlinkcounter_maxpagerank_100.txt`

reads WebGraph and QualityInfo and crawls with the default strategies with a batch size of 100

###Example 2
`java -jar -Xmx4096m -Xms4096m .\crawlingsim-1.jar ..\test_resources\prod\seeds.txt 5000 200 plstrat slvlstrat 500 ..\test_resources\prod\backlinkcounter_maxpagerank_500.txt`

crawls with the default strategies with a batch size of 500 without reading the WebgGraph or QualityInfo 
