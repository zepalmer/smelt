Smelt
=====

This project is intended to provide an editor for structured data models and
their instances.  Consider the following simple Smelt specification:

    data: Person
        Name: text
        Age: text
        Employment
    enum: Employment
        Employed, Unemployed
    editor: Person
        container: vertical
            container: horizontal
                Name
                Age
            container
                Employment

This specification indicates the existence of a Person data type (which relies
on an Employment data type) and also describes the layout for a simple editor
for an instance of Person.  While this file dictates a data model, another file
contains the database for this model.  Smelt then permits the editing of the
data model instance.

In order to admit general scalability, each top-level declaration in a Smelt
model file is handled by a different declaration processor.  The Smelt framework
then permits plugins to be loaded which add declaration processors, so the
semantics of the Smelt specification language are not fixed.  The parser,
however, is always indentation-based and maintains a consistent base syntax.

