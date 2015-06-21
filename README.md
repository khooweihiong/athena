# Athena [![Build Status](https://drone.io/github.com/StarTrackDevKL/athena/status.png)](https://drone.io/github.com/StarTrackDevKL/athena/latest)
A simple library system powered by [JHipster](http://jhipster.github.io/)

## Building the project
Athena uses [Apache Maven](https://maven.apache.org/) and she accepts the following parameters; `db.host`, `db.port`, `db.name`, `db.username`, and `db.password`. Each parameters are self explanatory.

Example of maven command to build her:
```sh
mvn clean package -Ddb.host=localhost -Ddb.port=5432 -Ddb.name=athena -Ddb.username=athena -Ddb.password=2948f5a3b9b3ca2d991c40c8d523bf07
```

To build production, you will have to include `-Pprod`. This will execute the `production` profile. By default it will be using `dev` profile.
