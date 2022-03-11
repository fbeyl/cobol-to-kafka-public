# Apache Kafka example howto integrate z/OS COBOL batch in hybrid application. z/OS COBOL module invokes java classes for sending messages to kafka topic residing in the cloud + receive topic messages using java class running on z/OS aswell as on Linux as on any java supporting platform.

Example code to integrate z/OS COBOL batch job in hybrid setup.

### Prerequisites

To easily test this code you can create a free Apacha Kafka instance at https://www.cloudkarafka.com
To easily perform actions with z/OS install zowe-cli from https://www.zowe.org, vscode + zowe explorer extension.

### Configure

All of the authentication settings for kafka instance can be found in the Details page for your CloudKarafka instance. Use them and z/OS USERID , /home folder to replace in jcl and uss fromkafka, tokafka files + tasks.json if needed.

```
export CLOUDKARAFKA_BROKERS=broker1:9094,broker2:9094,broker3:9094
export CLOUDKARAFKA_USERNAME=<username>
export CLOUDKARAFKA_PASSWORD=<password>
export CLOUDKARAFKA_TOPIC_PREFIX=<topicprefix>
```

## Folder Structure

The workspace contains :

- `src`: folder for java sources
- `target`: folder contains classes + jar result of maven install
- `resources`: folder for z/OS sources (cobol, copybook, jcl, uss settingsfiles, ...) that need to be uploaded + build on z/OS (task 'Deploy to z/OS)


## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
