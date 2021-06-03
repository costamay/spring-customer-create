# t24-create-customer

This project is part of the KBC Group B2C Use Case which leads to their middleware evolution platform using OpenShift Container Platform 4.x (OCP). 

## Clone the Project

On your local, type the following command to clone the project:

```bash
git clone https://KCB-DEV@dev.azure.com/KCB-DEV/KCB%20Tech%20Delivery/_git/create-customer
```

## Deploy the Project in OCP

Change to your cloned project directory to execute next steps using the following instruction:

```bash
cd /path/to/your/cloned/project/directory
```

Login to KCB OpenShift Platform typing the following command:

```bash
oc login --token=<token> --server=https://api.test.aro.kcbgroup.com:6443
```

Create a new project on KCB OpenShift Platform with below command:

```bash
oc new-project kcb-retail-banking-services --description="kcb-retail-banking-services" --display-name="kcb-retail-banking-services"
```

Check the created project on KCB OpenShift Platform executing the next instruction:

```bash
oc get projects
```

Get into the created project on KCB OpenShift Platform using the following command:

```bash
oc project kcb-retail-banking-services
```

Clean, test and package the project using below instruction: 

```bash
For localhost:
mvn clean install spring-boot:run -DskipTests -Dspring-boot.run.profiles=local -DJAEGER_SERVICE_NAME=t24-create-customer
```

Deploy the project directly to KCB OpenShift Platform executing the next instruction:

```bash
mvn fabric8:deploy -Popenshift
```

Finally, after deploying the project, check the new PODs on KCB OpenShift Platform with the next command:

```bash
oc get pods -w
```

#### Redis    
Please refer to the **kcb-common-utils/kcb-connector-redis** project to install Redis pod.


#### ActiveMQ
Please refer to the **kcb-common-utils/kcb-connector-amq** project to install ActiveMQ pods.

#### Related Projects
Please consider installing the following projects to complete the flow:

- kcb-common-utils
- kcb-connector-vault
- mock-t24-create-customer

## About the Project
This projects was developed using 7.4.0.fuse-sb2-740019-redhat-00005 BOM. See [Red Hat Maven Repository](https://maven.repository.redhat.com/ga/org/jboss/redhat-fuse/fuse-springboot-bom/7.4.0.fuse-sb2-740019-redhat-00005/fuse-springboot-bom-7.4.0.fuse-sb2-740019-redhat-00005.pom)

