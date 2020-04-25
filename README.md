# Hubspot Java API

A java client for the HubSpot API.

# Usage

## Construct the client

### From an API key
```
HubSpot hub = HubSpot.fromKey("mysecretkey")
```

### From an environment variable
```
HubSpot hub = HubSpot.fromEnvironment("HUBSPOT_KEY")
```

# Companies

## Create a company
```
hub.companies().create(props);
```

## Update a company
```
hub.companies().create(123, props);
```

## Stream all companies
```
hub.companies().all();
```

# Maven
Use maven – one of life's necessary evils – to fetch the latest version of this library.

## Add repository
```
<repository>
    <id>bintray-i386-maven</id>
    <name>bintray</name>
    <url>https://dl.bintray.com/i386/maven</url>
    <snapshots>
        <enabled>false</enabled>
    </snapshots>
</repository>
```

## Add dependency
```
<dependency>
    <groupId>com.wildspirit.hubspot</groupId>
    <artifactId>hubspot-java-api</artifactId>
    <version>0.1-SNAPSHOT</version>
</dependency>
```

# License

MIT License
