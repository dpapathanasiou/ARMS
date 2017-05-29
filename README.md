# <u>A</u>nother <u>R</u>ESTful <u>M</u>ongo <u>S</u>ervice

## About

The basic idea is that since a subset of [HTTP verbs](https://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html) map nicely to [Mongo Database](https://en.wikipedia.org/wiki/MongoDB) [CRUD operations](https://docs.mongodb.com/manual/crud/), it would be useful to provide a [RESTful web service](https://www.ibm.com/developerworks/library/ws-restful/index.html) for read/write access by a variety of clients.

This idea is [not new](https://github.com/search?utf8=%E2%9C%93&q=mongo+restful) but serves as a useful exercise in learning [Kotlin](http://kotlinlang.org/).

## API Usage

The database and collection names are embedded in the first two [path parameters](https://tools.ietf.org/html/rfc3986#section-3.3) and the [query parameters](https://tools.ietf.org/html/rfc3986#section-3.4) correspond to the specific document identifiers.

| HTTP Method | ARMS API  |  MongoDB Action |
| ------------- | ------------- | ------------- |
| GET  | /{database}/{collection}/key<sub>0</sub>=value<sub>0</sub>&key<sub>1</sub>=value<sub>1</sub> ... | [database.collection.find({ key<sub>0</sub> : value<sub>0</sub>, key<sub>1</sub> : value<sub>1</sub>, ... })](https://docs.mongodb.com/manual/reference/method/db.collection.find/) |
| PUT  | /{database}/{collection}/key<sub>0</sub>=value<sub>0</sub>&key<sub>1</sub>=value<sub>1</sub> ... <br /><br /><i>plus a [json](http://json.org/) document in the request [message body](https://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.3)</i>  | [database.collection.replaceOne(<br />{ key<sub>0</sub> : value<sub>0</sub>, key<sub>1</sub> : value<sub>1</sub>, ... },<br />{ json document },<br />upsert: true)](https://docs.mongodb.com/manual/reference/method/db.collection.replaceOne/) |
| DELETE  | /{database}/{collection}/key<sub>0</sub>=value<sub>0</sub>&key<sub>1</sub>=value<sub>1</sub> ...  | [database.collection.deleteMany({ key<sub>0</sub> : value<sub>0</sub>, key<sub>1</sub> : value<sub>1</sub>, ... })](https://docs.mongodb.com/manual/reference/method/db.collection.deleteMany/) |
