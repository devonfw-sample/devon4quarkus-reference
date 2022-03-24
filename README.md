# devon4quarkus cloud native reference project

This is the reference project of [devon4quarkus](https://github.com/devonfw/devon4quarkus).

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Database & Jaeger & Prometheus

The app uses data persistence and you need a working database to use it. We also use tracing and metrics collector.
There is a `docker-compose.yaml` in the root of this repo that provides all of them.
You can start the DB and Jaeger containers using simple cmd:
```
docker-compose up
```
If you want to use other DB, modify the params in `application.properties`. 

For using Cloud DBs, refer the steps in [AWS DB](https://github.com/devonfw-sample/devon4quarkus-reference/blob/master/documentation/aws-db-steps.asciidoc) or [Azure DB](https://github.com/devonfw-sample/devon4quarkus-reference/blob/master/documentation/azure-db-steps.asciidoc).

To access Jaeger UI(tracing): http://localhost:16686  
To access Prometheus(metrics): http://localhost:9090/graph
To access health check of our app: http://localhost:8080/q/health

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Running tests

You can run tests from your IDE or via Maven. Simply run `./mvnw test ` or `./mvnw package`

## Access your REST endpoint

Go to http://localhost:8080/product/v1


## OpenAPI & Swagger UI

With your app running, go to http://localhost:8080/q/swagger-ui to see the Swagger UI visualizing your API. You can access the YAML OpenAPI schema under http://localhost:8080/q/openapi

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/demo-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

## Maven settings

It is recommended to use vanilla maven settings (no custom mirror, proxy) for better performance. If you have modified your default settings `~/.m2/settings.xml` please revert it, or run the maven commands with the clean settings included in this project using  `-s ./settings.xml`

## Deploy to kubernetes

To deploy the application, you need a Kubernetes cluster and a registry from which to pull the application image.

Package your app as docker container and push the image to your local registry:

```shell
docker build -f src/main/docker/Dockerfile.jvm . -t your-registry/demo-quarkus:latest
docker push your-registry/demo-quarkus:latest
```

Also enter the path to your registry in the `k8s/application-deployment.yaml` file so that Kubernetes knows where to get the image from. The location to change is marked with a "TODO" comment.

Then apply the k8s resources to your cluster (make sure your kubectl has the correct context first)

```shell
kubectl apply -f k8s/postgres-deployment.yaml
kubectl apply -f k8s/postgres-service.yaml
kubectl apply -f k8s/application-deployment.yaml
kubectl apply -f k8s/application-service.yaml
kubectl apply -f k8s/ingress.yaml
```

Give it a few moments and then open http://demo-quarkus.localhost/products/ in your browser.

- For deploying in **AWS** EKS Cluster, refer this [link](https://github.com/devonfw-sample/devon4quarkus-reference/blob/master/documentation/aws-k8s-steps.asciidoc).
- For deploying in **Azure** AKS Cluster, refer this [link](https://github.com/devonfw-sample/devon4quarkus-reference/blob/master/documentation/azure-k8s-steps.asciidoc).

## Helm

> **_NOTE:_**  Be sure to remove your old resources first
```shell
kubectl delete -f k8s
```

First, in the `src/main/helm/values.yaml` file, specify the path to your registry from which you want to obtain the image.
Then you can deploy the application with the following command:

```shell
helm install demo-quarkus src/main/helm
```

This will deploy the application and the corresponding Postgres database.
Try it out by opening http://demo-quarkus.localhost/products/ in your browser.

To terminate the instances use the following command:
```shell
helm uninstall demo-quarkus
```

### OpenTelemetry integration

Quarkus can be easily configured to support OpenTelemetry features that can be used in combination with tools such as Jaeger or VictoriaMetrics to monitor traces and metrics.
To learn more about OpenTelemetry, see the devonfw architecture browser in the [chapter about OpenTelemetry](https://devonfw.com/website/pages/architectures/solutions/monitoring_openTelemetry/).

The `documentation` folder contains a guide with instructions on how to set up the application in combination with these tools.
