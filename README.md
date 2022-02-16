## Prolog CLI Software with Java jpl

The main goal of this project is to practice prolog and java jpl library.

And first of all, excuse my english.

This small system creates a prolog knowloge base with the prepositions give by the user.
The system was designed to receive prepositions in spanish.
I use Java to receive and pre-processing the prepositions. And than. with java i create a prolog file
and pass the preposition using the prolog syntax in form of horn clauses.

- The user can enter preposition in this form:
    - Si A entonces B => If A then B
    - Si A y B entonces C => If A and B then C
    - Si A o B entonces C and D => If A and B then C and D
Where A, B, C and can be any word. That's it's not necessary write the prepositions with with one char.

- The user can enter the number of preposition she/he want.
- After enter all preposition, the system request a fact/predicate based in the preposition entered by the user.
- After that, the system request the question that the user want to do in the knowledge base.
- And then, using the jpl library, the system consult the KB and show the response.

The system is very intiutive and can be easely used.
