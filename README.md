# Java Utilities for PySpark Kafka Clients (Kerberos)

## Getting Started
```
git clone https://github.com/gerrard-err/kafka-clients-util.git
```

### Prerequisites
Maven 3, Java 8

### Installing
```
mvn install
```

## Running the tests
```
mvn test
```

## Deployment
```
export PYSPARK_SUBMIT_ARGS='--master yarn-client
--jars .ivy2/jars/kafka-clients-util-1.0.jar
--driver-class-path .ivy2/jars/kafka-clients-util-1.0.jar
 pyspark-shell'
 ```

## Example
```python
from pyspark import SparkContext, SparkConf
from py4j.java_gateway import Py4JNetworkError

log4jLogger = sc._jvm.org.apache.log4j 
log = log4jLogger.LogManager.getLogger(__name__) 

def create_kafka_params():
    sc._jvm.java.lang.System.setProperty("java.security.auth.login.config",
                                         "/usr/hdp/current/kafka-broker/conf/kafka_client_jaas.conf")
    kafkaParams = sc._gateway.jvm.java.util.HashMap()
    kafkaParams.put("bootstrap.servers", "localhost:6667");
    kafkaParams.put("security.protocol", "SASL_PLAINTEXT")
    kafkaParams.put("acks", "all");
    kafkaParams.put("retries", 1);
    kafkaParams.put("key.serializer",
    "org.apache.kafka.common.serialization.StringSerializer");
    kafkaParams.put("value.serializer",
    "org.apache.kafka.common.serialization.StringSerializer");
    return kafkaParams

def send(message):
    try:
        log.info("Creating Kafka producer...")
        kafkaClientsHelperClass = sc._jvm.java.lang.Thread.currentThread().getContextClassLoader()\
                .loadClass("com.github.kafka.clients.KafkaClientsPythonHelper")
        kafkaClientsHelper = kafkaClientsHelperClass.newInstance()
        kafkaClientsHelper.createProducer(create_kafka_params())
        log.info("Sending message...")
        kafkaClientsHelper.send("topic_example", sc._gateway.jvm.java.util.UUID.randomUUID().toString(), message)
    except Py4JJavaError as e:
        if 'ClassNotFoundException' in str(e.java_exception):
            log.error(e.java_exception)
        raise e
    except Exception:
        print("Exception")
    finally:
        log.info("Shutting down Kafka producer...")
        kafkaClientsHelper.shutdownProducer()        
```

## Authors
* **Gerrard** - https://github.com/gerrard-err

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
