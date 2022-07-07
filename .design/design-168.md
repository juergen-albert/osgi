# Condition Factory Design

## Introduction

The [Condition Service](http://docs.osgi.org/specification/osgi.core/8.0.0/service.condition.html ) allows to mark arbitrary states a system can listen for and react to. They can be traced programmatically or can work as indicator for [Declarative Services](https://docs.osgi.org/specification/osgi.cmpn/8.0.0/service.component.html) to start there lifecycle. 

This specification addresses the most common situations that may lead to the registration of a Condition service, such as:

* Availability or absence of Services
* Events that have or have not occurred
* Satisfaction of Requirements for Capabilities in the Framework
* State of Bundles

[Declarative Services](https://docs.osgi.org/specification/osgi.cmpn/8.0.0/service.component.html) already provide a powerful mechanism to describe necessities of a Service or Component. It is however wholly focused on the availability Service References and Instances. The Condition Factory thus provides a convenient way to widen the scope services can react to. 

## Essentials

* *Simplicity* - Prerequisites of a Condition must be easily describable via a [Configuration](https://docs.osgi.org/specification/osgi.cmpn/8.0.0/service.cm.html#org.osgi.service.cm.Configuration). 

## Entities

* *Condition* - An Instance of a http://docs.osgi.org/specification/osgi.core/8.0.0/service.condition.html 
* *Prerequisite* - In OSGi we have different methods of describing something that is needed. For different Situations, different names are used and maybe reused in this Specification. In order to not confuse Terms that are already loaded with meaning, a prerequisite will be the named concept that can describe e.g. a Filter, a Requirement or a Condition.
* *Condition Factory Configuration* - A Configuration describing the properties of the to be registered Condition and an optional set of Prerequisites that must and or must not be satisfied. 
* *Service Reference Condition* - A Condition registered based on a described set of Target Filters for a single or multiple  Service References.
* *Service Instance Condition* - A Condition registered based on a described set of Target Filters for a single  or multiple  Service Instances.
* *Bundle Condition* - A Condition registered based on a described set of Bundle states for a single  or multiple Bundles.
* *Framework Wiring Capability Condition* - A Condition registered based on a described set of [Requirements](http://docs.osgi.org/specification/osgi.core/8.0.0/framework.module.html#framework.module.dependencies) that must be fulfilled.
* *Event Condition* - A Condition registered based on the occurrence or certain [Events](https://docs.osgi.org/specification/osgi.cmpn/8.0.0/service.event.html#d0e40027) or lack thereof.







# OLD Version

In order to create more complex Conditions easily a configurable Factory is called for. This allows integration of Conditions in a Systems without the requirement to actually register a Condition Service.

The implementation implementation of the `ConditionFactory` should register a `org.osgi.service.cm.ManagedServiceFactory` with the service PID `osgi.conditionfactory`. A configuration it might receive via ConfigurationAdmin can contain two lists of target filter. One for filters that MUST find at least one Service each in order to have a Condition registered. The other one represents a List of filters, that MUST NOT find any matching service. The moment all the included and non of the exclude filters match a Service, a Condition is registered with the given identifier and the additional properties. If any of the include Services go away or an excluded Service becomes available, this Condition must be unregistered.

## List of properties

Name | Value
------------ | -------------
`osgi.condition.identifier` | The `Constants.CONDITION_ID` that will be set to the condition that will be registered if all filters are satisfied
`osgi.condition.properties.*` | Properties like this will be registered with the condition if all filters are satisfied. The key will be the * part.
`osgi.condition.match.all` | An optional list of valid target filters. A Condition will be registered if each filter finds at least one matching service.
`osgi.condition.match.none` | An optional list of valid target filters. A Condition will not be registered if any filter finds at least one matching service.

## Example

An Example Configuration utilizing the Configurator would look like as follows:

```json
{
    ":configurator:resource-version": 1,
    "osgi.serviceref.conditionfactory~test" : {
        "osgi.condition.identifier" : "resulting.condition.id",
        "osgi.condition.properties.custom.condition.prop" : "my.property",
        "osgi.condition.match.all" : [
            "(&(objectClass=org.foo.Bar)(my.prop=foo))",
            "(my.prop=bar)"
        ],
        "osgi.condition.match.none" : [
            "(&(objectClass=org.foo.Fizz)(my.prop=buzz))"
        ]
    }
},
{
    ":configurator:resource-version": 1,
    "osgi.serviceref.conditionfactory~test" : {
        "osgi.condition.identifier" : "resulting.condition.id",
        "osgi.condition.properties.custom.condition.prop" : "my.property",
        "osgi.condition.match.all" : [
            {
                "osgi.service;filter:=(&(objectClass=org.foo.Bar)(my.prop=foo))"
            },
            {
                "osgi.bundle;filter:=(&(bundle.symbolic.name=org.foo.Bar)(bundle.state=ACTIVE))"
            }
        ],
        "osgi.condition.match.none" : [
            {
                "osgi.service;filter:=(&(objectClass=org.foo.Fizz)(my.prop=buzz))"
            }
        ]
    }
},
{
    ":configurator:resource-version": 1,
    "osgi.serviceinstance.conditionfactory~test" : {
        "osgi.condition.identifier" : "resulting.condition.id",
        "osgi.condition.properties.custom.condition.prop" : "my.property",
        "osgi.condition.match.all" : [
            "(&(objectClass=org.foo.Bar)(my.prop=foo))"
        ],
        "osgi.condition.match.none" : [
            "(&(objectClass=org.foo.Fizz)(my.prop=buzz))"
        ]
    }
}
```
