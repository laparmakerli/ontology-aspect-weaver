# Aspect Oriented Ontologies

Inspired by Aspect Oriented Programming (AOP), Aspect Oriented Ontology Development deals with the problem of cross-cutting-concerns in knowledge engineering. The requirements on an ontology differ in terms of different (stakeholder) perspectives. One and the same phenomenon might require a completely different description regarding different views on it. Facts which are essential in the perspective of one application domain might be completely irrelevant from another perspective. Furthermore knowledge modeling has to deal with inevitable natural inconsistencies since the validity of knowledge is often controversial or only valid under certain conditions.
The primitives of OWL do not natively support logical constructs allowing such metamodeling of information.

Equivalent to the aspect-oriented programming paradigm the introduction of aspects as a metalinguistic construct for the description of ontologies can provide mechanisms to deal with different concerns independently and lead to a further modularization of these. Different parts of an ontology sharing certain meta-properties are associated with the corresponding aspect. Therefore an aspect defines a subset of axioms of an ontology and hence a self contained ontology-module.

Depending on the different requirements on an ontology for different application domains modules of an ontology can be extracted by selecting all relevant aspects.

An implementation of this approach is described [here](http://www.diss.fu-berlin.de/docs/servlets/MCRFileNodeServlet/FUDOCS_derivate_000000005284/CSCReport2015.pdf), where aspects are modeled as OWL2-classes that can be connected to arbitrary axioms via OWL2-annotation properties, meaning that the semantics of the axiom is valid in the context of the corresponding aspect. As defined in ... "Annotations carry no semantics in the OWL 2 Direct Semantics". Therefore they are ignored by description logic reasoners.

# Aspect Weaver

The purpose of this Aspect Weaver is translating aspect-ontologies automatically into, in particular concerning the semantics added by aspects, fully interpretable OWL2 ontologies. The Aspect Weaver brings together the knowledge defined in the different modules on a level suited for machine processing.


# Ontology Design Pattern

The Weaver uses ontology design patterns that allow the modeling of aspect-semantics with current OWL2 features. An ontology design pattern is a reusable successful solution to a recurrent modeling problem. The following two patterns have been implemented.

## Context Slices Pattern

The context slices pattern is an approach to model contextualized information that for example "may be believed by a person or organization [or] hold only for some time period". This comes very near to the knowledge modeling approach of AOOD. Instead of adding meta-information to axioms it uses "projections of relation arguments in each context for which some binary relation holds between them". Projections are modeled as object properties.
Since the projection property can only be applied to individuals this pattern is limited to the modeling of contextualizations of relations that take individuals as arguments - hence object properties and data properties. In order to obtain the semantics of the Aspect approach we interpret contextes as possible worlds. Projections of individuals are interpreted as the extension of that individual in in the world/context associated with it.


## N'ary Relation Pattern

Another possible way for modeling aspects with OWL standard functionality is using n-ary relations. A corresponding ontology design pattern is presented [here](http://ontologydesignpatterns.org/wiki/Submissions:N-Ary_Relation_Pattern_(OWL_2)).  A property between two entities with an aspect assertion is modeled as an 3-ary relation between the 2 entities and the aspect.
git config --global