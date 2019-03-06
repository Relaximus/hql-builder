# Setup your development environment

This will guide you for a setup on your local machine. Please keep in mind that this maybe not applicable for the production environment.
Please feel free to contribute your knowledge and add new information yourself!

## General information

### Dependencies

Current framework usage:
   1. [Java 8](http://docs.oracle.com/javase/8/docs/)
   2. [Grails 3.2.11](http://docs.grails.org/latest/guide/single.html)

## Development environment
The following lines will guide you through a setup on Mac OS X with a given [homebrew](http://brew.sh) installation for your development with IntelliJ.

## Before start
 * Run `npm install` from `src/main/webapp/app` folder

### Quick start
 * `$ ./gradlew bootRun`
 * if you need hot swap for webpack bundle run in a separate terminal `./gradlew bundleDev`. Don't forget to stop it as this is a daemon task.
 
 ### Generate War
  * `$ ./gradlew war`

### Database setup for regular development
[Ebedded Postgress](https://github.com/opentable/otj-pg-embedded) is used in usual dev process.
If application was stopped in force mode (kill -9) the postgres process stays alive. It keeps serving
all consecutive runs, but stays in the list processes.

If you need to kill you can use following commands:
To find PID (56566 - port for example)
>lsof -n -i4TCP:56566 

> kill < found PID >

#### Configuration of posgresql for development
Be aware that you have to lookup placholder information in grails-app/conf/application.yml, section environments.development.dataSource:

 * `$ createdb 'avax-portal'`
 * `$ psql 'avax-portal'`
 
 `postgres` user with password `postgres` used.
 
 For custom user:
 * `PSQL$ CREATE USER "$user" with password '$password';`
 * `PSQL$ GRANT ALL ON ALL TABLES IN SCHEMA "public" TO "$user";`
 * `PSQL$ CREATE EXTENSION IF NOT EXISTS citext WITH SCHEMA public;`

#### Test data
Project is using [fixtures plugin](http://www.grails.org/plugin/fixtures). It will automatically provide some essential data to start with.

#### Run tests

#### Functional tests
>./gradlew integrationTest

Installed Browser used during tests. FireFox 47.0.1 mast be used by default.
For starting with any version of Chrome use:

>./gradlew -Pgeb.env=chrome integrationTest

In Idea add -Pgeb.env=chrome  in *Script parameters*

You can run your Functional tests on running instance of grails
 - comment @Integration annotation to avoid starting new instance of gradle (don't forget uncomment before commit)
 - create Gradle Run/Debug Configuration :
    - Task : 
    `integrationTest --tests *OffersSpec`
    - Arguments : 
    `-Dgeb.env=chrome  -Dgeb.build.baseUrl="http://localhost:8080/"`

Result of test placed in avax-portal/build/test-reports/com/avax/functional/specs

### Integration tests 
By default, starts with Functional test, for separate run:

>./gradlew integrationTest --tests com.avax.integration.*

#### Unit test

>./gradlew test --stacktrace

## Deployment

### Increase versioning
change *version* in gradle.properties

### Prepare database migration

##### How to use:
If you've changed any database tables (e.g. by editing domain objects), please be aware that we use [database migration plugin](http://plugins.grails.org/plugin/grails/database-migration/) in our development and deplyment process.
Said that you have to:
0. create DB
 * `$ createdb 'prod-migration'`
 * `$ psql 'prod-migration'`
 
`postgres` user with password `postgres` will be used used.

1. `-Dgrails.env=migrationSchema dbm-update` for apply existing patches
2. `-Dgrails.env=migrationSchema dbm-gorm-diff 999-temp.groovy -add` for making patch
3. `-Dgrails.env=migrationSchema dbm-update` again for testing newly created patch

####Grails commands
create patch:
>-Dgrails.env=migrationSchema dbm-gorm-diff 999-temp.groovy -add

sync change log:
>-Dgrails.env=migrationSchema dbm-changelog-sync

Apply all patches:
>-Dgrails.env=migrationSchema dbm-update


# Test portal
test.avax-portal.de

Client:	211@mail.ru / Clientexample13!
Agency: sdcf@mail.ru / Clientexample13!	

demo.avax-portal.de -- used during sales. Don't  modify data
clone.avax-portal.de -- copy of production data

## Start dev with Test DB
>-Dgrails.env=test_db 


## Coding convention
* Use GORM dynamic finders if possible. Otherwise HQL. In the worst case criteria
* Avoid DB call from views, use controllers or tags
* Place security check on controller level. Use UserService.is*Accessible methods
* Set right margin columns option to 180 in code style Idea settings

## Copyright
```
/**
 ** AVAX GmbH CONFIDENTIAL CONTROLLED.  DO NOT COPY OR DISTRIBUTE FURTHER.
 ** © 2017 AVAX GmbH, Inc.  All rights reserved.
 **/
```
Guide for Intellij IDEA:
* Create copyright profile
 1. File -> Settings -> Editor -> Copyright -> Copyright profiles
 2. Create new profile with given text, check with "Validate" button

* Create copyright scope (classes which copyright should be applied to)
 1. File -> Settings -> Appearence & Behaviour -> Scopes
 2. Create new scope
 3. Select only necessary folders in project structure and exclude others

* Update copyright in given scope
 1. Code -> Update copyright
 2. Select custom scope
 3. Select checkbox "Update existing copyrights"
 4. Done

boom

