# <u>A</u>nother <u>R</u>ESTful <u>M</u>ongo <u>S</u>ervice

[![Travis CI Status Image](https://travis-ci.org/dpapathanasiou/ARMS.svg?branch=master)](https://travis-ci.org/dpapathanasiou/ARMS)

## About

The basic idea is that since a subset of [HTTP verbs](https://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html) map nicely to [Mongo Database](https://en.wikipedia.org/wiki/MongoDB) [CRUD operations](https://docs.mongodb.com/manual/crud/), it would be useful to provide a [RESTful web service](https://www.ibm.com/developerworks/library/ws-restful/index.html) for read/write access by a variety of clients.

This idea is [not new](https://github.com/search?utf8=%E2%9C%93&q=mongo+restful) but serves as a useful exercise in learning [Kotlin](http://kotlinlang.org/).

## API Usage

The database and collection names are embedded in the first two [path parameters](https://tools.ietf.org/html/rfc3986#section-3.3) and the [query parameters](https://tools.ietf.org/html/rfc3986#section-3.4) correspond to the specific document identifiers.

| HTTP Method | ARMS API  |  MongoDB Action |
| ------------- | ------------- | ------------- |
| GET  | /{database}/{collection}?key<sub>0</sub>=value<sub>0</sub>&key<sub>1</sub>=value<sub>1</sub> ... | [database.collection.find({ key<sub>0</sub> : value<sub>0</sub>, key<sub>1</sub> : value<sub>1</sub>, ... })](https://docs.mongodb.com/manual/reference/method/db.collection.find/) |
| PUT  | /{database}/{collection}<br /><br /><i>plus a [json](http://json.org/) document in the request [message body](https://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.3)</i>  | [database.collection.insertOne({ json document })](https://docs.mongodb.com/manual/reference/method/db.collection.insertOne/) |
| PUT  | /{database}/{collection}?key<sub>0</sub>=value<sub>0</sub>&key<sub>1</sub>=value<sub>1</sub> ... <br /><br /><i>plus a [json](http://json.org/) document in the request [message body](https://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.3)</i>  | [database.collection.replaceOne(<br />{ key<sub>0</sub> : value<sub>0</sub>, key<sub>1</sub> : value<sub>1</sub>, ... },<br />{ json document },<br />upsert: true)](https://docs.mongodb.com/manual/reference/method/db.collection.replaceOne/) |
| DELETE  | /{database}/{collection}?key<sub>0</sub>=value<sub>0</sub>&key<sub>1</sub>=value<sub>1</sub> ...  | [database.collection.deleteMany({ key<sub>0</sub> : value<sub>0</sub>, key<sub>1</sub> : value<sub>1</sub>, ... })](https://docs.mongodb.com/manual/reference/method/db.collection.deleteMany/) |

## Configuration

Edit the [configuration.json](src/main/resources/configuration.json) file to define the service host and port, as well as the [Mongo Connection String URI](https://docs.mongodb.com/manual/reference/connection-string/).

The default values are:

```json
{
    "mongoURI": "mongodb://localhost:27017",
    "serviceURI": "http://localhost:9001/"
}
```

## Roadmap

Features to add, and other things to implement in the future:

- [X] ~~Unit tests and continuous integration~~
- [ ] Deployable jars and instructions
- [ ] Support for more complex queries, using [QueryBuilder](http://api.mongodb.com/java/current/com/mongodb/QueryBuilder.html)
- [ ] Handling mongo connection errors using an appropriate [HTTP status code](https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html) for the API requests (e.g., 410 Gone or 503 Service Unavailable, etc.), along with an alert to the service maintainer
- [ ] Authentication, especially for PUT and DELETE requests
- [ ] Prevent [Jersey](https://jersey.github.io/) from returning 500 when the json document used in the PUT request is invalid (alas, [this solution](https://stackoverflow.com/a/10738086) doesn't seem to work)

Pull requests for both roadmap ideas and implementations are welcome!
