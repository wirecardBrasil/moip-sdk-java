<img src="https://gist.githubusercontent.com/joaolucasl/00f53024cecf16410d5c3212aae92c17/raw/1789a2131ee389aeb44e3a9d5333f59cfeebc089/moip-icon.png" align="right" />

# Moip Java SDK

[![CircleCI](https://circleci.com/gh/moip/moip-sdk-java/tree/master.svg?style=svg)](https://circleci.com/gh/moip/moip-sdk-java/tree/master)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/59c15b9d4e35440c8e1d2810c0509836)](https://www.codacy.com/app/rodrigo-saito/moip-sdk-java)
[![Software License](https://img.shields.io/badge/license-MIT-brightgreen.svg)](https://github.com/moip/moip-sdk-java/blob/master/LICENSE)
[![Slack](https://slackin-cqtchmfquq.now.sh/badge.svg)](https://slackin-cqtchmfquq.now.sh/)

<details>
    <summary>Index</summary>

* [Simple flow](#simple-flow)
  * [Setup](#setup)
    * [Authentication](#authentication)
    * [Environment](#environment)
    * [Finally](#finally)
  * [Create customer](#create-customer)
  * [Create order](#create-order)
  * [Create Payment](#create-payment)
  * [Other examples](#other-examples)
* [Exceptions treatment](#exceptions-treatment)
* [Moip documentation](#moip-documentation)
* [Getting help](#getting-help)
* [Contributing](#contributing)

</details>

## Require

Java `v1.8ˆ` ![java-cup](https://user-images.githubusercontent.com/32847427/37723265-b8441610-2d0c-11e8-8238-ab27df829a13.png)

## Installing

### Gradle

![Maven-central](https://img.shields.io/maven-central/v/br.com.moip/sdk-java.svg)

Add the fallowing dependency to `build.gradle` in the project:

```gradle
// https://mvnrepository.com/artifact/br.com.moip/sdk-java
compile group: 'br.com.moip', name: 'sdk-java', version: 'x.y.z'
```

### Maven

![Maven-central](https://img.shields.io/maven-central/v/br.com.moip/sdk-java.svg)

Add the fallowing dependency to `pom.xml` in the project:

```xml
<!-- https://mvnrepository.com/artifact/br.com.moip/sdk-java -->
<dependency>
    <groupId>br.com.moip</groupId>
    <artifactId>sdk-java</artifactId>
    <version>x.y.z</version>
</dependency>
```

### Another installation methods
https://mvnrepository.com/artifact/br.com.moip/sdk-java

## Simple flow

This step by step will exemplify the integration flow with simple usage examples.

### 1. Setup
Before making requests to Moip API its necessary create the **Setup** object, defining the environment, the connect timeout, the read timeout and the authentication that will be used.

#### 1.1 Authentication
There are two ways to authenticate the request, some endpoints require a "highest authorization level", it will depend on the endpoint and type of request.
```java
import br.com.moip.models.Setup;

Setup setup = new Setup().setAuthentication(auth).setEnvironment(ENVIRONMENT);
```

#### By BasicAuth
The following set will generate a hash `Base64` with your Moip account **token** and **key** to authenticate.
```java
import br.com.moip.auth.Authentication;
import br.com.moip.auth.BasicAuth;

String token = "01010101010101010101010101010101";
String key = "ABABABABABABABABABABABABABABABABABABABAB";

Authentication auth = new BasicAuth(token, key);
```

> :bulb: If you don't know how to get your **token** and **key**, click [here](https://conta-sandbox.moip.com.br/configurations/api_credentials) (you must be logged in).

#### By OAuth
The following set will create an OAuth authentication object.

> :bulb: Click [here](https://dev.moip.com.br/v2.0/reference#autenticacao-mp) to know how to get your token OAuth.

```java
import br.com.moip.auth.Authentication;
import br.com.moip.auth.OAuth;

String oauth = "8833c9eb036543b6b0acd685a76c9ead_v2";

Authentication auth = new OAuth(oauth);
```

#### 1.2 Environment
We have some environments that you can send your requests. 

##### Sandbox
The test environment. You can use this to simulate all of your business scenarios.

```
Setup.Environment.SANDBOX
```

##### Production
**_"The environment of truth"_** :eyes:. This is the environment where the real transactions run.

```
Setup.Environment.PRODUCTION
```

> :bulb: Before going to production, you need to request homologation of your application [here](https://dev.moip.com.br/page/homologacao-api-v2).

##### Connect
The connect URL must be used only for operations involving another Moip accounts (_request connect permission, generate the account accessToken, refresh account accessToken_).

> :bulb: If you want to know more about the Moip Connect flow, check [here](https://dev.moip.com.br/docs/app-1) (PT-BR).

**Sandbox**
```
Setup.Environment.CONNECT_SANDBOX
```

**Production**
```
Setup.Environment.CONNECT_PRODUCTION
```

#### 1.3 Finally
So, your setup must be something like this: 
```java
import br.com.moip.models.Setup;

Setup setup = new Setup().setAuthentication(auth).setEnvironment(Setup.Environment.SANDBOX);
```

### 2. Create customer
With the setup created, you can make requests to Moip API. To start the basic e-commerce flow you need to create a customer. After all, it's whom will order your products or services.

```java
import static br.com.moip.helpers.PayloadFactory.payloadFactory;
import static br.com.moip.helpers.PayloadFactory.value;

Map<String, Object> taxDocument = payloadFactory(
        value("type", "CPF"),
        value("number", "10013390023")
);

Map<String, Object> phone = payloadFactory(
        value("countryCode", "55"),
        value("areaCode", "11"),
        value("number", "22226842")
);

Map<String, Object> shippingAddress = payloadFactory(
        value("city", "Sao Paulo"),
        value("district", "Itaim BiBi"),
        value("street", "Av. Brigadeiro Faria Lima"),
        value("streetNumber", "3064"),
        value("state", "SP"),
        value("country", "BRA"),
        value("zipCode", "01451001")
);

Map<String, Object> customerRequestBody = payloadFactory(
        value("ownId", "customer_own_id"),
        value("fullname", "Test Moip da Silva"),
        value("email", "test.moip@mail.com"),
        value("birthDate", "1980-5-10"),
        value("taxDocument", taxDocument),
        value("phone", phone),
        value("shippingAddress", shippingAddress)
);

Map<String, Object> responseCreation = Moip.API.customers().create(customerRequestBody, setup);
```

> Read more about customer on [API reference](https://dev.moip.com.br/v2.0/reference#clientes-ec).

###  3. Create order
Customer created! It's buy time! :tada:

```java
import static br.com.moip.helpers.PayloadFactory.payloadFactory;
import static br.com.moip.helpers.PayloadFactory.value;

Map<String, Object> subtotals = payloadFactory(
        value("shipping", 15000)
);

Map<String, Object> amount = payloadFactory(
        value("currency", "BRL"),
        value("subtotals", subtotals)
);

Map<String, Object> product1 = payloadFactory(
        value("product", "Product 1 Description"),
        value("category", "TOYS_AND_GAMES"),
        value("quantity", 2),
        value("detail", "Anakin's Light Saber"),
        value("price", 100000000)
);

Map<String, Object> product2 = payloadFactory(
        value("product", "Product 2 Description"),
        value("category", "SCIENCE_AND_LABORATORY"),
        value("quantity", 5),
        value("detail", "Pym particles"),
        value("price", 2450000000)
);

List items = new ArrayList();
items.add(product1);
items.add(product2);

Map<String, Object> customer = payloadFactory(
        value("id", "CUS-XXOBPZ80QLYP")
);

Map<String, Object> order = payloadFactory(
        value("ownId", "order_own_id"),
        value("amount", amount),
        value("items", items),
        value("customer", customer)
);

Map<String, Object> responseCreation = Moip.API.orders().create(order, setup);
```

> Read more about order on [API reference](https://dev.moip.com.br/v2.0/reference#pedidos-ec).

### 4. Create Payment
Alright! Do you have all you need? So, lets pay this order. :moneybag:

```java
import static br.com.moip.helpers.PayloadFactory.payloadFactory;
import static br.com.moip.helpers.PayloadFactory.value;

Map<String, Object> taxDocument = payloadFactory(
        value("type", "CPF"),
        value("number", "33333333333")
);

Map<String, Object> phone = payloadFactory(
        value("countryCode", "55"),
        value("areaCode", "11"),
        value("number", "66778899")
);

Map<String, Object> holder = payloadFactory(
        value("fullname", "Portador Teste Moip"),
        value("birthdate", "1988-12-30"),
        value("taxDocument", taxDocument),
        value("phone", phone)
);

Map<String, Object> creditCard = payloadFactory(
        value("hash", "CREDIT_CARD_HASH"),
        value("store", false),
        value("holder", holder)
);

Map<String, Object> fundingInstrument = payloadFactory(
        value("method", "CREDIT_CARD"),
        value("creditCard", creditCard)
);

Map<String, Object> payment = payloadFactory(
        value("installmentCount", 1),
        value("statementDescriptor", "minhaLoja.com"),
        value("fundingInstrument", fundingInstrument)
);

Map<String, Object> newPay = Moip.API.payments().pay(payment, "order_id", setup);
```

> Read more about payment on [API reference](https://dev.moip.com.br/v2.0/reference#pagamentos-ec).

### Other examples
If you want to see other functional examples, check the [Wiki](https://github.com/moip/moip-sdk-java/wiki).

## Exceptions treatment
| errors | cause | status |
| :---: | :---: | :---: |
| UnautorizedException | to authentication errors | == 401 |
| ValidationException | to validation errors | >= 400 && <= 499 (except 401) |
| UnexpectedException | to unexpected errors | >= 500 |

> :warning: To catch these errors, use the bellow treatment:

```java
import br.com.moip.exception.UnauthorizedException;
import br.com.moip.exception.UnexpectedException;
import br.com.moip.exception.ValidationException;

try {
    
    Map<String, Object> newPay = Moip.API.payments().pay(payment, "order_id", setup);
    
} catch(UnauthorizedException e) {
  // StatusCode == 401
} catch(UnexpectedException e) {
  // StatusCode >= 500
} catch(ValidationException e) {
  // StatusCode entre 400 e 499 (exceto 401)
}
```

## Moip documentation

### Docs
To stay up to date about the **Moip Products**, check the [documentation](https://dev.moip.com.br/v2.0/docs).

### References
Read more about the **Moip APIs** in [API reference](https://dev.moip.com.br/v2.0/reference).

## Getting help
We offer many ways to contact us, so if you have a question, do not hesitate, talk to us whatever you need. For questions about API or business rules, contact us by [support](https://dev.moip.com.br/v2.0/) or [slack](https://slackin-cqtchmfquq.now.sh/):slack:. But, if you have a question or suggestion about the SDK, feel free to open an **issue** or **pull request**.

## Contributing
Do you have an enhancement suggest or found something to fix? Go ahead, help us and let your mark on Moip, open **pull requests** and **issues** against this project. If you want to do it, please read the `CONTRIBUTING.md` to be sure everyone follows the same structure and planning of the project. Remember, we :heart: contributions. :rocket: