#Batch Processor

This is a Spring boot Java 14 backend service for batch processing. This service starts to process any valid XML file and PDF files provided in the  application's root directory BatchProcessor/data/in

As soon as this app launched, it processed any existing files at `BatchProcessor/data/in`. After processing all files, it continuously monitors for any input XML and PDF files.

All unit test was done. During the unit test run, it generates directories under `BatchProcessor/src/test/data`. After the successful test run, it cleans up generated directories. 










