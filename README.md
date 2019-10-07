# SimpleMoneyTransfers

Installation instructions
---

* In the root folder of your project, run `mvn clean install` to build the application. The app was tested on OpenJDK ver. `1.8.0_212` and Maven `3.5.0`.
* Start the application with `java -jar target/simple-money-transfers-1.0-SNAPSHOT.jar server config.yml`.
* The following API will be available at `http://localhost:8090`:

| URI                        | HTTP Method |
|----------------------------|-------------|
| /api/account               | GET         |
| /api/account               | POST        |
| /api/account               | PUT         |
| /api/account/{accountId}   | GET         |
| /api/account/{accountId}   | DELETE      |
| /api/transfer              | GET         |
| /api/transfer              | POST        |
| /api/transfer/{transferId} | GET         |

For example, you can create a new account in the system by making a `POST` request to `http://localhost:8090/api/account` with the header `Content-Type: application/json` and the following content:
```json
{
  "creationTime": "2019-01-02T11:22:33",
  "holderName": "TestAccount1",
  "balance": 1000,
  "active": true,
  "description": "Some description"
}
```
The in-memory database is pre-populated with a few records for accounts and transfers, you can find them here: `resources/init_data.sql`. Thus, you can make use of the existing accounts and create a new transfer by making a `POST` request with the mandatory query parameters `fromAccount`, `toAccount`, `amount`, and an empty body as follows:
```http request
http://localhost:8090/api/transfer/?fromAccount=2&toAccount=1&amount=300&description=TestTransfer_2-1
```