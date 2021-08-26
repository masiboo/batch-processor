#Batch Processor

This is a Spring boot Java 14 backend service for batch processing. This service starts to process any valid XML file and PDF files provided in the  application's root directory BatchProcessor/data/in

As soon as this app launched, it processed any existing files at `batch-processor/data/in`. After processing all files, it continuously monitors for any input XML and PDF files.

All unit test was done. During the unit test run, it generates directories under `batch-processor/src/test/data`. After the successful test run, it cleans up generated directories.

Simply run `docker-compose up` . In the very first run, it will build docker images. After succuss build, it will execute the application. It will start the application and start monitoring `batch-processor/data/in`. Docker will mount the local data directory to the container. For testing, you can add more input XML and PDF files.  













