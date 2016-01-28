# GraphQLGenerator

It is generator for GraphQL schema from Java classes. It is expected that code will use [graphql-java](https://github.com/andimarek/graphql-java).

It is in working, but usage and code can be improved.
To use it you need to edit Main.java and fill typeMap.
TypeMap is map from java class name to the name you want GraphQL object to have.
Each new object needs to be in it. Unless it is scalar (integer, short, float, double, boolean, char).

Then you need to call ReadClass or ReadEnum with typeMap and call readFile with path to wanted java file. It prints resulting GraphQL.

ReadClass can be used to read classes which will be objects or operations. In that case you need to call ReadClass with second parameter to true).

In java classes it reads:
* class comments as object description
* public variables
* Comments before each variable (this is description in GraphQL)
	* here it also supports @notnull
	* and @default: if class is used as GraphQL operation
* it also adds datafetcher that calls public variable

In enums it reads:
* enum comments as object description
* enum values comments as value description
* Value and text value

## Future wishes:
* prettier code
* better documentation
* ability to add root java file and it automatically generates Graphql code for it and all its dependencies
* nicer templates

## License
MIT
