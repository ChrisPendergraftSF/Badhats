# BADHATS
## _THE REST API TO FIND A BAD IP, AND THE SOURCE IT CAME FROM_

[![N|Solid](https://cldup.com/dTxpPi9lDf.thumb.png)](https://nodesource.com/products/nsolid)

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

This is a MAVEN Java/Spring H2 REST imploying HAL

- Searchable Interface
- Supports over 6 million Changing Addresses daily
- Can be deployed via MAVEN/EB to AWS with a node of XML in the POM.xml
## Features

- Imports over 6 million addresses in less than 4 minutes
- Search by ip and find all the list sources who supplied the ID
- Gauge what lists to employ at your firewall.
- Designed and tested on Elastic Beanstalk
- Internal h2 DB makes for fast query, and queries can continue durring ingest.
- Because of Spring Datasource, DB can be decoupled and pointed to any JPA compliant connector.
- Drops tables on startup, if needed,  and snatches GitHub Repo for Firehol list once each startup, and that could be tweaked via Commnand line runner or chron.
- Can ignore injest and be pointed to external h2 instance, and limit downtime.

Just a few days effort because of Spring. Love Spring.



BADHATS uses a number of open source projects to work properly:

- [MAVEN] - Java container made simple!
- [SPRING] - awesome DI and WEB FRAMEWORK FOR J8 and above.
- [H2] - Native JAVA SQL DB, writing to a local file in the jar, not mem, so if needed, persistance would be maitained.
- [HAL] - Hypertext Application Language, standard.
- [JGit] - Wonderful JAVA lib for using the github API.




## Installation
CLONE REPO and RUN CLEAN in the maven scripts panel of INteliJ or your fav IDE for java.

RUN THE MAIN APP - It will take 4 minutes to get all 6 million IPs in the DB, from 45 seconds in your can explore the first 500K addresses.
goto http://localhost:8080/ to get a HAL breakdown of DOs
## API
{
"_links" : {
"sources" : {
"href" : "http://localhost:8080/sources{?page,size,sort}",
"templated" : true
},
"badips" : {
"href" : "http://localhost:8080/badips{?page,size,sort}",
"templated" : true
        },
        "profile" : {
        "href" : "http://localhost:8080/profile"
        }
    }
}
# BADDIES!!!!
Both call will return a JSON with a list of sources. IPS have A LOT of list overlap and this can be used to create understanding of sourcing.
- [findByIP] - http://localhost:8080/badips/search/findByIp?ip=XXXXXXXX
- [findByID] - http://localhost:8080/badips/search/findByID?ip=XXXXXXXX

# SOURCES!!!!
This call will return a JSON with a list source. Sources sometimes maintian multiple lists, based on caterygory (malware, uncontrolled...etc).
- [findByListName] - http://localhost:8080/sources/search/findByListName?listname=XXXXXXXX
