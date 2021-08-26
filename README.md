/*
* Copyright (c) 2021. batch-processor is free software; you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either
* version 2 of the License, or batch-processor(at your option) any later version.
*
* batch-processor is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License along with batch-processor; if not, write to the Free Software
* Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

#Batch Processor

This is a Spring boot Java 14 backend service for batch processing. This service starts to process any valid XML file and PDF files provided in the  application's root directory `batch-processor/data/in`

As soon as the app launches, it processes any existing files at `batch-processor/data/in`. After processing all files, it continuously monitors for any input XML and PDF files.

Production code is fully unit tested. During the test execution, it generates directories under `batch-processor/src/test/data`. After the successful test run, it cleans up generated contents and gets ready for consecutive test execution.

In order to test, launch the service `docker-compose up` command. Prerequisite testing: docker, docker-compose shall be installed on the host.  In the very first run, it will build docker images. After the success build, it will launch the service and start monitoring `batch-processor/data/in` directory. Docker will mount the local `data` directory to the docker container. For testing, you can add more input XML and PDF files.  













