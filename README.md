# kafka-clients-util
Java Utilities for PySpark Kafka Clients (Kerberos)

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
```
from pyspark import SparkContext, SparkConf
from py4j.java_gateway import Py4JNetworkError
from pyspark.sql import SQLContext 

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
        kafkaClientsHelper = sc._gateway.jvm.com.github.kafka.clients.KafkaClientsPythonHelper()
        kafkaClientsHelper.createProducer(create_kafka_params())
        kafkaClientsHelper.send("topic_example", sc._gateway.jvm.java.util.UUID.randomUUID().toString(), message)
    except Py4JNetworkError:
        print("Py4JNetworkError")
    except Exception:
        print("Exception")
    finally:
        kafkaClientsHelper.shutdownProducer()
        
```

## Authors
* **Gerrard** - https://github.com/gerrard-err

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
