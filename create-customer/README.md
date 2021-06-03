# Create Customer Adapter

This project is part of the KBC Group B2C Use Case which leads to their middleware evolution platform using OpenShift Container Platform (OCP). All this microservices will serve as a direct interface between business clients and orchestration microservices layer. 

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

(Optional) Create a new project on KCB OpenShift Platform with below command:

```bash
oc new-project kcb-retail-banking-services --description="kcb-retail-banking-services" --display-name="kcb-retail-banking-services"
```

(Optional) Check the created project on KCB OpenShift Platform executing the next instruction:

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
mvn clean install spring-boot:run -DskipTests -Dspring-boot.run.profiles=local -DJAEGER_SERVICE_NAME=create-customer
```

Deploy the project directly to KCB OpenShift Platform executing the next instruction:

```bash
mvn fabric8:deploy -Popenshift -DskipTests
```

Finally, after deploying the project, check the new PODs on KCB OpenShift Platform with the next command:

```bash
oc get pods -w
```

This project will create automatically a new **Route** that exposes services to the internet with the following URI:

```bash
http://create-customer-kcb-financial-inquiries.apps.test.aro.kcbgroup.com/
```

## Test the Project

First, check the status of the project:

```bash
curl --location --request GET 'http://create-customer-kcb-financial-inquiries.apps.test.aro.kcbgroup.com/health'
```

Then, send a POST message to the API endpoint:

```bash

curl --location --request POST 'http://localhost:8081/api/createCustomer' \
--header 'messageID: 123456789' \
--header 'channelCode: 101' \
--header 'Authorization: Basic YXRtLTEwMTp0ZXN0QDEyMw==' \
--header 'Content-Type: application/json' \
--data-raw '{
    "header": {
        "messageID": "123456789",
        "featureCode": "401",
        "featureName": "RetailBankingServices",
        "serviceCode": "4001",
        "serviceName": "CreateCustomer",
        "serviceSubCategory": "CUSTOMER",
        "minorServiceVersion": "1.0",
        "channelCode": "101",
        "channelName": "atm",
        "routeCode": "001",
        "timeStamp": "22222",
        "serviceMode": "sync",
        "subscribeEvents": "1",
        "callBackURL": ""
    },
    "requestPayload": {
        "transactionInfo": {
            "companyCode": "KE0010001",
            "mnemonic": "33333343",
            "firstName": "Sridhara",
            "middleName": "Vinayakrao",
            "lastName": "Shastry",
            "street": "Koloboto Road",
            "town": "Ngara",
            "sectorCode": "36000",
            "branchCode": "140",
            "industryCode": "4",
            "targetCode": "0306",
            "nationality": "Kenyan",
            "customerStatus": "active",
            "residence": "active",
            "documentDetails": [
                {
                    "documentNumber": "1234564",
                    "documentType": "ALIENID",
                    "documentHolderName": "Sridhara",
                    "issuingAuthority": "PP Offr Nairobi",
                    "issueDate": "20200101",
                    "expirtyDate": "20220101"
                },
                {
                    "documentNumber": "1234564",
                    "documentType": "ALIENID",
                    "documentHolderName": "Sridhara",
                    "issuingAuthority": "PP Offr Nairobi",
                    "issueDate": "20200101",
                    "expirtyDate": "20220101"
                }
            ],
            "contactDetails": [
                {
                    "mobileNumber": "792483394",
                    "emailAddress": "sridharavshastry@gmail.com"
                },
                {
                    "mobileNumber": "792483394",
                    "emailAddress": "sridharavshastry@gmail.com"
                }
            ],
            "notificationLanguage": "English",
            "kcbSector": "2222",
            "customerSegmentCode": "8100"
        }
    }
}
'
```

## Dependencies
#### Redis    
Please refer to the **kcb-common-utils/kcb-connector-redis** project to install Redis pod.


#### ActiveMQ
Please refer to the **kcb-common-utils/kcb-connector-amq** project to install ActiveMQ pods.

#### Related Projects
Please consider installing the following projects to complete the flow:

- kcb-common-utils
- kcb-connector-vault
- mock-t24-create-customer
- t24-create-customer

## About the Project
If you change the name of the project, namespaces and some data listed in this file will also change. This projects was developed using 7.4.0.fuse-sb2-740019-redhat-00005 BOM. See [Red Hat Maven Repository](https://maven.repository.redhat.com/ga/org/jboss/redhat-fuse/fuse-springboot-bom/7.4.0.fuse-sb2-740019-redhat-00005/fuse-springboot-bom-7.4.0.fuse-sb2-740019-redhat-00005.pom)

