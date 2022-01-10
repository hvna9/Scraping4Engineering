# Scraping4Engineering: WEB SCRAPING AUTOMATION
A Web Scraping application to retrieve and store data from Web sorurces. The project was developed in Java, using Spring Boot and JSoup as main frameworks: the application expose and uses REST APIs to make operations with its microservices. The data retrieved are stored into a NoSql database; in particular the used db is MongoDB.

## Why this project?
The project with codename "Scraping4Engineering" is a work born to carry on the curricular internship for the University of Salerno (www.unisa.it). This Training is the result of a collaboration beetween the University and Engineering Ingegneria Informatica S.p.a. (www.eng.it), a Company that operates in the IT sector.
The project plans to get some data from Web pages and store them into MedITech's catalogues. Since the MedITech's data saving systems are not known, the best method to product something good, was to use the REST APIs, in order to expose data as JSON object: in this way, the developers didn't care how the data will be stored, but only the possibility to get and provide them. 

### Other informations
A project developed by Claudio S. Di Mauro & Pierpaolo Venanzio.

### Docker image
Download the Docker image at https://hub.docker.com/r/havana9/scrape4engineering

### How to start
You can pull the last image using:
```console
docker pull havana9/scrape4engineering:v0.0.4
```
This image need a connection to MongoDB, so in order to get a good configuration of the project, the best way to do this is to use the docker-compose.yml:
```yml
version: "3.8"
services:
    newMongoDB:
        image: mongo:latest
        container_name: newMongoDB
        ports:
            - 27017:27017
    scrape4engineering:
        image: havana9/scrape4engineering:v0.0.4
        container_name: scrape4engineering
        ports:
            - 8080:8080
        links:
            - newMongoDB
```
When you will run the docker-compose with the command
```console
docker-compose up
```
the Docker Engine will start the downloading of both MongoDB and Scrape4Engineering, exposes the specified ports and links the containers in order to work well.  
You can use the REST API's with Postman or Swagger. In particular, if you want to use Swagger, you can connect with your browser at http://localhost:8080/swagger-ui.html.

### How to use
The software is based on JSoup so to scrape a web page you have to know it's DOM structure to get the html tags that are mandatory to scrape an element in the page.  
Using the exposed API's you can create a pattern. A pattern is a JSON document, stored into a MongoDB collection (called *patterns*), that contains the tag for each section the user wants to retrieve in the page.  
After the creation of a pattern, the real scraping can starts. If the pattern is a valid one, the scraping will retrive a set of entity that represents the real information the user need. This information will stored in a MongoDB collection called *entities*.  
If the page you're scraping contains an attachment (such as a pdf, an image, etc.), the scraping process will store it on MongoDB using GridFS.
