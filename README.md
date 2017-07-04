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

After cloning this repo, create one or more environment configuration folders under <tt>src/main/configurations</tt>. A typical deployment will have <tt>dev</tt>, <tt>qa</tt>, and <tt>prod</tt>, like this:

```sh
mkdir -p src/main/configurations/{dev,qa,prod}
```

The [configuration.json](src/main/resources/configuration.json) file defines the service host and port, as well as the [Mongo Connection String URI](https://docs.mongodb.com/manual/reference/connection-string/), as well as authentication options per client, per HTTP action.

The default values are:

```json
{
    "authSeeds": {},
    "authenticate": false,
    "mongoURI": "mongodb://localhost:27017",
    "serviceURI": "http://localhost:9001/"
}
```

Copy <tt>configuration.json</tt> into each of the <tt>src/main/configurations</tt> environment folders, and make the appropriate edits for each environment. 

Everything under <tt>src/main/configurations</tt> is excluded from source control.

### Client Request Authentication (optional)

When <tt>"authenticate"</tt> in the <tt>configuration.json</tt> file is set to <tt>true</tt>, each client request must include these two headers, otherwise the server will reply with [403 Forbidden](https://en.wikipedia.org/wiki/HTTP_403):

* API-KEY &mdash; a unique client identifier string, defined as a key in the <tt>"authSeeds"</tt> hash
* API-TOTP &mdash; a Time-Based One-Time Password (TOTP) string, generated as defined in [RFC 6238](https://tools.ietf.org/html/rfc6238), using the seed value in the <tt>"authSeeds"</tt> hash for the corresponding client identifier

Each API client needs to know its seed value and keep it secret, so that it can produce the TOTP within 30 seconds of each request it makes to the server.

Permissions are granular, per each HTTP action (GET, PUT, DELETE), and an API client have have any combination of allowed and disallowed requests.

The [test configuration](src/test/resources/configuration.json) file has an example for two API clients, the first of which has read-only access (i.e., find using GET, but not PUT nor DELETE), and the second of which has all permissions.

## Building &amp; Deploying

After [installing Kotlin](https://kotlinlang.org/docs/tutorials/getting-started.html) and defining your environment-specific setup in <tt>configuration.json</tt>, use the [gradle distTar or distZip task](https://docs.gradle.org/current/userguide/distribution_plugin.html#sec:distribution_tasks) along with the desired environment.

For example, to build a zip file using the configuration defined in <tt>src/main/configurations/qa</tt>, run this command:

```sh 
./gradlew distZip -Penv=qa
```

If the <tt>-Penv=qa</tt> option is missing, the default <tt>configuration.json</tt> file from <tt>src/main/resources</tt> will be used instead.

The resulting zip file, <tt>ARMS-0.0.1.zip</tt> will be created in the <tt>build/distributions</tt> folder.

Copy or move it to the appropriate place on your server.

Unzip it, and run it using either one of these launcher scripts:

* <tt>ARMS-0.0.1/bin/ARMS</tt>
* <tt>ARMS-0.0.1/bin/ARMS.bat</tt>

## Roadmap

Features to add, and other things to implement in the future:

- [X] ~~Unit tests and continuous integration~~
- [X] ~~Deployable jars and instructions~~
- [ ] Support for more complex queries, using [QueryBuilder](http://api.mongodb.com/java/current/com/mongodb/QueryBuilder.html)
- [ ] Handling mongo connection errors using an appropriate [HTTP status code](https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html) for the API requests (e.g., 410 Gone or 503 Service Unavailable, etc.), along with an alert to the service maintainer
- [X] ~~Authentication, especially for PUT and DELETE requests~~
- [ ] Prevent [Jersey](https://jersey.github.io/) from returning 500 when the json document used in the PUT request is invalid (alas, [this solution](https://stackoverflow.com/a/10738086) doesn't seem to work)

Pull requests for both roadmap ideas and implementations are welcome!
